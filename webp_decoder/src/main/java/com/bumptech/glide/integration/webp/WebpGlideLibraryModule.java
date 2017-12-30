package com.bumptech.glide.integration.webp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.webp.decoder.ByteBufferBitmapWebpDecoder;
import com.bumptech.glide.integration.webp.decoder.StreamBitmapWebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDownsampler;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableEncoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder;
import com.bumptech.glide.load.resource.bitmap.ByteBufferBitmapDecoder;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
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
        // 动态webp
        registry.prepend(ByteBuffer.class, WebpDrawable.class, byteBufferWebpDecoder);
        registry.prepend(InputStream.class, WebpDrawable.class, new StreamWebpDecoder(byteBufferWebpDecoder, glide.getArrayPool()));
        registry.prepend(WebpDrawable.class, new WebpDrawableEncoder());

        // 静态无损/透明webp，Android 4.2.1以下
        final Resources resources = context.getResources();
        final BitmapPool bitmapPool = glide.getBitmapPool();
        final ArrayPool arrayPool = glide.getArrayPool();
        Downsampler downsampler = new Downsampler(registry.getImageHeaderParsers(),
                resources.getDisplayMetrics(), bitmapPool, arrayPool);
        WebpDownsampler webpDownsampler = new WebpDownsampler(registry.getImageHeaderParsers(),
                resources.getDisplayMetrics(), bitmapPool, arrayPool, downsampler);
        ByteBufferBitmapDecoder byteBufferBitmapDecoder = new ByteBufferBitmapWebpDecoder(webpDownsampler);
        StreamBitmapDecoder streamBitmapDecoder = new StreamBitmapWebpDecoder(webpDownsampler, arrayPool);
        registry
                /* Bitmaps for Static Webps */
                .prepend(Registry.BUCKET_BITMAP, ByteBuffer.class, Bitmap.class, byteBufferBitmapDecoder)
                .prepend(Registry.BUCKET_BITMAP, InputStream.class, Bitmap.class, streamBitmapDecoder)
                /* BitmapDrawables for Static Webps */
                .prepend(
                        Registry.BUCKET_BITMAP_DRAWABLE,
                        ByteBuffer.class,
                        BitmapDrawable.class,
                        new BitmapDrawableDecoder<>(resources, bitmapPool, byteBufferBitmapDecoder))
                .prepend(
                        Registry.BUCKET_BITMAP_DRAWABLE,
                        InputStream.class,
                        BitmapDrawable.class,
                        new BitmapDrawableDecoder<>(resources, bitmapPool, streamBitmapDecoder));
    }
}
