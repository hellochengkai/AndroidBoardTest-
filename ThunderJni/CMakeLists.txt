cmake_minimum_required(VERSION 3.4.1)
set(JNIPATH src/main/cpp/)
#include_directories(${JNIPATH} ${JNIPATH}include ${JNIPATH}include/png ${JNIPATH}android/Callback ${JNIPATH}breakpad/src ${JNIPATH}breakpad/src/common/android/include)
include_directories(${JNIPATH} ${JNIPATH}include)

set(SRC_LIST  ${JNIPATH}android/Java_HardwareHelper.cpp
              ${JNIPATH}tools/JniHelper.cpp
        )
set(SRC_LIST  ${SRC_LIST})
add_library(thunderjni SHARED  ${SRC_LIST} )
set(LIBPATH ${PROJECT_SOURCE_DIR}/src/main/cpp/libs/)
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
target_link_libraries( # Specifies the target library.
                       thunderjni
                       ${log-lib}
                       )