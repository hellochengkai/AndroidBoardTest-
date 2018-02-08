APP_PLATFORM := android-19
APP_STL := gnustl_static
APP_CPPFLAGS := -std=c++11 -frtti -fexceptions -O2 -mfpu=vfpv3-d16 -mfloat-abi=softfp
#APP_ABI := all
APP_ABI := armeabi-v7a armeabi
APP_OPTIM := debug
LOCAL_ARM_NEON := true