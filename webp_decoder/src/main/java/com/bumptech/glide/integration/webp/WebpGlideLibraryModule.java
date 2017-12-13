package com.bumptech.glide.integration.webp;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableEncoder;
import com.bumptech.glide.module.LibraryGlideModule;
import com.bumptech.glide.integration.webp.decoder.ByteBufferWebpDecoder;
import com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;

import java.io.InputStream;
import java.nio.ByteBuffer;


@GlideModule
public class WebpGlideLibraryModule extends LibraryGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

        ByteBufferWebpDecoder byteBufferWebpDecoder =
                new ByteBufferWebpDecoder(context, glide.getArrayPool(), glide.getBitmapPool());
        // 需要插入到数组的最前面
        registry.prepend(ByteBuffer.class, WebpDrawable.class, byteBufferWebpDecoder);
        registry.prepend(InputStream.class, WebpDrawable.class, new StreamWebpDecoder(byteBufferWebpDecoder, glide.getArrayPool()));
        registry.prepend(WebpDrawable.class, new WebpDrawableEncoder());
    }
}
