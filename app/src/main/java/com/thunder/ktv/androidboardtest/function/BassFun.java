package com.thunder.ktv.androidboardtest.function;

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
        return false;
    }

    int[] codeTable = {-14,-12,-10,-8,-6,-4,-2,0,14,12,10,8,6,4,2,0};


    String codeToBinaryStr(int a)
    {
        String str = ((a >> 7)& 0x1) + " ";
        str += ((a >> 6)& 0x1) + " ";
        str += ((a >> 5)& 0x1) + " ";
        str += ((a >> 4)& 0x1) + " ";
        str += ((a >> 3)& 0x1) + " ";
        str += ((a >> 2)& 0x1) + " ";
        str += ((a >> 1)& 0x1) + " ";
        str += ((a >> 0)& 0x1);
        return str;
    }

    @Override
    public String getShowInfo() {

        return getShowName() +": [ " + codeToBinaryStr(cur | 0x60) + " ] vol: "+ codeTable[cur];
    }
}
