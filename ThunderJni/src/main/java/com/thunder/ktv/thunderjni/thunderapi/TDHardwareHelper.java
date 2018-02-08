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

    private static final int StreamID = 0;
    public static final String TAG = TDHardwareHelper.class.getName();

//    private static native void NativeInit(int platformtype);
//    private static native void NativeDestroy();
//    public   static native int StartTestUart(int port);
//    private  static native boolean startRecord();
//    private  static native boolean stopRecord();
//    private  static native byte[] readRecordBuffer();
//    private  static native int getRecordRate();
//    private  static native boolean fileStreamPlay(int stream,byte[] path,int startAuidoIndex);
//    private  static native boolean fileStreamStop(int stream);
//    private  static native boolean fileStreamCheckPlayEnd(int stream);
//    private  static native void setDecodeVGAWindow(int stream,int left, int top, int right, int bottom );
//    private  static native void setDecodeTVWindow(int stream,int left, int top, int right, int bottom );
//    private  static native boolean decoderSetVolume(int stream,int volume );

    public static native int nativeOpenUart(byte[] dev,int rate);
    public static native void nativeCloseUart(int fd);
    public static native int nativeWriteUart(int fd,byte[] data,int len);
    public static native int nativeReadUart(int fd,byte[] data,int len);
}
