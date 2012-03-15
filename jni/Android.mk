LOCAL_PATH      := $(call my-dir)

# libexifcomment
include $(CLEAR_VARS)
LOCAL_MODULE    := prebuild-libexifcomment
LOCAL_SRC_FILES := libexifcomment.so
include $(PREBUILT_SHARED_LIBRARY)

#
include $(CLEAR_VARS)
LOCAL_MODULE    := exif_usercomment_wrapper
LOCAL_SRC_FILES := exif_usercomment_wrapper.c
LOCAL_SHARED_LIBRARIES := prebuild-libexifcomment
TARGET_PLATFORM := android-8
include $(BUILD_SHARED_LIBRARY)
