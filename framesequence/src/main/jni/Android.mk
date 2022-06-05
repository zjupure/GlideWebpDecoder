
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := framesequence
LOCAL_SRC_FILES := \
	BitmapDecoderJNI.cpp \
	FrameSequence.cpp \
	FrameSequenceJNI.cpp \
	FrameSequence_gif.cpp \
	JNIHelpers.cpp \
	Registry.cpp \
	Stream.cpp  \

CXX11_FLAGS := -std=c++11
LOCAL_CFLAGS += $(CXX11_FLAGS)
#LOCAL_CFLAGS += -DLOG_TAG=\"libglide-webp\"
LOCAL_CFLAGS += -fvisibility=hidden
LOCAL_CFLAGS += $(GLIDE_CPP_CFLAGS)

LOCAL_EXPORT_CPPFLAGS := $(CXX11_FLAGS)
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)
LOCAL_EXPORT_C_INCLUDES += $(LOCAL_PATH)/libwebp/src
LOCAL_EXPORT_C_INCLUDES += $(LOCAL_PATH)/giflib/src

LOCAL_LDLIBS := -latomic -llog -ljnigraphics
LOCAL_LDFLAGS += $(GLIDE_CPP_LDFLAGS)

LOCAL_SHARED_LIBRARIES += gif

ifeq ($(FRAMESEQUENCE_INCLUDE_WEBP),true)
	LOCAL_SRC_FILES += FrameSequence_webp.cpp
	LOCAL_SHARED_LIBRARIES += webp
endif

include $(BUILD_SHARED_LIBRARY)

$(call import-module,giflib)

ifeq ($(FRAMESEQUENCE_INCLUDE_WEBP),true)
    $(call import-module,libwebp)
endif









