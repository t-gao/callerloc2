
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

# the .so file name, remember need lib in the front
LOCAL_MODULE    := libphone-number-jni

# the main cpp file name
LOCAL_SRC_FILES := number-jni.cpp 


LOCAL_PRELINK_MODULE := false
include $(BUILD_SHARED_LIBRARY)
