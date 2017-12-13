package com.bumptech.glide.integration.webp.decoder;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.bumptech.glide.integration.webp.WebpHeaderParser;
import com.bumptech.glide.integration.webp.WebpImage;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * An {@link com.bumptech.glide.load.ResourceDecoder} that decodes {@link
 *  com.bumptech.glide.integration.webp.decoder.WebpDrawable} from {@link java.nio.ByteBuffer} data
 *
 * author: liuchun
 * date:  2017/10/24
 */
public class ByteBufferWebpDecoder implements ResourceDecoder<ByteBuffer, WebpDrawable> {
    private static final String TAG = "BufferWebpDecoder";

    private final Context mContext;
    private final BitmapPool mBitmapPool;
    private final GifBitmapProvider mProvider;


    public ByteBufferWebpDecoder(Context context) {
        this(context, Glide.get(context).getArrayPool(),
                Glide.get(context).getBitmapPool());
    }

    public ByteBufferWebpDecoder(Context context, ArrayPool byteArrayPool, BitmapPool bitmapPool) {
        this.mContext = context.getApplicationContext();
        this.mBitmapPool = bitmapPool;
        this.mProvider = new GifBitmapProvider(bitmapPool, byteArrayPool);
    }

    @Override
    public boolean handles(ByteBuffer source, Options options) throws IOException {

        WebpHeaderParser.WebpImageType webpType = WebpHeaderParser.getType(source);
        return WebpHeaderParser.isAnimatedWebpType(webpType);
    }

    @Nullable
    @Override
    public Resource<WebpDrawable> decode(ByteBuffer source, int width, int height, Options options) throws IOException {

        int length = source.remaining();
        byte[] data = new byte[length];
        source.get(data, 0, length);

        WebpImage webp = WebpImage.create(data);

        int sampleSize = getSampleSize(webp.getWidth(), webp.getHeight(), width, height);
        WebpDecoder webpDecoder = new WebpDecoder(mProvider, webp, source, sampleSize);
        Bitmap firstFrame = webpDecoder.getNextFrame();
        if (firstFrame == null) {
            return null;
        }

        Transformation<Bitmap> unitTransformation = UnitTransformation.get();

        return new WebpDrawableResource(new WebpDrawable(mContext, webpDecoder, mBitmapPool, unitTransformation, width, height,
                firstFrame));
    }


    private static int getSampleSize(int srcWidth, int srcHeight, int targetWidth, int targetHeight) {
        int exactSampleSize = Math.min(srcHeight / targetHeight,
                srcWidth / targetWidth);
        int powerOfTwoSampleSize = exactSampleSize == 0 ? 0 : Integer.highestOneBit(exactSampleSize);
        // Although functionally equivalent to 0 for BitmapFactory, 1 is a safer default for our code
        // than 0.
        int sampleSize = Math.max(1, powerOfTwoSampleSize);
        if (Log.isLoggable(TAG, Log.VERBOSE) && sampleSize > 1) {
            Log.v(TAG, "Downsampling WEBP"
                    + ", sampleSize: " + sampleSize
                    + ", target dimens: [" + targetWidth + "x" + targetHeight + "]"
                    + ", actual dimens: [" + srcWidth + "x" + srcHeight + "]");
        }
        return sampleSize;
    }
}
