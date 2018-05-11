package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.PT2033Helper;
import com.thunder.ktv.androidboardtest.function.basefun.SwitchListFun;

/**
 * Created by chengkai on 18-5-11.
 */

public class BassSwitchFun extends SwitchListFun implements IBindFrontPanel{

    private static final byte [] changeBassCode =    {(byte) 0xEF, (byte) 0xc5, (byte) 0x05, (byte) 0xfe};

    private static final byte [] openBassCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x06, (byte) 0xfe};
    private static final byte [] closeBassCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x07, (byte) 0xfe};

    int cur = 0;
    String msg = null;
    int[] codeTable = {-14,-12,-10,-8,-6,-4,-2,0,14,12,10,8,6,4,2,0};
    private void openBass()
    {
        cur = PT2033Helper.getInstance().openBass() & 0x0f;
        msg = ":vol = " + codeTable[cur]+ ": [ " + PT2033Helper.codeToBinaryStr(cur | 0x60) + " ]";
        AppHelper.showMsg("设置重低音:" + msg);
    }
    private void closeBass()
    {
        cur = PT2033Helper.getInstance().closeBass() & 0x0f;
        msg = ":vol = " + codeTable[cur]+ ": [ " + PT2033Helper.codeToBinaryStr(cur | 0x60) + " ]";
        AppHelper.showMsg("设置重低音:" + msg);
    }

    public BassSwitchFun() {
        super(FUN_TYPE_Bass, "设置重低音:");
        switchBaseList.add(new SwitchBase() {
            @Override
            public byte[] getCode() {
                return changeBassCode;
            }

            @Override
            public byte[] getCbCode() {
                if(isChecked){
                    return openBassCode;
                }else {
                    return closeBassCode;
                }
            }
            @Override
            public boolean doAction(Object o) {
                isChecked = (boolean) o;
                isChecked =!isChecked;
                if(isChecked){
                    openBass();
                }else{
                    closeBass();
                }
                return false;
            }

            @Override
            public String getName() {
                return "重低音";
            }
        });
    }
}
