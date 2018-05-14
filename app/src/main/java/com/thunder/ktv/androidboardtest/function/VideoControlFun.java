package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;

/**
 * Created by chengkai on 18-5-10.
 */

public class VideoControlFun  extends ButtonListFun implements IBindFrontPanel{
    private static final byte [] changeAudioCode =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x07, (byte) 0xfe};
    private static final byte [] originalCBCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x08, (byte) 0xfe};
    private static final byte [] accompanyCBCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x09, (byte) 0xfe};
    public VideoControlFun() {
        super(FUN_TYPE_VIDEO_CONTROL, "视频控制");
        buttonBaseList.add(audioChangeButton);
    }

    ButtonBase audioChangeButton = new ButtonBase("切换音轨") {
        byte[] needCallBackCode = null;
        @Override
        public byte[] getCode() {
            return changeAudioCode;
        }

        @Override
        public byte[] getCbCode() {
            return needCallBackCode;
        }
        @Override
        public boolean doAction(Object o) {

            int ret = AppHelper.getThPlayer().audio_select();
            msg = name + ":audio_select == " + ret;
            AppHelper.showMsg(msg);
            if(ret == 1){
                needCallBackCode = originalCBCode;
            }else{
                needCallBackCode = accompanyCBCode;
            }
            FrontPanelFun.frontPanelWriteCode(getCbCode());
            return true;
        }
    };
    String msg = null;
    @Override
    public String getShowInfo() {
        return msg;
    }
}
