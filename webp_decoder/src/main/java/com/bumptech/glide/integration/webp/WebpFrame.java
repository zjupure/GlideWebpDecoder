package com.bumptech.glide.integration.webp;

import android.graphics.Bitmap;
import android.support.annotation.Keep;

/**
 * Inner model class housing metadata for each animated webp frame.
 *
 * @see <a href="https://developers.google.com/speed/webp/docs/riff_container#animation">Container Specification</a>
 *
 * @author liuchun
 */
@Keep
public class WebpFrame {
    // Access from Native
    @Keep
    private long mNativePtr;
    /**
     * XOffset, YOffset, Frame Width, Frame Height
     */
    int ix, iy, iw, ih;
    /**
     * Delay, in milliseconds, to next frame.
     */
    int delay;
    /**
     * Indicates how transparent pixels of the current frame are to be
     * blended with corresponding pixels of the previous canvas
     */
    boolean blendPreviousFrame;
    /**
     * Indicates how the current frame is to be treated after it has been
     * displayed (before rendering the next frame) on the canvas:
     */
    boolean disposeBackgroundColor;

    // Called from JNI
    WebpFrame(long nativePtr, int xOffset, int yOffset, int width, int height,
              int delay, boolean blendPreviousFrame, boolean disposeBackgroundColor) {

        this.mNativePtr = nativePtr;
        this.ix = xOffset;
        this.iy = yOffset;
        this.iw = width;
        this.ih = height;
        this.delay = delay;
        this.blendPreviousFrame = blendPreviousFrame;
        this.disposeBackgroundColor = disposeBackgroundColor;
    }

    @Override
    protected void finalize() throws Throwable {
        nativeFinalize();
    }

    public void dispose() {
        nativeDispose();
    }

    public void renderFrame(int width, int height, Bitmap bitmap) {
        nativeRenderFrame(width, height, bitmap);
    }

    public int getWidth() {
        return iw;
    }

    public int getHeight() {
        return ih;
    }

    public int getDurationMs() {
        return delay;
    }

    public int getXOffest() {
        return ix;
    }

    public int getYOffest() {
        return iy;
    }

    public boolean shouldDisposeToBackgroundColor() {
        return disposeBackgroundColor;
    }

    public boolean isBlendWithPreviousFrame() {
        return blendPreviousFrame;
    }

    private native void nativeRenderFrame(int width, int height, Bitmap bitmap);
    private native void nativeDispose();
    private native void nativeFinalize();
}
