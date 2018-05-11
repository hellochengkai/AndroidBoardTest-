package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.PT2033Helper;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;
import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;

/**
 * Created by chengkai on 18-5-10.
 */

public class BassFun  extends SeekFun{
    public BassFun() {
        super(FUN_TYPE_Bass, "重低音",null,(byte) 15,(byte) 0);
    }

    @Override
    public boolean doAction(Object o) {
        PT2033Helper.getInstance().openBass();
        return false;
    }

    int[] codeTable = {-14,-12,-10,-8,-6,-4,-2,0,14,12,10,8,6,4,2,0};
    @Override
    public String getShowInfo() {
        return getShowName() +  ":vol = " + codeTable[cur]+ ": [ " + PT2033Helper.codeToBinaryStr(cur | 0x60) + " ]";
    }
}
