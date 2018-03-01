package com.thunder.ktv.androidboardtest.function;

import android.util.Log;
import android.widget.EditText;

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
    private static final String TAG = "FrontPanelFun";
    private static final byte [] powerCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x00, (byte) 0xfe};
    private static final byte [] effectCode1 =    {(byte) 0xEF, (byte) 0x90, (byte) 0x00, (byte) 0xfe};
    private static final byte [] effectCode2 =    {(byte) 0xEF, (byte) 0x90, (byte) 0x01, (byte) 0xfe};
    private static final byte [] effectCode3 =    {(byte) 0xEF, (byte) 0x90, (byte) 0x02, (byte) 0xfe};

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
            Iterator iterator = MyListViewAdapter.list.iterator();

            while (iterator.hasNext()){
                AbsFunction absFunction = (AbsFunction) iterator.next();
                if((absFunction instanceof EditorFun) == false)
                    continue;
                EditorFun editorFun = (EditorFun) absFunction;
                if(type == editorFun.codeType){
                    Log.d(TAG, "editorFun: " + editorFun.showName + type + " " + editorFun.codeType);
                    if(isUpCode(code)){
                        editorFun.up();
                    }else if(isDownCode(code)){
                        editorFun.down();
                    }
                }
            }
            return;
        }
    }

    List<FrontPanelCode> frontPanelCodes = null;
    void initCode()
    {
        frontPanelCodes = new ArrayList<>();
        frontPanelCodes.add(new FrontPanelCode(EditorFun.TYPE_ECHO,"echo", (byte) 0xc4));
        frontPanelCodes.add(new FrontPanelCode(EditorFun.TYPE_DELAY,"delay", (byte) 0xc3));
        frontPanelCodes.add(new FrontPanelCode(EditorFun.TYPE_MUSIC,"music", (byte) 0xc0));
        frontPanelCodes.add(new FrontPanelCode(EditorFun.TYPE_MIC,"mic",(byte) 0xc1));
    }
    ReadCodeRunnable readCodeRunnable = null;

    public FrontPanelFun(String showName) {
        super(MyListViewAdapter.ItemViewTypeSwitch,showName, null);
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


        void doRolandPrmFun(String name)
        {
            List list = MyListViewAdapter.list;
            if(list == null){
                return;
            }
            for (int i = 0; i < list.size();i ++){
                if(list.get(i) instanceof RolandPrmFun){
                    if(((RolandPrmFun) list.get(i)).getShowName().equals(name)){
                        ((RolandPrmFun) list.get(i)).doAction(null);
                    }
                }
            }
        }

        void readCode()
        {
            List list = MyListViewAdapter.list;
            Arrays.fill(bytes, (byte) 0);
            int ret = TDHardwareHelper.nativeReadUart(fd,bytes,bytes.length);
            if(ret > 0){
                AppHelper.showMsg("前面板获取到码值: " + byteCode2String(bytes,ret));
                Log.d(TAG,"readok " + byteCode2String(bytes,ret));
                if(Arrays.equals(bytes, powerCode)){
                    Log.d(TAG,"power up " + byteCode2String(powerCode, powerCode.length));
                    TDHardwareHelper.nativeWriteUart(fd, powerCode, powerCode.length);
                }else if(Arrays.equals(bytes, effectCode1)) {
                    doRolandPrmFun("效果1");
                }else if(Arrays.equals(bytes, effectCode2)) {
                    doRolandPrmFun("效果2");
                }else if(Arrays.equals(bytes, effectCode3)) {
                    doRolandPrmFun("效果3");
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
