package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.SwitchListFun;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class GpioSetFun extends SwitchListFun {

    String msg = new String();
    public GpioSetFun() {
        super(FUN_TYPE_DEF,"GPIO设置");
        switchBaseList.add(new SwitchBase() {
            @Override
            public byte[] getCode() {
                return new byte[0];
            }

            @Override
            public byte[] getCbCode() {
                return new byte[0];
            }

            @Override
            public boolean doAction(Object o) {
                int ret = 0;
                int gpio = 2 * 8 + 3;
                int vol = (boolean) o?1:0;
                ret = TDHardwareHelper.nativeSetGPIO(gpio,vol);
                msg = String.format("设置%s : vol %d, ret %d",showName,vol,ret);
                AppHelper.showMsg(msg);
                return true;
            }

            @Override
            public String getName() {
                return "GPIO 2-3";
            }
        });
        switchBaseList.add(new SwitchBase() {
            @Override
            public byte[] getCode() {
                return new byte[0];
            }

            @Override
            public byte[] getCbCode() {
                return new byte[0];
            }

            @Override
            public boolean doAction(Object o) {
                int ret = 0;
                int gpio = 2 * 8 + 4;
                int vol = (boolean) o?1:0;
                ret = TDHardwareHelper.nativeSetGPIO(gpio,vol);
                msg = String.format("设置%s : vol %d, ret %d",showName,vol,ret);
                AppHelper.showMsg(msg);
                return true;
            }

            public String getName() {
                return "GPIO 2-4";
            }
        });
    }
}
