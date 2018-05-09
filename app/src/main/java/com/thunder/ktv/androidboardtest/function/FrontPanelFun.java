package com.thunder.ktv.androidboardtest.function;

import android.util.Log;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;
import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;
import com.thunder.ktv.androidboardtest.function.basefun.SwitchListFun;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chengkai on 18-2-24.
 */

public class FrontPanelFun extends AbsFunction {
    private static final String TAG = "FrontPanelFun";
    private static final byte [] powerCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x00, (byte) 0xfe};
//    private static final byte [] effectCode1 =    {(byte) 0xEF, (byte) 0x90, (byte) 0x00, (byte) 0xfe};
//    private static final byte [] effectCode2 =    {(byte) 0xEF, (byte) 0x90, (byte) 0x01, (byte) 0xfe};
//    private static final byte [] effectCode3 =    {(byte) 0xEF, (byte) 0x90, (byte) 0x02, (byte) 0xfe};
//    private static final byte [] effectCodeup =    {(byte) 0xEF, (byte) 0xc3, (byte) 0x02, (byte) 0xfe};
//    private static final byte [] effectCodeDowm =    {(byte) 0xEF, (byte) 0xc3, (byte) 0x03, (byte) 0xfe};


    private static final byte [] RolandPrm1Code =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x01, (byte) 0xfe};
    private static final byte [] RolandPrm2Code =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x02, (byte) 0xfe};
    private static final byte [] RolandPrm3Code =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x03, (byte) 0xfe};
    private static final byte [] RolandPrm4Code =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x04, (byte) 0xfe};

    private static final byte [] KTVCode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x01, (byte) 0xfe};
    private static final byte [] BTCode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x02, (byte) 0xfe};
    private static final byte [] LINECode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x03, (byte) 0xfe};

    private static final byte [] EchoUPCode =    {(byte) 0xEF, (byte) 0xc3, (byte) 0x02, (byte) 0xfe};
    private static final byte [] EchoDownCode =    {(byte) 0xEF, (byte) 0xc3, (byte) 0x03, (byte) 0xfe};

    private static final byte [] MicUPCode =    {(byte) 0xEF, (byte) 0xc2, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MicDownCode =    {(byte) 0xEF, (byte) 0xc2, (byte) 0x03, (byte) 0xfe};

    private static final byte [] MusicUPCode =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MusicDownCode =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x03, (byte) 0xfe};

    private static List rolandPrmCodeList = new ArrayList();
    private static List ktvBTLineCodeList = new ArrayList();
    private static List micCodeList = new ArrayList();
    private static List musicCodeList = new ArrayList();
    private static List echoCodeList = new ArrayList();
    {
        rolandPrmCodeList.add(RolandPrm1Code);
        rolandPrmCodeList.add(RolandPrm2Code);
        rolandPrmCodeList.add(RolandPrm3Code);
        rolandPrmCodeList.add(RolandPrm4Code);

        ktvBTLineCodeList.add(KTVCode);
        ktvBTLineCodeList.add(BTCode);
        ktvBTLineCodeList.add(LINECode);

        micCodeList.add(MicUPCode);
        micCodeList.add(MicDownCode);

        musicCodeList.add(MusicUPCode);
        musicCodeList.add(MusicDownCode);

        echoCodeList.add(EchoUPCode);
        echoCodeList.add(EchoDownCode);
    }



