package com.thunder.ktv.androidboardtest;

import android.util.Log;

import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

/**
 * Created by chengkai on 18-5-10.
 */

public class PT2033Helper {
    private static final byte PT2033_ADDRESS = (byte) 0x88;
    private static PT2033Helper instance = null;
    private static byte[] InitCoed = {
            (byte) 0x18,//音量衰减为0dB
            (byte) 0xd0,//左声道扬声器衰减到-20dB
            (byte) 0xf0,//右声道扬声器衰减到-20dB
            (byte) 0x80,//sub声道扬声器衰减到0dB
            (byte) 0x48,//切换声道1,增益7.5dB，响度开
            (byte) 0x7f //treble 0dB
    };
    private static String[] InitCoedInfo = {
            "音量衰减为0dB",//
            "左声道扬声器衰减到-20dB",//
            "右声道扬声器衰减到-20dB",//
            "sub声道扬声器衰减到0dB",//
            "切换声道1,增益7.5dB，响度开",//
            "treble 0dB" //
    };
    private PT2033Helper()
    {
        init();
    }

    public static PT2033Helper getInstance() {
        if(instance == null){
            synchronized (PT2033Helper.class){
                if(instance == null){
                    instance = new PT2033Helper();
                }
            }
        }
        return instance;
    }

    public static String codeToBinaryStr(int a)
    {
        String str = ((a >> 7)& 0x1) + " ";
        str += ((a >> 6)& 0x1) + " ";
        str += ((a >> 5)& 0x1) + " ";
        str += ((a >> 4)& 0x1) + " ";
        str += ((a >> 3)& 0x1) + " ";
        str += ((a >> 2)& 0x1) + " ";
        str += ((a >> 1)& 0x1) + " ";
        str += ((a >> 0)& 0x1);
        return str;
    }
    String msg = null;
    private static final String TAG = "PT2033Helper";
    public void init()
    {
        int ret = TDHardwareHelper.nativeI2C1Init();
        Log.d(TAG, "init: " + ret);

        for (int i = 0; i < InitCoed.length;i++){
            msg = String.format("(0x%x)",InitCoed[i] ) + ":" + codeToBinaryStr(InitCoed[i]) + "  " +  InitCoedInfo[i];
            showMsg();
        }
        ret = TDHardwareHelper.nativeI2C1Write(PT2033_ADDRESS,0,1,InitCoed,InitCoed.length);
        msg = String.format("写入返回值 ret = (0x%x，%s)",ret,ret==0?"成功":"失败" );
        showMsg();
    }
    public void deinit()
    {
        int ret = TDHardwareHelper.nativeI2C1DeInit();
        Log.d(TAG, "deinit: " + ret);
    }

    private int writeOneByte(byte b)
    {
        int ret = TDHardwareHelper.nativeI2C1WriteOneByte(PT2033_ADDRESS,0,1,b);
        msg = String.format("0x%x",b)  + ":" + codeToBinaryStr(b) + String.format("(%s)",ret==0?"成功":"失败" );
        showMsg();
        return ret;
    }

    public void setvolume(byte b)
    {
        writeOneByte(b);
    }
    public void openBass()
    {
        AppHelper.showMsg("PT2033 设置低音:" + 0x67);
        writeOneByte((byte) 0x67);
    }
    public void closeBass()
    {
        AppHelper.showMsg("PT2033 设置低音:" + 0x6d);
        writeOneByte((byte) 0x6d);
    }
    void showMsg()
    {
        AppHelper.showMsg("PT2033 写入:" + msg);
    }
}
