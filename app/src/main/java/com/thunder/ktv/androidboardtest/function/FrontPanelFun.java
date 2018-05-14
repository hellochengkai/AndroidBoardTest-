package com.thunder.ktv.androidboardtest.function;

import android.util.Log;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.function.basefun.IFrontPanelDoAction;
import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chengkai on 18-2-24.
 */

public class FrontPanelFun extends AbsFunction {
    private static final String TAG = "FrontPanelFun";
    ReadCodeRunnable readCodeRunnable = null;
    public FrontPanelFun(String showName) {
        super(FUN_TYPE_DEF,MyListViewAdapter.ItemViewTypeSwitch,showName, null);
        readCodeRunnable = new ReadCodeRunnable();
        new Thread(readCodeRunnable).start();
    }
    @Override
    public boolean doAction(Object o) {
        readCodeRunnable.needRead = (boolean) o;
        return false;
    }
    static int fd = 0;
    public String getShowInfo()
    {
        return readCodeRunnable.needRead?"已使能前面板控制":"已禁止前面板控制";
    }

    private class ReadCodeRunnable implements Runnable {
        byte [] bytes = new byte[4];
        boolean needRead = true;
        void readCode()
        {
            Arrays.fill(bytes, (byte) 0);
            int ret = TDHardwareHelper.nativeReadUart(fd,bytes,bytes.length);
            if(ret > 0){
                AppHelper.showMsg("获取到前面板码值: " + byteCode2String(bytes,ret));
                Log.d(TAG,"read ok " + byteCode2String(bytes,ret));
                Iterator iterator = MyListViewAdapter.list.iterator();
                while (iterator.hasNext()) {
                    AbsFunction absFunction = (AbsFunction) iterator.next();
                    if(!(absFunction instanceof IBindFrontPanel)){
                        continue;
                    }
                    if(absFunction instanceof SeekFun){
                        SeekFun seekFun = (SeekFun) absFunction;
                        seekFun.doUporDown(bytes);
                        continue;
                    }
                    absFunction.doAction(bytes);
                    AppHelper.getMainActivity().upDataView();
                }
            }else{
//              Log.d(TAG,"read time out");
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
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
    public static void frontPanelWriteCode(byte[] bytes)
    {
        if(bytes == null)
            return;
        int ret = TDHardwareHelper.nativeWriteUart(fd, bytes, bytes.length);
        ret |= TDHardwareHelper.nativeWriteUart(fd, bytes, bytes.length);
        AppHelper.showMsg("回复前面板码值:" + byteCode2String(bytes) + " X2 ret = " + ret);
    }

    static public boolean doActionByFrontCodeButtonList(List<IFrontPanelDoAction> IFrontPanelDoActionList, byte[] bytes) {
        if(IFrontPanelDoActionList == null)
            return false;
        if(bytes == null){
            return false;
        }
        Iterator<IFrontPanelDoAction> iterator = IFrontPanelDoActionList.iterator();
        while (iterator.hasNext()){
            IFrontPanelDoAction iFrontPanelDoAction = iterator.next();
            if(iFrontPanelDoAction.getCode() == null)
                continue;
            if(Arrays.equals(iFrontPanelDoAction.getCode(),bytes)){
                iFrontPanelDoAction.doAction(null);
                return true;
            }
        }
        return false;
    }
}
