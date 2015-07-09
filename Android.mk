LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := $(call all-java-files-under, src)

# apk name
LOCAL_PACKAGE_NAME := CallHelper
LOCAL_CERTIFICATE := platform
include $(BUILD_PACKAGE)
LOCAL_JNI_SHARED_LIBRARIES := libphone-number-jni

# so can call jni/Android.mk
include $(call all-makefiles-under,$(LOCAL_PATH))
