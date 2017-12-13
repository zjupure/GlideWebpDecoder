package com.bumptech.glide.integration.webp.decoder;

import android.support.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.integration.webp.WebpHeaderParser;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


/**
 * An {@link com.bumptech.glide.load.ResourceDecoder} that decodes {@link
 *  com.bumptech.glide.integration.webp.decoder.WebpDrawable} from {@link java.io.InputStream} data
 *
 * @author liuchun
 */
public class StreamWebpDecoder implements ResourceDecoder<InputStream, WebpDrawable> {

    private final static String TAG = "StreamWebpDecoder";

    private final ResourceDecoder<ByteBuffer, WebpDrawable> byteBufferDecoder;
    private final ArrayPool byteArrayPool;

    public StreamWebpDecoder(ResourceDecoder<ByteBuffer, WebpDrawable> byteBufferDecoder, ArrayPool byteArrayPool) {
        this.byteBufferDecoder = byteBufferDecoder;
        this.byteArrayPool = byteArrayPool;
    }


    @Override
    public boolean handles(InputStream inputStream, Options options) throws IOException {

//        ImageHeaderParser.ImageType imageType = ImageHeaderParserUtils.getType(mParsers, inputStream, mByteArrayPool);
//        return imageType == ImageHeaderParser.ImageType.WEBP || imageType == ImageHeaderParser.ImageType.WEBP_A;
        WebpHeaderParser.WebpImageType webpType = WebpHeaderParser.getType(inputStream, byteArrayPool);
        return WebpHeaderParser.isAnimatedWebpType(webpType);
    }

    @Nullable
    @Override
    public Resource<WebpDrawable> decode(InputStream inputStream, int width, int height, Options options) throws IOException {

        byte[] data = inputStreamToBytes(inputStream);
        if (data == null) {
            return null;
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);

        return byteBufferDecoder.decode(byteBuffer, width, height, options);
    }


    private static byte[] inputStreamToBytes(InputStream is) {
        final int bufferSize = 16384;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream(bufferSize);
        try {
            int nRead;
            byte[] data = new byte[bufferSize];
            while ((nRead = is.read(data)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            if (Log.isLoggable(TAG, Log.WARN)) {
                Log.w(TAG, "Error reading data from stream", e);
            }
            return null;
        }
        return buffer.toByteArray();
    }

}
