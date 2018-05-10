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
import java.util.Iterator;
import java.util.List;

/**
 * Created by chengkai on 18-2-24.
 */

public class FrontPanelFun extends AbsFunction {
    private static final String TAG = "FrontPanelFun";
    private static final byte [] powerCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x00, (byte) 0xfe};

    private static final byte [] originalCode =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x07, (byte) 0xfe};
    private static final byte [] accompanyCode =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x08, (byte) 0xfe};

    private static final byte [] openBassCode =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x05, (byte) 0xfe};
    private static final byte [] closeBassCode =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x06, (byte) 0xfe};

    private static final byte [] RolandPrm1Code =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x01, (byte) 0xfe};
    private static final byte [] RolandPrm2Code =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x02, (byte) 0xfe};
    private static final byte [] RolandPrm3Code =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x03, (byte) 0xfe};
    private static final byte [] RolandPrm4Code =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x04, (byte) 0xfe};

    private static final byte [] KTVCode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x01, (byte) 0xfe};
    private static final byte [] BTCode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x02, (byte) 0xfe};
    private static final byte [] LINECode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x03, (byte) 0xfe};



    private static final byte [] MusicUPCode =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MusicDownCode =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x03, (byte) 0xfe};

    private static final byte [] MicUPCode =    {(byte) 0xEF, (byte) 0xc2, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MicDownCode =    {(byte) 0xEF, (byte) 0xc2, (byte) 0x03, (byte) 0xfe};

    private static final byte [] EchoUPCode =    {(byte) 0xEF, (byte) 0xc3, (byte) 0x02, (byte) 0xfe};
    private static final byte [] EchoDownCode =    {(byte) 0xEF, (byte) 0xc3, (byte) 0x03, (byte) 0xfe};

    private static final byte [] MastVolumUPCode =    {(byte) 0xEF, (byte) 0xc4, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MastVolumDownCode =    {(byte) 0xEF, (byte) 0xc4, (byte) 0x03, (byte) 0xfe};

    private static List rolandPrmCodeList = new ArrayList();
    private static List ktvBTLineCodeList = new ArrayList();
    private static List micCodeList = new ArrayList();
    private static List musicCodeList = new ArrayList();
    private static List echoCodeList = new ArrayList();
    private static List powerCodeList = new ArrayList();
    private static List videoControlCodeList = new ArrayList();
    private static List mastVolumCodeList = new ArrayList();
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

        powerCodeList.add(powerCode);

        powerCodeList.add(powerCode);
        powerCodeList.add(powerCode);

        videoControlCodeList.add(originalCode);
        videoControlCodeList.add(accompanyCode);

        mastVolumCodeList.add(MastVolumUPCode);
        mastVolumCodeList.add(MastVolumDownCode);
    }

    private class FrontPanelCode{
        boolean needRetCode = false;
        int type;
        List codeList = null;
        public FrontPanelCode(int type,List<byte[]> list,boolean needRetCode) {
            this.needRetCode = needRetCode;
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
            if(type == FUN_TYPE_DEF)
                return info;

            Iterator iterator = MyListViewAdapter.list.iterator();
            while (iterator.hasNext()){
                AbsFunction absFunction = (AbsFunction) iterator.next();
                if(type == absFunction.funType){
                    if(absFunction instanceof SeekFun){
                        SeekFun seekFun = (SeekFun) absFunction;
                            if(isUpCode(code)){
                                seekFun.up();
                                info = seekFun.getShowName() + "  UP";
                                AppHelper.getMainActivity().upDataView();
                                return info;
                            }else if(isDownCode(code)){
                                seekFun.down();
                                info = seekFun.getShowName() +  "  DOWN";
                                AppHelper.getMainActivity().upDataView();
                                return info;
                            }
                    }else if(absFunction instanceof ButtonListFun){
                        ButtonListFun listFun = (ButtonListFun) absFunction;
                            listFun.doAction(new Integer(code[2]));
//                            info = listFun.getShowInfo();
                            AppHelper.getMainActivity().upDataView();
                            return info;
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

        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_DEF,powerCodeList,true));

        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_ECHO_DELAY,echoCodeList,false));
        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_MIC,micCodeList,false));
        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_MUSIC,musicCodeList,false));
        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_MAST_VOLUMER,mastVolumCodeList,false));

        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_KTV_BT_LINE,ktvBTLineCodeList,true));

        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_ROLANDEFFECT,rolandPrmCodeList,true));

        frontPanelCodes.add(new FrontPanelCode(AbsFunction.FUN_TYPE_VIDEO_CONTROL,videoControlCodeList,true));

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
                Iterator iterator = frontPanelCodes.iterator();
                while (iterator.hasNext()){
                    FrontPanelCode frontPanelCode = (FrontPanelCode) iterator.next();
                    if(!frontPanelCode.hasCode(bytes))
                        continue;
                    if(frontPanelCode.type != FUN_TYPE_DEF){
                        String info = frontPanelCode.doAction(bytes);
                        if(info != null){
                            Log.d(TAG,info);
                            AppHelper.showMsg(info);
                        }
                    }
                    if(frontPanelCode.needRetCode){
                        TDHardwareHelper.nativeWriteUart(fd, bytes, bytes.length);
                        AppHelper.showMsg("回复码值:" + byteCode2String(bytes));
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
