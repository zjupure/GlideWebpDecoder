package com.bumptech.glide.integration.framesequence;

import android.graphics.Bitmap;
import android.support.rastermill.FrameSequence;
import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

/**
 * Lazily allocates a {@link android.support.rastermill.FrameSequenceDrawable} from a given
 * {@link android.support.rastermill.FrameSequence} on the first call to {@link #get()}.
 *
 * author: liuchun
 * date: 2018/1/6
 */
public class LazyFsDrawableResource implements Resource<FrameSequenceDrawable>{

    private final FrameSequence frameSequence;
    private final FrameSequenceDrawable.BitmapProvider bitmapProvider;

    public LazyFsDrawableResource(FrameSequence frameSequence, final BitmapPool bitmapPool) {
        this.frameSequence = frameSequence;
        this.bitmapProvider = new FrameSequenceDrawable.BitmapProvider() {
            @Override
            public Bitmap acquireBitmap(int minWidth, int minHeight) {
                return bitmapPool.get(minWidth, minHeight, Bitmap.Config.ARGB_8888);
            }

            @Override
            public void releaseBitmap(Bitmap bitmap) {
                bitmapPool.put(bitmap);
            }
        };
    }

    @Override
    public Class<FrameSequenceDrawable> getResourceClass() {
        return FrameSequenceDrawable.class;
    }

    @Override
    public FrameSequenceDrawable get() {
        return new FrameSequenceDrawable(frameSequence, bitmapProvider);
    }

    @Override
    public int getSize() {
        return frameSequence.getWidth() * frameSequence.getHeight() * 4;
    }

    @Override
    public void recycle() {

    }
}
