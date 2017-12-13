
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := glide-webp
LOCAL_SRC_FILES := \
    jni_common.cpp       \
    webp.cpp     \

CXX11_FLAGS := -std=c++11
LOCAL_CFLAGS += $(CXX11_FLAGS)
#LOCAL_CFLAGS += -DLOG_TAG=\"libglide-webp\"
LOCAL_CFLAGS += -fvisibility=hidden
#LOCAL_CFLAGS += $(FRESCO_CPP_CFLAGS)
LOCAL_EXPORT_CPPFLAGS := $(CXX11_FLAGS)
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)
LOCAL_LDLIBS := -latomic -llog -ljnigraphics
LOCAL_LDFLAGS += $(GLIDE_CPP_LDFLAGS)

LOCAL_SHARED_LIBRARIES += webp

include $(BUILD_SHARED_LIBRARY)
$(call import-module,libwebp)