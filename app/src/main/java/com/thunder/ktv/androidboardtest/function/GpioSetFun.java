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
        super("GPIO设置");
        List<SwitchBase> list = new ArrayList<>();
        list.add(new SwitchBase() {
            @Override
            public boolean doAction(boolean is) {
                int ret = 0;
                int gpio = 2 * 8 + 3;
                int vol = (boolean) is?1:0;
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
        list.add(new SwitchBase() {
            @Override
            public boolean doAction(boolean is) {
                int ret = 0;
                int gpio = 2 * 8 + 4;
                int vol = (boolean) is?1:0;
                ret = TDHardwareHelper.nativeSetGPIO(gpio,vol);
                msg = String.format("设置%s : vol %d, ret %d",showName,vol,ret);
                AppHelper.showMsg(msg);
                return true;
            }
            @Override
            public String getName() {
                return "GPIO 2-4";
            }
        });
        setSwitchBaseList(list);
    }
}
