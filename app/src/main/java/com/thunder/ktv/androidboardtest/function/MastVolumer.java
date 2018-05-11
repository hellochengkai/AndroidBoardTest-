package com.thunder.ktv.androidboardtest.function;

import android.util.Log;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.PT2033Helper;
import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;

import java.util.logging.Handler;

/**
 * Created by chengkai on 18-5-10.
 */

public class MastVolumer extends SeekFun{
    private static final String TAG = "MastVolumer";
    byte volume = max;
    public MastVolumer() {
        super(FUN_TYPE_MAST_VOLUMER, "主音量", null, (byte) 0x3f,(byte) 0);
        setSetup(3);
        doAction(cur);
    }

    String dBstr = null;
    @Override
    public boolean doAction(Object o) {
        cur = (int) o;
        volume = (byte) (max - cur);
        PT2033Helper.getInstance().setvolume(volume);

        int a = (volume >> 3)& 0x07;
        int b = (volume)& 0x07;
        dBstr = String.format("%.2f dB",a*(-10) + b*(-1.25));
        String msg = "PT2033 设置主音量:" + dBstr;
        AppHelper.showMsg(msg);
        return false;
    }

    @Override
    public String getShowInfo()
    {
        return String.format("设置主音量{%d%%} :%s (0x%x [ %s ])",(int)((cur * 100 )/max),dBstr,volume,PT2033Helper.codeToBinaryStr(volume));
    }
}
