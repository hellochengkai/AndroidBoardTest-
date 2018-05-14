package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.PT2033Helper;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;
import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

/**
 * Created by chengkai on 18-5-10.
 */

public class BassSeekFun extends SeekFun {
    public BassSeekFun() {
        super(FUN_TYPE_Bass, "重低音",null,(byte) 15,(byte) 0);
    }
    String msg = null;
    @Override
    public boolean doAction(Object o) {
        int a = (int) o;
        msg = ":vol = " + codeTable[cur]+ ": [ " + PT2033Helper.codeToBinaryStr(cur | 0x60) + " ]";
        PT2033Helper.getInstance().setBass((byte) ((byte) a | 0x60));
        AppHelper.showMsg("设置重低音:" + msg);
        return false;
    }
    public void setCur(int cur)
    {
        super.cur = cur;
    }
    int[] codeTable = {-14,-12,-10,-8,-6,-4,-2,0,14,12,10,8,6,4,2,0};
    @Override
    public String getShowInfo() {
        return getShowName() + msg;
    }

    @Override
    public byte[] getUpCode() {
        return null;
    }

    @Override
    public byte[] getDownCode() {
        return null;
    }
}
