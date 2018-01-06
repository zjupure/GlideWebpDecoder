# libgif version 5.1.4, https://github.com/aosp-mirror/platform_external_giflib
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_SRC_FILES := \
    src/dgif_lib.c  \
    src/gifalloc.c  \
    src/openbsd-reallocarray.c

UNUSED_SRCS := \
    src/egif_lib.c  \
    src/gif_font.c  \
    src/gif_hash.c  \
    src/quantize.c

GIF_CFLAGS := -Wall -DANDROID -DHAVE_MALLOC_H -DHAVE_PTHREAD

LOCAL_CFLAGS := $(GIF_CFLAGS)
LOCAL_C_INCLUDES += $(LOCAL_PATH)/src

LOCAL_MODULE := gif
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/src

include $(BUILD_STATIC_LIBRARY)
