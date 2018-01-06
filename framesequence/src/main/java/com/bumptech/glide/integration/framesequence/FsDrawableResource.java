package com.bumptech.glide.integration.framesequence;

import android.support.rastermill.FrameSequenceDrawable;

import com.bumptech.glide.load.resource.drawable.DrawableResource;

/**
 * author: liuchun
 * date: 2018/1/6
 */
public class FsDrawableResource extends DrawableResource<FrameSequenceDrawable> {

    public FsDrawableResource(FrameSequenceDrawable drawable) {
        super(drawable);
    }

    @Override
    public Class<FrameSequenceDrawable> getResourceClass() {
        return FrameSequenceDrawable.class;
    }


    @Override
    public int getSize() {
        return drawable.getSize();
    }

    @Override
    public void recycle() {
        drawable.stop();
        drawable.destroy();
    }
}
