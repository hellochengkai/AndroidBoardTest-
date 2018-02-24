package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.SystemControlClientHelper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

/**
 * Created by chengkai on 18-2-22.
 */

public class GpioFun extends AbsFunction {
    private int gpio;
//    private int vol = 0;
    public GpioFun(String showName,int gpio) {
        super(MyListViewAdapter.ItemViewTypeSwitch,showName, null,(byte) 0,(byte) 0,(byte) 0);
        this.gpio = gpio;
//        this.vol = vol;
    }
    String msg = new String();
    @Override
    public boolean doAction(Object o) {
        int ret = 0;
        int vol = (boolean) o?1:0;
        ret = TDHardwareHelper.nativeSetGPIO(gpio,vol);
        msg = String.format("设置%s : vol %d, ret %d",showName,vol,ret);
        AppHelper.showMsg(msg);
        return true;
    }
    public String getShowInfo()
    {
        return msg;
    }
}
