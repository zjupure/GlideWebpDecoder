package com.bumptech.glide.integration.webp.decoder;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * author: liuchun
 * date:  2017/10/21
 */
public class WebpFrameLoader {

    private final GifDecoder gifDecoder;
    private final Handler handler;
    private final List<FrameCallback> callbacks;
    final RequestManager requestManager;
    private final BitmapPool bitmapPool;
    private boolean isRunning;
    private boolean isLoadPending;
    private boolean startFromFirstFrame;
    private RequestBuilder<Bitmap> requestBuilder;
    private DelayTarget current;
    private boolean isCleared;
    private DelayTarget next;
    private Bitmap firstFrame;
    private Transformation<Bitmap> transformation;

    public WebpFrameLoader(Glide glide, GifDecoder gifDecoder, int width, int height, Transformation<Bitmap> transformation, Bitmap firstFrame) {
        this(glide.getBitmapPool(), Glide.with(glide.getContext()), gifDecoder, (Handler) null, getRequestBuilder(Glide.with(glide.getContext()), width, height), transformation, firstFrame);
    }

    WebpFrameLoader(BitmapPool bitmapPool, RequestManager requestManager, GifDecoder gifDecoder, Handler handler, RequestBuilder<Bitmap> requestBuilder, Transformation<Bitmap> transformation, Bitmap firstFrame) {
        this.callbacks = new ArrayList<>();
        this.isRunning = false;
        this.isLoadPending = false;
        this.startFromFirstFrame = false;
        this.requestManager = requestManager;
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper(), new FrameLoaderCallback());
        }

        this.bitmapPool = bitmapPool;
        this.handler = handler;
        this.requestBuilder = requestBuilder;
        this.gifDecoder = gifDecoder;
        this.setFrameTransformation(transformation, firstFrame);
    }

    void setFrameTransformation(Transformation<Bitmap> transformation, Bitmap firstFrame) {
        this.transformation = Preconditions.checkNotNull(transformation);
        this.firstFrame = (Bitmap) Preconditions.checkNotNull(firstFrame);
        requestBuilder = requestBuilder.apply((new RequestOptions()).transform(transformation));
    }

    Transformation<Bitmap> getFrameTransformation() {
        return this.transformation;
    }

    Bitmap getFirstFrame() {
        return this.firstFrame;
    }

    void subscribe(FrameCallback frameCallback) {
        if (isCleared) {
            throw new IllegalStateException("Cannot subscribe to a cleared frame loader");
        } else {
            boolean start = callbacks.isEmpty();
            if (callbacks.contains(frameCallback)) {
                throw new IllegalStateException("Cannot subscribe twice in a row");
            } else {
                callbacks.add(frameCallback);
                if (start) {
                    start();
                }

            }
        }
    }

    void unsubscribe(FrameCallback frameCallback) {
        callbacks.remove(frameCallback);
        if (callbacks.isEmpty()) {
            stop();
        }

    }

    int getWidth() {
        return getCurrentFrame().getWidth();
    }

    int getHeight() {
        return getCurrentFrame().getHeight();
    }

    int getSize() {
        return gifDecoder.getByteSize() + getFrameSize();
    }

    int getCurrentIndex() {
        return current != null ? current.index : -1;
    }

    private int getFrameSize() {
        return Util.getBitmapByteSize(getCurrentFrame().getWidth(), getCurrentFrame().getHeight(), getCurrentFrame().getConfig());
    }

    ByteBuffer getBuffer() {
        return gifDecoder.getData().asReadOnlyBuffer();
    }

    int getFrameCount() {
        return gifDecoder.getFrameCount();
    }

    int getLoopCount() {
        return gifDecoder.getTotalIterationCount();
    }

    private void start() {
        if (!isRunning) {
            isRunning = true;
            isCleared = false;
            loadNextFrame();
        }
    }

    private void stop() {
        isRunning = false;
    }

    void clear() {
        callbacks.clear();
        recycleFirstFrame();
        stop();
        if (current != null) {
            requestManager.clear(current);
            current = null;
        }

        if (next != null) {
            requestManager.clear(next);
            next = null;
        }

        gifDecoder.clear();
        isCleared = true;
    }

    Bitmap getCurrentFrame() {
        return current != null ? current.getResource() : firstFrame;
    }

    private void loadNextFrame() {
        if (isRunning && !isLoadPending) {
            if (startFromFirstFrame) {
                gifDecoder.resetFrameIndex();
                startFromFirstFrame = false;
            }

            isLoadPending = true;
            int delay = gifDecoder.getNextDelay();
            long targetTime = SystemClock.uptimeMillis() + (long) delay;
            gifDecoder.advance();
            next = new DelayTarget(handler, gifDecoder.getCurrentFrameIndex(), targetTime);
            requestBuilder.clone().apply(RequestOptions.signatureOf(new FrameSignature())).load(gifDecoder).into(next);
        }
    }

    private void recycleFirstFrame() {
        if (firstFrame != null) {
            bitmapPool.put(firstFrame);
            firstFrame = null;
        }

    }

    void setNextStartFromFirstFrame() {
        Preconditions.checkArgument(!isRunning, "Can\'t restart a running animation");
        startFromFirstFrame = true;
    }

    void onFrameReady(DelayTarget delayTarget) {
        if (isCleared) {
            handler.obtainMessage(2, delayTarget).sendToTarget();
        } else {
            if (delayTarget.getResource() != null) {
                recycleFirstFrame();
                DelayTarget previous = current;
                current = delayTarget;

                for (int i = callbacks.size() - 1; i >= 0; --i) {
                    FrameCallback cb = (FrameCallback) callbacks.get(i);
                    cb.onFrameReady();
                }

                if (previous != null) {
                    handler.obtainMessage(2, previous).sendToTarget();
                }
            }

            isLoadPending = false;
            loadNextFrame();
        }
    }

    private static RequestBuilder<Bitmap> getRequestBuilder(RequestManager requestManager, int width, int height) {
        return requestManager.asBitmap().apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE).skipMemoryCache(true).override(width, height));
    }

    static class FrameSignature implements Key {
        private final UUID uuid;

        public FrameSignature() {
            this(UUID.randomUUID());
        }

        FrameSignature(UUID uuid) {
            this.uuid = uuid;
        }

        public boolean equals(Object o) {
            if (o instanceof FrameSignature) {
                FrameSignature other = (FrameSignature) o;
                return other.uuid.equals(this.uuid);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return uuid.hashCode();
        }

        public void updateDiskCacheKey(MessageDigest messageDigest) {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    static class DelayTarget extends SimpleTarget<Bitmap> {
        private final Handler handler;
        final int index;
        private final long targetTime;
        private Bitmap resource;

        DelayTarget(Handler handler, int index, long targetTime) {
            this.handler = handler;
            this.index = index;
            this.targetTime = targetTime;
        }

        Bitmap getResource() {
            return this.resource;
        }

        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
            this.resource = resource;
            Message msg = handler.obtainMessage(1, this);
            handler.sendMessageAtTime(msg, targetTime);
        }
    }

    private class FrameLoaderCallback implements Handler.Callback {
        public static final int MSG_DELAY = 1;
        public static final int MSG_CLEAR = 2;

        FrameLoaderCallback() {
        }

        public boolean handleMessage(Message msg) {
            DelayTarget target;
            if (msg.what == 1) {
                target = (DelayTarget) msg.obj;
                WebpFrameLoader.this.onFrameReady(target);
                return true;
            } else {
                if (msg.what == 2) {
                    target = (DelayTarget) msg.obj;
                    WebpFrameLoader.this.requestManager.clear(target);
                }

                return false;
            }
        }
    }

    public interface FrameCallback {
        void onFrameReady();
    }
}
