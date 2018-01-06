package com.bumptech.glide.integration.framesequence;

import android.support.rastermill.FrameSequence;

import com.bumptech.glide.load.engine.Resource;

/**
 * @author liuchun
 */
public class FrameSequenceResource implements Resource<FrameSequence> {
    private final FrameSequence frameSequence;


    public FrameSequenceResource(FrameSequence fs) {
        frameSequence = fs;
    }

    @Override
    public Class<FrameSequence> getResourceClass() {
        return FrameSequence.class;
    }

    @Override
    public FrameSequence get() {
        return frameSequence;
    }

    @Override
    public int getSize() {
        return frameSequence.getWidth() * frameSequence.getHeight() * 4;
    }

    @Override
    public void recycle() {

    }
}
