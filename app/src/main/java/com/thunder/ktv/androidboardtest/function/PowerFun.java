package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.function.basefun.SwitchListFun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-5-11.
 */

public class PowerFun extends SwitchListFun {
    private static final byte [] openCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x00, (byte) 0xfe};
    private static final byte [] standbyCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x01, (byte) 0xfe};
    public PowerFun() {
        super(FUN_TYPE_Power, "待机开关");
        switchBaseList.add(new SwitchBase() {
            @Override
            public byte[] getCode() {
                // TODO: 18-5-14  
                return new byte[0];
            }

            @Override
            public byte[] getCbCode() {
                // TODO: 18-5-14
                return new byte[0];
            }

            @Override
            public boolean doAction(Object o) {
                if((boolean)o){
                    FrontPanelFun.frontPanelWriteCode(openCode);
                    AppHelper.showMsg("发送开机指令");
                }else{
                    FrontPanelFun.frontPanelWriteCode(standbyCode);
                    AppHelper.showMsg("发送待机指令");
                }
                return (boolean) o;
            }


            @Override
            public String getName() {
                return "待机开关";
            }
        });
    }

}
