//
// Created by 成凯 on 2017/7/21.
//
#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <strings.h>
#include <termios.h>
#include <pthread.h>
#include <fcntl.h>
#include <cstring>
#include "tools/JniHelper.h"
#include "jni_log.h"
#ifdef __cplusplus
extern "C" {
#endif

#include "adaptor.h"

int open_port(char * dev) //////////////打开串口
{
    if(dev == NULL)
        return -1;
    int fd =open(dev, O_RDWR|O_NOCTTY|O_NDELAY);
    isatty(STDIN_FILENO);
    return fd;
}

int set_opt(int fd ,int rate)  /////设置串口
{
    if(fd < 0)
        return -1;
    struct termios newtio;

    bzero (&newtio ,sizeof( newtio ) );
    newtio.c_cflag |= CLOCAL | CREAD;
    newtio.c_cflag &= ~CSIZE;
    newtio.c_cflag |= CS8;
    newtio.c_cflag &= ~PARENB;
    speed_t s = B9600;
    switch (rate){
        case 300:{
            s = B300;
            break;
        }
        case 600:{
            s = B600;
            break;
        }
        case 1200:{
            s = B1200;
            break;
        }
        case 1800:{
            s = B1800;
            break;
        }
        case 2400:{
            s = B2400;
            break;
        }
        case 4800:{
            s = B4800;
            break;
        }
        case 9600:{
            s = B9600;
            break;
        }
        case 19200:{
            s = B19200;
            break;
        }
        case 38400:{
            s = B38400;
            break;
        }
    }
    LOGD(" rate == %d", rate );
    cfsetispeed(&newtio, s);
    cfsetospeed(&newtio, s);
    newtio.c_cflag &= ~CSTOPB;

    //  newtio.c_cc[VTIME] = 0;
    // newtio.c_cc[VMIN] = 0;

    tcflush(fd,TCIFLUSH);

    if(tcsetattr(fd,TCSANOW,&newtio) != 0) {
        perror(" the com set error ");
        return -1;
    }

    return 1;
}

int uartRead(int fd,char * rdata,int len)////////串口接收数据
{
    int ret = 0;
    fd_set read_set;
    struct timeval tv;

    tv.tv_sec = 0;
    tv.tv_usec = 1000 * 10;
    FD_ZERO (&read_set);
    FD_SET (fd, &read_set);

    if(select(fd+1, &read_set, 0, 0, &tv) > 0) {
        ret = read(fd, rdata, len);
    }
    return ret;
}

#define JbyteArrayToChar(pjstring,pchar) if(pjstring) { pchar = JniHelper::getCString(env, pjstring); }
#define  FREEOBJ(obj) if(obj){free(obj);obj = NULL;}

JNIEXPORT jint JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeOpenUart(JNIEnv *env, jclass jclazz,jbyteArray dev,jint rate)
{
    char *cdev = NULL;
    JbyteArrayToChar(dev, cdev);
    int fd = open_port(cdev);
    if(fd < 0){
        FREEOBJ(cdev);
        return -1;
    }
    if(set_opt(fd,rate) < 0)
    {
        close(fd);
        FREEOBJ(cdev);
        return -2;
    }
    FREEOBJ(cdev);
    return fd;
}
JNIEXPORT void JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeCloseUart(JNIEnv *env, jclass jclazz,jint fd)
{
    if(fd > 0)
        close(fd);
}

JNIEXPORT int JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeWriteUart(JNIEnv *env, jclass jclazz,jint fd,jbyteArray data,int len)
{
    if(fd <= 0)
        return 0;
    char *cdata = NULL;
    JbyteArrayToChar(data, cdata);
    if(cdata == NULL)
        return 0;
    for(int i = 0;i < len ;i ++){
        LOGD("nativeWriteUart [%d] 0x%x",i,cdata[i]);
    }
    int ret = write(fd,cdata,len);
    FREEOBJ(cdata);
    return ret;
}

JNIEXPORT jint JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeReadUart(JNIEnv *env, jclass jclazz,jint fd,jbyteArray data,int len)
{
    char *cbuf = NULL;
    cbuf = (char *)env->GetByteArrayElements(data, NULL);
    int ret = uartRead(fd,cbuf,len);
    env->ReleaseByteArrayElements(data, (jbyte *) cbuf, 0);
    return ret;
}

void Debug(char *file, char *function, int line, char * message)
{
    int size = strlen(message) + 1024;
    char *msg = (char*)malloc(size);
    memset(msg, 0, size);
    char *tmpFile = strrchr(file, '/');
    if(tmpFile == NULL) {
        tmpFile = strrchr(file, '\\');
    }
    if(tmpFile) {
        file = tmpFile + 1;
    }
    sprintf(msg,"<file:%s--%s> %s <line:%d> <thread:%u>", file, function, message, line, gettid());
    char *pos = strrchr(msg,'\n');
    if(pos)
    {
        do{
            if(*(pos-1) == '\r')
            {
                *pos = *(pos-1) = ' ';
            }
            else
            {
                *pos = ' ';
            }
            pos = strrchr(msg,'\n');
        }while(pos);
    }
    LOGI("%s",msg);
    free(msg);
}

JNIEXPORT jint JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeSetGPIO(JNIEnv *env, jclass jclazz,jint gpio,int vol)
{
    Mid_RegsiterDEBUGCallbackFunction(Debug);
    return Mid_SetGPIO(gpio,vol);
}

JNIEXPORT void JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeI2C1Init(JNIEnv *env, jclass jclazz)
{
    Mid_InitI2C1();
}
JNIEXPORT void JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeI2C1DeInit(JNIEnv *env, jclass jclazz)
{
    Mid_DeInitI2C1();
}

JNIEXPORT jint JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeI2C1Write
        (JNIEnv *env, jclass jclazz,jbyte DevAddress,int RegAddr,int RegAddrCount,jbyteArray bytes,int len)
{
    char * cbuf = 0;
    cbuf = (char *)env->GetByteArrayElements(bytes, NULL);
    int ret =  Mid_I2CWrite((char)DevAddress,RegAddr,RegAddrCount,cbuf,len);
    env->ReleaseByteArrayElements(bytes, (jbyte *) cbuf, 0);
    LOGD("PT2033Helper Mid_I2CWrite cbuf == %d 0x%x",cbuf,ret);
    return ret;
}
JNIEXPORT jint JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeI2C1WriteOneByte
        (JNIEnv *env, jclass jclazz,jbyte DevAddress,int RegAddr,int RegAddrCount,jbyte bytes)
{
    int ret =  Mid_I2CWrite((char)DevAddress, RegAddr, RegAddrCount, (char *) &bytes, 1);
    LOGD("PT2033Helper Mid_I2CWrite cbuf == %d 0x%x",bytes,ret);
    return ret;
}

JNIEXPORT jint JNICALL Java_com_thunder_ktv_thunderjni_thunderapi_TDHardwareHelper_nativeI2C1Read
        (JNIEnv *env, jclass jclazz,jbyte DevAddress,int RegAddr,int RegAddrCount,jbyteArray bytes,int len)
{
    char * cbuf = 0;
    cbuf = (char *)env->GetByteArrayElements(bytes, NULL);
    int ret = Mid_I2CRead((char)DevAddress,RegAddr,RegAddrCount,cbuf,len);
    LOGD("PT2033Helper Mid_I2CRead cbuf == %d 0x%x",cbuf,ret);
    env->ReleaseByteArrayElements(bytes, (jbyte *) cbuf, 0);
    return ret;
}

#ifdef __cplusplus
}
#endif
