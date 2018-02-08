/****************************************************************************
Copyright (c) 2010 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
#include "JniHelper.h"
#include <android/log.h>
#include <string.h>
#include <pthread.h>

#if 1
#define  LOG_TAG    "JniHelper"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#else
#define  LOGD(...) 
#endif

#define JAVAVM    JniHelper::getJavaVM()

using namespace std;

extern "C"
{

    //////////////////////////////////////////////////////////////////////////
    // java vm helper function
    //////////////////////////////////////////////////////////////////////////

    static pthread_key_t s_threadKey;

    static void detach_current_thread (void *env) {
        JAVAVM->DetachCurrentThread();
    }



    static bool getEnv(JNIEnv **env)
    {
        bool bRet = false;

        switch(JAVAVM->GetEnv((void**)env, JNI_VERSION_1_4))
        {
        case JNI_OK:
            bRet = true;
            break;
        case JNI_EDETACHED:
            pthread_key_create (&s_threadKey, detach_current_thread);
            if (JAVAVM->AttachCurrentThread(env, 0) < 0)
            {
                LOGD("Failed to get the environment using AttachCurrentThread()");
                break;
            }
            if (pthread_getspecific(s_threadKey) == NULL)
                pthread_setspecific(s_threadKey, env);
            bRet = true;
            break;
        default:
            LOGD("Failed to get the environment using GetEnv()");
            break;
        }      

        return bRet;
    }

    static jclass getClassID_(const char *className, JNIEnv *env)
    {
        JNIEnv *pEnv = env;
        jclass ret = 0;

        do 
        {
            if (! pEnv)
            {
                if (! getEnv(&pEnv))
                {
                    break;
                }
            }
            
            ret = pEnv->FindClass(className);
            if (! ret)
            {
                 LOGD("Failed to find class of %s", className);
                break;
            }
        } while (0);

        return ret;
    }

    static bool getStaticMethodInfo_(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
    {
        jmethodID methodID = 0;
        JNIEnv *pEnv = 0;
        bool bRet = false;

        do 
        {
            if (! getEnv(&pEnv))
            {
                break;
            }

            jclass classID = getClassID_(className, pEnv);

            methodID = pEnv->GetStaticMethodID(classID, methodName, paramCode);
            if (! methodID)
            {
                LOGD("Failed to find static method id of %s", methodName);
                break;
            }

            methodinfo.classID = classID;
            methodinfo.env = pEnv;
            methodinfo.methodID = methodID;

            bRet = true;
        } while (0);

        return bRet;
    }

    static bool getMethodInfo_(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
    {
        jmethodID methodID = 0;
        JNIEnv *pEnv = 0;
        bool bRet = false;

        do 
        {
            if (! getEnv(&pEnv))
            {
                break;
            }

            jclass classID = getClassID_(className, pEnv);

            methodID = pEnv->GetMethodID(classID, methodName, paramCode);
            if (! methodID)
            {
                LOGD("Failed to find method id of %s", methodName);
                break;
            }

            methodinfo.classID = classID;
            methodinfo.env = pEnv;
            methodinfo.methodID = methodID;

            bRet = true;
        } while (0);

        return bRet;
    }

    static string jstring2string_(jstring jstr)
    {
        if (jstr == NULL)
        {
            return "";
        }
        
        JNIEnv *env = 0;

        if (! getEnv(&env))
        {
            return 0;
        }

        const char* chars = env->GetStringUTFChars(jstr, NULL);
        string ret(chars);
        env->ReleaseStringUTFChars(jstr, chars);

        return ret;
    }

    static char* getCStringFromJstring(JNIEnv *env, jstring jstr)
    {
        if (jstr == NULL)
        {
            return NULL;
        }

        const char* chars = env->GetStringUTFChars(jstr, NULL);
        string ret(chars);
        env->ReleaseStringUTFChars(jstr, chars);

        char *szStr = (char*)malloc(ret.length() + 1);
        strcpy(szStr, ret.c_str());
        return szStr;
    }

    static char* getCStringFromByteArray(JNIEnv *env, jbyteArray array) {
        if(!array) {
            LOGD("array is NULL!!");
            return NULL;
        }
        jint strLength = env->GetArrayLength(array);
        char *szString = NULL;
        if(strLength > 0) {
            const char *szTmp = (char*)env->GetByteArrayElements(array, JNI_FALSE);
            LOGD("strlength = %d, strlen(szTmp) = %d", strLength, strlen(szTmp));
            szString = (char*)calloc(1, strLength + 1);
            memcpy(szString, szTmp, strLength);
            env->ReleaseByteArrayElements(array, (jbyte*)szTmp, 0);
        }
        return szString;
    }

    static void memcpyArray(JNIEnv *env, char *pDst, jbyteArray array, jint nMaxSize) {
        if(!array) {
            LOGD("array is NULL!!");
            return;
        }
        const char *szString = (const char*)env->GetByteArrayElements(array, JNI_FALSE);
        jint nLength = env->GetArrayLength(array);
        if(nMaxSize > 0) {
            nLength = nLength > nMaxSize ? nMaxSize : nLength;
        }
        memcpy(pDst, szString, nLength);
        env->ReleaseByteArrayElements(array, (jbyte*)szString, 0);
    }

    static void memcpyToArray(JNIEnv *env, jbyteArray array, char *pSrc, jint nMaxSize) {
        if(!array) {
            LOGD("array is NULL!!");
            return;
        }
        char *szString = (char*)env->GetByteArrayElements(array, JNI_FALSE);
        jint nLength = env->GetArrayLength(array);
        if(nMaxSize > 0) {
            nLength = nLength > nMaxSize ? nMaxSize : nLength;
        }
        memcpy(szString, pSrc, nLength);
        LOGD("nLength %d, string:%s", nLength, szString);
        env->ReleaseByteArrayElements(array, (jbyte*)szString, 0);
    }
}

static pthread_key_t g_key;

void _detachCurrentThread(void* a) {
    JniHelper::getJavaVM()->DetachCurrentThread();
}

JavaVM* JniHelper::_psJavaVM = nullptr;
//jmethodID JniHelper::loadclassMethod_methodID = nullptr;
//jobject JniHelper::classloader = nullptr;
//jobject JniHelper::_activity = nullptr;
//std::unordered_map<JNIEnv*, std::vector<jobject>> JniHelper::localRefs;

JavaVM* JniHelper::getJavaVM() {
    pthread_t thisthread = pthread_self();
    LOGD("JniHelper::getJavaVM(), pthread_self() = %ld", thisthread);
    return _psJavaVM;
}

void JniHelper::setJavaVM(JavaVM *javaVM) {
    pthread_t thisthread = pthread_self();
    LOGD("JniHelper::setJavaVM(%p), pthread_self() = %ld", javaVM, thisthread);
    _psJavaVM = javaVM;
    pthread_key_create(&g_key, _detachCurrentThread);
}

JNIEnv* JniHelper::cacheEnv(JavaVM* jvm) {
    JNIEnv* _env = nullptr;
    // get jni environment
    jint ret = jvm->GetEnv((void**)&_env, JNI_VERSION_1_4);
    switch (ret) {
        case JNI_OK :
            // Success!
            pthread_setspecific(g_key, _env);
            return _env;
        case JNI_EDETACHED :
            // Thread not attached
            if (jvm->AttachCurrentThread(&_env, nullptr) < 0)
            {
            LOGE("Failed to get the environment using AttachCurrentThread()");
            return nullptr;
        } else {
            // Success : Attached and obtained JNIEnv!
            pthread_setspecific(g_key, _env);
            return _env;
        }
        case JNI_EVERSION :
            // Cannot recover from this error
            LOGE("JNI interface version 1.4 not supported");
        default :
            LOGE("Failed to get the environment using GetEnv()");
            return nullptr;
    }
}

//JNIEnv* JniHelper::getEnv()
JNIEnv* JniHelper::getJNIEnv() {
    JNIEnv *_env = (JNIEnv *)pthread_getspecific(g_key);
    if (_env == nullptr)
        _env = JniHelper::cacheEnv(_psJavaVM);
    return _env;
}


/*
JavaVM* JniHelper::m_psJavaVM = NULL;

JavaVM* JniHelper::getJavaVM()
{
    return m_psJavaVM;
}

void JniHelper::setJavaVM(JavaVM *javaVM)
{
    m_psJavaVM = javaVM;
    LOGD("setJavaVM %p", m_psJavaVM);
}*/

