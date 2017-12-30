package com.bumptech.glide.integration.webp.decoder;

import android.graphics.Bitmap;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Decodes Webp {@link android.graphics.Bitmap Bitmaps} from {@link java.io.InputStream InputStreams}.
 *
 * @author liuchun
 */
public class StreamBitmapWebpDecoder extends StreamBitmapDecoder {
    private final WebpDownsampler downsampler;


    public StreamBitmapWebpDecoder(WebpDownsampler downsampler, ArrayPool byteArrayPool) {
        super(downsampler.getDownsampler(), byteArrayPool);
        this.downsampler = downsampler;
    }

    @Override
    public boolean handles(InputStream source, Options options) throws IOException {
        return downsampler.handles(source);
    }

    @Override
    public Resource<Bitmap> decode(InputStream source, int width, int height, Options options) throws IOException {
        return downsampler.decode(source, width, height, options);
    }
}
