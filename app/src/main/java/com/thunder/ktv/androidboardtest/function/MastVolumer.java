package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;

/**
 * Created by chengkai on 18-5-10.
 */

public class MastVolumer extends SeekFun{

    public MastVolumer() {
        super(FUN_TYPE_MAST_VOLUMER, "主音量", null, (byte) 0x3f,(byte) 0);
        setSetup(3);
    }

    @Override
    public boolean doAction(Object o) {
        cur = (int) o;
        return false;
    }

    @Override
    public String getShowInfo()
    {
        int a = (cur >> 3)& 0x07;
        int b = (cur)& 0x07;
        return String.format("设置主音量{%d%%} : %.2f dB (%d)",(int)((cur * 100 )/0x3f),a*(-10) + b*(-1.25),cur);
    }
}
