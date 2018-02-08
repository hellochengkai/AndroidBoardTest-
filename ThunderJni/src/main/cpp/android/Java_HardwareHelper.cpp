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
#ifdef __cplusplus
}
#endif