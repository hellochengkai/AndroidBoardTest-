package com.thunder.ktv.thunderjni.thunderapi;

import android.graphics.Rect;
import android.os.Build;

import com.thunder.ktv.thunderjni.manager.LibsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 成凯 on 2017/7/21.
 */

public class TDHardwareHelper {
    public static native int nativeOpenUart(byte[] dev,int rate);
    public static native void nativeCloseUart(int fd);
    public static native int nativeWriteUart(int fd,byte[] data,int len);
    public static native int nativeReadUart(int fd,byte[] data,int len);
    public static native int nativeSetGPIO(int gpio,int vol);

    public static native int nativeI2C1Init();
    public static native int nativeI2C1DeInit();
    public static native int nativeI2C1Write(byte DevAddress,int RegAddr,int RegAddrCount,byte[] bytes,int len);
    public static native int nativeI2C1WriteOneByte(byte DevAddress,int RegAddr,int RegAddrCount,byte bytes);

    public static native int nativeI2C1Read(byte DevAddress,int RegAddr,int RegAddrCount,byte[] bytes,int len);
}
