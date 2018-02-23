package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.SystemControlClientHelper;

/**
 * Created by chengkai on 18-2-22.
 */

public class GpioFun extends AbsFunction {
    private int gpio;
    private int vol = 0;
    public GpioFun(String showName,int gpio,int vol) {
        super(showName, null,(byte) 0,(byte) 0,(byte) 0);
        this.gpio = gpio;
        this.vol = vol;
    }
    SystemControlClientHelper systemControlClientHelper = null;
    String msg = new String();
    @Override
    public boolean doAction(Object o) {
        systemControlClientHelper = SystemControlClientHelper.getInstance();
        systemControlClientHelper.gpioDirSet(gpio,0);
//        msg = "设置 GPIO " + gpio/8 +"-" + gpio%8 + "输出" + ((vol==1)?"高":"低") + "电平";
//        AppHelper.showMsg(msg);
        int getGpio = systemControlClientHelper.gpioBitGet(gpio);
        if(getGpio == vol){
            msg = "GPIO " + gpio/8 +"-" + gpio%8 + "已经输出" + ((getGpio==1)?"高":"低") + "电平";
            AppHelper.showMsg(msg);
            return true;
        }
        systemControlClientHelper.gpioBitSet(gpio,vol);
        getGpio = systemControlClientHelper.gpioBitGet(gpio);
        if(getGpio < 0){
            msg = "读取 GPIO " + gpio/8 +"-" + gpio%8 + "失败!";
            AppHelper.showMsg(msg);
        }else{
            msg = "设置 GPIO " + gpio/8 +"-" + gpio%8 + "输出" + ((getGpio==1)?"高":"低") + "电平";
            if(getGpio != vol){
                msg += "失败!!";
            }else{
                msg += "成功!!";
            }
            AppHelper.showMsg(msg);
        }
        return false;
    }
    public String getShowInfo()
    {
        return msg;
    }
}
