/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#ifndef RASTERMILL_FRAMESQUENCE_WEBP_H
#define RASTERMILL_FRAMESQUENCE_WEBP_H

#include "config.h"
#include "webp/decode.h"
#include "webp/demux.h"

#include "Stream.h"
#include "Color.h"
#include "FrameSequence.h"

// Parser for a possibly-animated WebP bitstream.
class FrameSequence_webp : public FrameSequence {
public:
    FrameSequence_webp(Stream* stream);
    virtual ~FrameSequence_webp();

    virtual int getWidth() const {
        if (!mDemux) {
            return 0;
        }
        return WebPDemuxGetI(mDemux, WEBP_FF_CANVAS_WIDTH);
    }

    virtual int getHeight() const {
        if (!mDemux) {
            return 0;
        }
        return WebPDemuxGetI(mDemux, WEBP_FF_CANVAS_HEIGHT);
    }

    virtual bool isOpaque() const {
        return !(mFormatFlags & ALPHA_FLAG);
    }

    virtual int getFrameCount() const {
        if (!mDemux) {
            return 0;
        }
        return WebPDemuxGetI(mDemux, WEBP_FF_FRAME_COUNT);
    }

    virtual int getDefaultLoopCount() const {
        return mLoopCount;
    }

    virtual jobject getRawByteBuffer() const {
        return mRawByteBuffer;
    }

    virtual FrameSequenceState* createState() const;

    WebPDemuxer* getDemuxer() const { return mDemux; }

    bool isKeyFrame(size_t frameNr) const { return mIsKeyFrame[frameNr]; }

private:
    void constructDependencyChain();

    WebPData mData;
    WebPDemuxer* mDemux;
    int mLoopCount;
    uint32_t mFormatFlags;
    // mIsKeyFrame[i] is true if ith canvas can be constructed without decoding any prior frames.
    bool* mIsKeyFrame;
    jobject mRawByteBuffer = nullptr;
};

// Produces frames of a possibly-animated WebP file for display.
class FrameSequenceState_webp : public FrameSequenceState {
public:
    FrameSequenceState_webp(const FrameSequence_webp& frameSequence);
    virtual ~FrameSequenceState_webp();

    // Returns frame's delay time in milliseconds.
    virtual long drawFrame(int frameNr,
            Color8888* outputPtr, int outputPixelStride, int previousFrameNr);

private:
    void initializeFrame(const WebPIterator& currIter, Color8888* currBuffer, int currStride,
            const WebPIterator& prevIter, const Color8888* prevBuffer, int prevStride);
    bool decodeFrame(const WebPIterator& iter, Color8888* currBuffer, int currStride,
            const WebPIterator& prevIter, const Color8888* prevBuffer, int prevStride);

    const FrameSequence_webp& mFrameSequence;
    WebPDecoderConfig mDecoderConfig;
    Color8888* mPreservedBuffer;
};

#endif //RASTERMILL_FRAMESQUENCE_WEBP_H
