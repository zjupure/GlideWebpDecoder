package com.bumptech.glide.integration.framesequence;

import android.graphics.Bitmap;
import android.support.rastermill.FrameSequence;
import android.support.rastermill.FrameSequenceDrawable;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.io.IOException;

/**
 * author: liuchun
 * date: 2018/1/6
 */
public class FsDrawableDecoder<DataType> implements ResourceDecoder<DataType, FrameSequenceDrawable>{

    private final ResourceDecoder<DataType, FrameSequence> decoder;
    private final FrameSequenceDrawable.BitmapProvider bitmapProvider;

    public FsDrawableDecoder(final BitmapPool bitmapPool, ResourceDecoder<DataType, FrameSequence> decoder) {
        this.decoder = decoder;
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
    public boolean handles(DataType dataType, Options options) throws IOException {
        return decoder.handles(dataType, options);
    }

    @Nullable
    @Override
    public Resource<FrameSequenceDrawable> decode(DataType dataType, int width, int height, Options options) throws IOException {

        Resource<FrameSequence> fsResource = decoder.decode(dataType, width, height, options);
        if (fsResource == null) {
            return null;
        }

        // TODO modify the source code to support downsampling
        FrameSequenceDrawable fsDrawable = new FrameSequenceDrawable(fsResource.get(), bitmapProvider);
        return new FsDrawableResource(fsDrawable);
    }
}
