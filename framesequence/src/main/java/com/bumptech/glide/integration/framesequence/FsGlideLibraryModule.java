package com.bumptech.glide.integration.framesequence;

import android.content.Context;
import android.support.rastermill.FrameSequence;
import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.module.LibraryGlideModule;

import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author liuchun
 */
@GlideModule
public class FsGlideLibraryModule extends LibraryGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

        final BitmapPool bitmapPool = glide.getBitmapPool();
        final ArrayPool arrayPool = glide.getArrayPool();
        final ByteBufferFsDecoder bufferFsDecoder = new ByteBufferFsDecoder(registry.getImageHeaderParsers());
        final StreamFsDecoder streamFsDecoder = new StreamFsDecoder(registry.getImageHeaderParsers(), arrayPool);
        registry /* FrameSequences */
                .prepend(ByteBuffer.class, FrameSequence.class, bufferFsDecoder)
                .prepend(InputStream.class, FrameSequence.class, streamFsDecoder)
                /* FrameSequencesDrawables */
                .prepend(ByteBuffer.class, FrameSequenceDrawable.class,
                        new FsDrawableDecoder<>(bitmapPool, bufferFsDecoder))
                .prepend(InputStream.class, FrameSequenceDrawable.class,
                        new FsDrawableDecoder<>(bitmapPool, streamFsDecoder))
                /* Transcodes */
                .register(FrameSequence.class, FrameSequenceDrawable.class,
                        new FsDrawableTranscoder(bitmapPool));
    }
}