jclass JniHelper::getClassID(const char *className, JNIEnv *env)
{
    return getClassID_(className, env);
}

bool JniHelper::getStaticMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
{
    return getStaticMethodInfo_(methodinfo, className, methodName, paramCode);
}

bool JniHelper::getMethodInfo(JniMethodInfo &methodinfo, const char *className, const char *methodName, const char *paramCode)
{
    return getMethodInfo_(methodinfo, className, methodName, paramCode);
}

/*
JNIEnv* JniHelper::getJNIEnv()
{
    JNIEnv *env = NULL;
    bool ret = getEnv(&env);
    if(ret)
    {
        return env;
    }
    return NULL;
}*/

string JniHelper::jstring2string(jstring str)
{
    return jstring2string_(str);
}

char* JniHelper::getCString(jbyteArray array)
{
    return getCStringFromByteArray(getJNIEnv(), array);
}

char* JniHelper::getCString(JNIEnv *env, jstring string)
{
    return getCStringFromJstring(env, string);
}

char* JniHelper::getCString(JNIEnv *env, jbyteArray array)
{
    return getCStringFromByteArray(env, array);
}

void JniHelper::memcpy(JNIEnv *env, char *pDst, jbyteArray array, jint nMaxSize)
{
    memcpyArray(env, pDst, array, nMaxSize);
}

void JniHelper::memcpy(char *pDst, jbyteArray array, jint nMaxSize)
{
    memcpyArray(getJNIEnv(), pDst, array, nMaxSize);
}

void JniHelper::memcpy(JNIEnv *env, jbyteArray array, char *pSrc, jint nMaxSize)
{
    memcpyToArray(env, array, pSrc, nMaxSize);
}
