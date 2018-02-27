package com.thunder.ktv.androidboardtest.function;

import android.util.Log;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by chengkai on 18-2-24.
 */

public class FrontPanelFun extends AbsFunction {
    private static final byte [] powerCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x00, (byte) 0xfe};
//    private static final byte [] Code3Up =      {(byte) 0xEF, (byte) 0xc3, (byte) 0x02, (byte) 0xfe};
//    private static final byte [] Code3Down =    {(byte) 0xEF, (byte) 0xc3, (byte) 0x03, (byte) 0xfe};
//    private static final byte [] Code4Up =      {(byte) 0xEF, (byte) 0xc4, (byte) 0x02, (byte) 0xfe};
//    private static final byte [] Code4Down =    {(byte) 0xEF, (byte) 0xc4, (byte) 0x03, (byte) 0xfe};
//    private static final byte [] Code0Up =      {(byte) 0xEF, (byte) 0xc0, (byte) 0x02, (byte) 0xfe};
//    private static final byte [] Code0Down =    {(byte) 0xEF, (byte) 0xc0, (byte) 0x03, (byte) 0xfe};
//    private static final byte [] Code1Up =      {(byte) 0xEF, (byte) 0xc1, (byte) 0x02, (byte) 0xfe};
//    private static final byte [] Code1Down =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x03, (byte) 0xfe};


    private class FrontPanelCode{
        String name;
        int type;
        byte [] upCode = {(byte) 0xEF, (byte) 0xc0, (byte) 0x02, (byte) 0xfe};
        byte [] downCode = {(byte) 0xEF, (byte) 0xc0, (byte) 0x03, (byte) 0xfe};
        public FrontPanelCode(int type,String name, byte code) {
            this.type = type;
            this.name = name;
            this.upCode[1] = code;
            this.downCode[1] = code;
        }
        boolean isUpCode(byte [] code)
        {
            return Arrays.equals(code, upCode);
        }
        boolean isDownCode(byte [] code)
        {
            return Arrays.equals(code, downCode);
        }
        String getInfo(byte [] code)
        {
           if(isUpCode(code)){
               return new String(name + " UP");
           }
           if (isDownCode(code)){
               return new String(name + " DOWN");
           }
           return null;
        }
        void doAction(byte [] code)
        {
            Set keySet = MyListViewAdapter.seekMap.keySet();
            Iterator iterator = keySet.iterator();
            while (iterator.hasNext()){
                MyListViewAdapter.BaseCode baseCode = MyListViewAdapter.seekMap.get(iterator.next());
                if(type == baseCode.getType()){
                    if(isUpCode(code)){
                        baseCode.up();
                    }else if(isDownCode(code)){
                        baseCode.down();
                    }
                    AppHelper.getMainActivity().upDataView();
                }
            }
            return;
        }
    }

    List<FrontPanelCode> frontPanelCodes = null;
    void initCode()
    {
        frontPanelCodes = new ArrayList<>();
        frontPanelCodes.add(new FrontPanelCode(MyListViewAdapter.BaseCode.TYPE_ECHO,"echo", (byte) 0xc4));
        frontPanelCodes.add(new FrontPanelCode(MyListViewAdapter.BaseCode.TYPE_DELAY,"delay", (byte) 0xc3));
        frontPanelCodes.add(new FrontPanelCode(MyListViewAdapter.BaseCode.TYPE_MUSIC,"music", (byte) 0xc0));
        frontPanelCodes.add(new FrontPanelCode(MyListViewAdapter.BaseCode.TYPE_MIC,"mic",(byte) 0xc1));
    }
    ReadCodeRunnable readCodeRunnable = null;
    private static final String TAG = "TestFun";
    public FrontPanelFun(String showName) {
        super(MyListViewAdapter.ItemViewTypeSwitch,showName, null,(byte) 0,(byte) 0,(byte) 0);
        initCode();
        readCodeRunnable = new ReadCodeRunnable();
        new Thread(readCodeRunnable).start();
    }
    @Override
    public boolean doAction(Object o) {
        readCodeRunnable.needRead = (boolean) o;
        return false;
    }
    public String getShowInfo()
    {
        return readCodeRunnable.needRead?"已使能前面板控制":"已禁止前面板控制";
    }

    private class ReadCodeRunnable implements Runnable {
        byte [] bytes = new byte[4];
        boolean needRead = false;
        int fd = 0;

        void readCode()
        {
            Arrays.fill(bytes, (byte) 0);
            int ret = TDHardwareHelper.nativeReadUart(fd,bytes,bytes.length);
            if(ret > 0){
                AppHelper.showMsg("前面板获取到码值: " + byteCode2String(bytes,ret));
                Log.d(TAG,"readok " + byteCode2String(bytes,ret));
                if(Arrays.equals(bytes, powerCode)){
                    Log.d(TAG,"power up " + byteCode2String(powerCode, powerCode.length));
                    TDHardwareHelper.nativeWriteUart(fd, powerCode, powerCode.length);
                }else {
                    Iterator iterator = frontPanelCodes.iterator();
                    while (iterator.hasNext()){
                        FrontPanelCode frontPanelCode = (FrontPanelCode) iterator.next();
                        frontPanelCode.doAction(bytes);
                        String info = frontPanelCode.getInfo(bytes);
                        if(info != null){
                            Log.d(TAG,info);
                            AppHelper.showMsg(info);
                        }
                    }
                }
            }else{
//              Log.d(TAG,"read time out");
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            while (true){
                if(needRead){
                    fd = TDHardwareHelper.nativeOpenUart("/dev/ttyAMA2".getBytes(),4800);
                    while (needRead){
                        readCode();
                    }
                    TDHardwareHelper.nativeCloseUart(fd);
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
