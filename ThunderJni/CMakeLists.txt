# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

set(JNIPATH src/main/cpp/)
#include_directories(${JNIPATH} ${JNIPATH}include ${JNIPATH}include/png ${JNIPATH}android/Callback ${JNIPATH}breakpad/src ${JNIPATH}breakpad/src/common/android/include)
include_directories(${JNIPATH} ${JNIPATH}include)

set(SRC_LIST  ${JNIPATH}android/Java_HardwareHelper.cpp
              ${JNIPATH}tools/JniHelper.cpp
        )

set(SRC_LIST_V4  ${SRC_LIST})
set(SRC_LIST_V7  ${SRC_LIST})

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             thunderjni_v4

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             ${SRC_LIST} )

add_library( # Sets the name of the library.
             thunderjni_v7

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             ${SRC_LIST} )
set(LIBPATH ${PROJECT_SOURCE_DIR}/src/main/cpp/libs/)

add_library( adaptor_7000 SHARED IMPORTED )
set_target_properties(  adaptor_7000 PROPERTIES IMPORTED_LOCATION ${LIBPATH}/libadaptor_7000.so )

add_library( adaptor_8000 SHARED IMPORTED )
set_target_properties(  adaptor_8000 PROPERTIES IMPORTED_LOCATION ${LIBPATH}/libadaptor_8000.so )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       thunderjni_v4
                       adaptor_7000
                       ${log-lib}
                       )

target_link_libraries( # Specifies the target library.
                       thunderjni_v7
                       adaptor_8000
                       ${log-lib}
                       )