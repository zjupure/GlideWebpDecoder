
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := glide-webp
LOCAL_SRC_FILES := \
    jni_common.cpp       \
    webp.cpp     \

LOCAL_CFLAGS += -fvisibility=hidden
LOCAL_EXPORT_CPPFLAGS := $(CXX11_FLAGS)
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)
LOCAL_LDLIBS := -llog -ljnigraphics
LOCAL_LDFLAGS += $(GLIDE_CPP_LDFLAGS)

ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
    LOCAL_LDFLAGS += -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384
else ifeq ($(TARGET_ARCH_ABI), x86_64)
    LOCAL_LDFLAGS += -Wl,-z,max-page-size=16384 -Wl,-z,common-page-size=16384
endif

LOCAL_SHARED_LIBRARIES += webp

include $(BUILD_SHARED_LIBRARY)
$(call import-module,libwebp)