//    private static Map<String,AbsFunction> absFunctionMap = new HashMap();
//
//    {
//        absFunctionMap.put(byteCode2String(RolandPrm1Code, RolandPrm1Code.length), new RolandPrmFun("默认效果","/sdcard/roland/02.prm"));
//        absFunctionMap.put(byteCode2String(RolandPrm2Code, RolandPrm2Code.length), new RolandPrmFun("效果1","/sdcard/roland/01.prm"));
//        absFunctionMap.put(byteCode2String(RolandPrm3Code, RolandPrm3Code.length), new RolandPrmFun("效果2","/sdcard/roland/03.prm"));
//        absFunctionMap.put(byteCode2String(RolandPrm4Code, RolandPrm4Code.length), new RolandPrmFun("效果3","/sdcard/roland/04.prm"));
//    }

    private class FrontPanelCode{
        int type;
        List codeList = null;
        public FrontPanelCode(int type,List<byte[]> list) {
            codeList = list;
            this.type = type;
        }
        boolean isUpCode(byte [] code)
        {
            if(code == null)
                return false;
            return code[2] == 0x02;
        }
        boolean isDownCode(byte [] code)
        {
            if(code == null)
                return false;
            return code[2] == 0x03;
        }
        boolean hasCode(byte[] bytes)
        {
            Iterator iterator = codeList.iterator();
            while (iterator.hasNext()){
                byte[] bytes1 = (byte[]) iterator.next();
                if(Arrays.equals(bytes1,bytes)){
                    return true;
                }
            }
            return false;
        }
        String doAction(byte [] code)
        {
            String info = null;
            if(codeList == null)
                return info;
            Iterator iterator = MyListViewAdapter.list.iterator();
            while (iterator.hasNext()){
                AbsFunction absFunction = (AbsFunction) iterator.next();
                if(type == absFunction.funType){
                    if(absFunction instanceof SeekFun){
                        SeekFun seekFun = (SeekFun) absFunction;
                        if(hasCode(code)){
                            if(isUpCode(code)){
                                seekFun.up();
                                info = seekFun.getShowName() + "UP";
                                AppHelper.getMainActivity().upDataView();
                                return info;
                            }else if(isDownCode(code)){
                                seekFun.down();
                                info = seekFun.getShowName() +  "DOWN";
                                AppHelper.getMainActivity().upDataView();
                                return info;
                            }
                        }
                    }else if(absFunction instanceof ButtonListFun){
                        ButtonListFun listFun = (ButtonListFun) absFunction;
                        if(hasCode(code)){
                            listFun.doAction(new Integer(code[2]));
                            info = listFun.getShowName() +  "Click " + code[2];
                            AppHelper.getMainActivity().upDataView();
                            return info;
                        }
                    }else if(absFunction instanceof SwitchListFun){

                    }else {

                    }
                }
            }
            return info;
        }
    }

    List<FrontPanelCode> frontPanelCodes = null;
    void initCode()
    {
        frontPanelCodes = new ArrayList<FrontPanelCode>();

        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_ECHO_DELAY,echoCodeList));
        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_MIC,micCodeList));
        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_MUSIC,musicCodeList));

        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_KTV_BT_LINE,ktvBTLineCodeList));

        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_ROLANDEFFECT,rolandPrmCodeList));
    }
    ReadCodeRunnable readCodeRunnable = null;

    public FrontPanelFun(String showName) {
        super(FUN_TYPE_DEF,MyListViewAdapter.ItemViewTypeSwitch,showName, null);
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
        boolean needRead = true;
        int fd = 0;

        void readCode()
        {
            Arrays.fill(bytes, (byte) 0);
            int ret = TDHardwareHelper.nativeReadUart(fd,bytes,bytes.length);
            if(ret > 0){
                AppHelper.showMsg("获取到前面板码值: " + byteCode2String(bytes,ret));
                Log.d(TAG,"read ok " + byteCode2String(bytes,ret));
                if(Arrays.equals(bytes, powerCode)){
                    Log.d(TAG,"power up " + byteCode2String(powerCode, powerCode.length));
                    TDHardwareHelper.nativeWriteUart(fd, powerCode, powerCode.length);
                    RolandCodeManage.clear();
                }else {
                    Iterator iterator = frontPanelCodes.iterator();
                    while (iterator.hasNext()){
                        FrontPanelCode frontPanelCode = (FrontPanelCode) iterator.next();
                        String info = frontPanelCode.doAction(bytes);
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
            return;
        }
        @Override
        public void run() {
            while (true){
                if(needRead){
                    fd = TDHardwareHelper.nativeOpenUart("/dev/ttyAMA2".getBytes(),4800);
                    while (needRead){
                        readCode();
//                        Arrays.fill(bytes, (byte) 0);
//                        int ret = TDHardwareHelper.nativeReadUart(fd,bytes,bytes.length);
//                        if(ret > 0){
//                            AppHelper.showMsg("获取到前面板码值: " + byteCode2String(bytes,ret));
//                            doActioByCode(bytes);
//                            TDHardwareHelper.nativeWriteUart(fd,bytes,bytes.length);
//                        }
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
