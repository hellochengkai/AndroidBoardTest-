package com.thunder.ktv.androidboardtest.function;

import android.util.Log;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.function.basefun.IFrontPanelDoAction;
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


    private static final byte [] MusicUPCode =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MusicDownCode =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x03, (byte) 0xfe};

    private static final byte [] MicUPCode =    {(byte) 0xEF, (byte) 0xc2, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MicDownCode =    {(byte) 0xEF, (byte) 0xc2, (byte) 0x03, (byte) 0xfe};




//    private static List ktvBTLineCodeList = new ArrayList();
//    private static List micCodeList = new ArrayList();
//    private static List musicCodeList = new ArrayList();
//    private static List echoCodeList = new ArrayList();
//    private static List powerCodeList = new ArrayList();
//    private static List videoControlCodeList = new ArrayList();
//    private static List mastVolumCodeList = new ArrayList();
//    private static List bassCodeList = new ArrayList();
    {
//        rolandPrmCodeList.add(RolandPrm1Code);
//        rolandPrmCodeList.add(RolandPrm2Code);
//        rolandPrmCodeList.add(RolandPrm3Code);
//        rolandPrmCodeList.add(RolandPrm4Code);
//
//        ktvBTLineCodeList.add(KTVCode);
//        ktvBTLineCodeList.add(BTCode);
//        ktvBTLineCodeList.add(LINECode);
//
//        micCodeList.add(MicUPCode);
//        micCodeList.add(MicDownCode);
//
//        musicCodeList.add(MusicUPCode);
//        musicCodeList.add(MusicDownCode);
//
//        echoCodeList.add(EchoUPCode);
//        echoCodeList.add(EchoDownCode);
//
//        powerCodeList.add(standbyCode);
//        powerCodeList.add(openCode);
//
//        videoControlCodeList.add(originalCode);
//        videoControlCodeList.add(accompanyCode);
//
//        mastVolumCodeList.add(MastVolumUPCode);
//        mastVolumCodeList.add(MastVolumDownCode);
//
//        bassCodeList.add(openBassCode);
//        bassCodeList.add(closeBassCode);
    }
//
//    private class FrontPanelCode{
//        boolean needRetCode = false;
//        int type;
//        List codeList = null;
//        public FrontPanelCode(int type,List<byte[]> list,boolean needRetCode) {
//            this.needRetCode = needRetCode;
//            codeList = list;
//            this.type = type;
//        }
//        boolean isUpCode(byte [] code)
//        {
//            if(code == null)
//                return false;
//            return code[2] == 0x02;
//        }
//        boolean isDownCode(byte [] code)
//        {
//            if(code == null)
//                return false;
//            return code[2] == 0x03;
//        }
//        boolean hasCode(byte[] bytes)
//        {
//            Iterator iterator = codeList.iterator();
//            while (iterator.hasNext()){
//                byte[] bytes1 = (byte[]) iterator.next();
//                if(Arrays.equals(bytes1,bytes)){
//                    return true;
//                }
//            }
//            return false;
//        }
//        String doAction(byte [] code)
//        {
//            String info = null;
//            if(type == FUN_TYPE_DEF)
//                return info;
//
//            Iterator iterator = MyListViewAdapter.list.iterator();
//            while (iterator.hasNext()){
//                AbsFunction absFunction = (AbsFunction) iterator.next();
//                if(type == absFunction.funType){
//                    if(absFunction instanceof SeekFun){
//                        SeekFun seekFun = (SeekFun) absFunction;
//                            if(isUpCode(code)){
//                                seekFun.up();
//                                info = seekFun.getShowName() + "  UP";
//                                AppHelper.getMainActivity().upDataView();
//                                return info;
//                            }else if(isDownCode(code)){
//                                seekFun.down();
//                                info = seekFun.getShowName() +  "  DOWN";
//                                AppHelper.getMainActivity().upDataView();
//                                return info;
//                            }else{
//                                if(type == FUN_TYPE_Bass){
//                                    if(absFunction instanceof BassSeekFun){
//                                        BassSeekFun bassFun = (BassSeekFun) absFunction;
//                                        if(Arrays.equals(code,openBassCode)){
//                                            if(bassFun.changeBass()) {
//                                                FrontPanelFun.frontPanelWriteCode(openBassCode);
//                                            }else{
//                                                FrontPanelFun.frontPanelWriteCode(closeBassCode);
//                                            }
//                                        }else if(Arrays.equals(code,closeBassCode)){
////                                            FrontPanelFun.frontPanelWriteCode(openBassCode);
//                                        }
//                                        AppHelper.getMainActivity().upDataView();
//                                    }
//                                }
//                            }
//
//                    }else if(absFunction instanceof ButtonListFun){
//                        ButtonListFun listFun = (ButtonListFun) absFunction;
//                            listFun.doAction(new Integer(code[2]));
////                            info = listFun.getShowInfo();
//                            AppHelper.getMainActivity().upDataView();
//                            return info;
//                    }else if(absFunction instanceof SwitchListFun){
//
//                    }else {
//
//                    }
//                }
//            }
//            return info;
//        }
//    }

//    List<FrontPanelCode> frontPanelCodes = null;
//    void initCode()
//    {
//        frontPanelCodes = new ArrayList<FrontPanelCode>();
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_Power,powerCodeList,false));
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_ECHO_DELAY,echoCodeList,false));
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_MIC,micCodeList,false));
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_MUSIC,musicCodeList,false));
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_MAST_VOLUMER,mastVolumCodeList,false));
//
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_KTV_BT_LINE,ktvBTLineCodeList,true));
//
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_ROLANDEFFECT,rolandPrmCodeList,true));
//
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_VIDEO_CONTROL,videoControlCodeList,true));
//        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_Bass,bassCodeList,false));
//
//    }
    ReadCodeRunnable readCodeRunnable = null;

    public FrontPanelFun(String showName) {
        super(FUN_TYPE_DEF,MyListViewAdapter.ItemViewTypeSwitch,showName, null);
//        initCode();
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
                    absFunction.doAction(bytes);
//                    IBindFrontPanel iBindFrontPanel = (IBindFrontPanel) absFunction;
//                    IFrontPanelDoAction idoAction = iBindFrontPanel.GetDoActionByFrontCode(bytes);
//                    if(idoAction == null)
//                        continue;
//                    idoAction.doAction(bytes);
                    AppHelper.getMainActivity().upDataView();
                }

//                Iterator iterator = frontPanelCodes.iterator();
//                while (iterator.hasNext()){
//                    FrontPanelCode frontPanelCode = (FrontPanelCode) iterator.next();
//                    if(!frontPanelCode.hasCode(bytes))
//                        continue;
//                    if(frontPanelCode.type != FUN_TYPE_DEF){
//                        String info = frontPanelCode.doAction(bytes);
//                        if(info != null){
//                            Log.d(TAG,info);
//                            AppHelper.showMsg(info);
//                        }
//                    }
//                    if(frontPanelCode.needRetCode){
//                        FrontPanelFun.frontPanelWriteCode(bytes);
//                    }
//                }
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
                if(iFrontPanelDoAction.getCbCode() != null){
                    frontPanelWriteCode(iFrontPanelDoAction.getCbCode());
                }
                return true;
            }
        }
        return false;
    }

}
