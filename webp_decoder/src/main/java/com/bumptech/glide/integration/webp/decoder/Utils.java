package com.bumptech.glide.integration.webp.decoder;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author liuchun
 */
public class Utils {
    private static final String TAG = "Utils";

    private static boolean USE_COMPACT_POOL_ON_Q = false;

    static int getSampleSize(int srcWidth, int srcHeight, int targetWidth, int targetHeight) {
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

    static byte[] inputStreamToBytes(InputStream is) {
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

    public static BitmapPool getCompactBitmapPool(BitmapPool bitmapPool) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return bitmapPool;
        }

        if (!USE_COMPACT_POOL_ON_Q) {
            return bitmapPool;
        }

        if (bitmapPool instanceof LruBitmapPool) {
            LruBitmapPool oldPool = (LruBitmapPool)bitmapPool;
            long poolSize = (long)(oldPool.getMaxSize() * 1.0f / 3.0f);
            LruBitmapPool newPool = new LruBitmapPool(poolSize);
            try {
                Field field = newPool.getClass().getDeclaredField("strategy");
                field.setAccessible(true);
                Object strategy = field.get(newPool);
                if (strategy instanceof SizeConfigStrategy) {
                    Object newStrategy = getDefaultStrategy();
                    if (newStrategy != null) {
                        field.set(newPool, newStrategy);
                        return newPool;
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bitmapPool;
    }

    public static boolean fixBitmapPoolStrategy(LruBitmapPool bitmapPool) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return false;
        }
        try {
            Field field = bitmapPool.getClass().getDeclaredField("strategy");
            field.setAccessible(true);
            Object strategy = field.get(bitmapPool);
            if (strategy instanceof SizeConfigStrategy) {
                Object newStrategy = getDefaultStrategy();
                if (newStrategy != null) {
                    field.set(bitmapPool, newStrategy);
                    return true;
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static Object getDefaultStrategy() {
        String clsName = SizeConfigStrategy.class.getName();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            clsName = "com.bumptech.glide.load.engine.bitmap_recycle.AttributeStrategy";
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            clsName = "com.bumptech.glide.load.engine.bitmap_recycle.AttributeStrategy";
        }

        try {
            Class<?> clazz = Class.forName(clsName);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);

            return constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
