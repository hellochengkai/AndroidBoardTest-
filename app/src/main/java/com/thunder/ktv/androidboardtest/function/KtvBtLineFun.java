package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-5-9.
 */

public class KtvBtLineFun extends ButtonListFun {
    List<ButtonBase> list = new ArrayList();
    private final int[] KtvBtLineCode = {0x01,0x02,0x03};
    private final String[] KtvBtLineName = {"KTV模式","蓝牙模式","线路输入模式"};

    int gpioA = 2 * 8 + 3;
    int gpioB = 2 * 8 + 4;

    void setGPIO(int gpio,int vol){
        int ret = TDHardwareHelper.nativeSetGPIO(gpio,vol);
        String msg = String.format("设置GPIO %d_%d : vol %d, ret %d",gpio/8,gpio%8,vol,ret);
        AppHelper.showMsg(msg);
    }

    void doKTV()
    {
        setGPIO(gpioA,0);
        setGPIO(gpioB,0);
    }
    void doBT()
    {
        setGPIO(gpioA,1);
        setGPIO(gpioB,0);
    }
    void doLine()
    {
        setGPIO(gpioA,0);
        setGPIO(gpioB,1);
    }
    public KtvBtLineFun() {
        super(FUN_TYPE_KTV_BT_LINE, "效果切换");
        for (int i = 0; i < KtvBtLineName.length;i ++){
            int finalI = i;
            list.add(new ButtonBase() {
                @Override
                public boolean doAction() {
                    showInfo = "当前模式:" + KtvBtLineName[finalI];
                    switch (finalI){
                        case 0:{
                            doKTV();break;
                        }
                        case 1:{
                            doBT();break;
                        }
                        case 2:{
                            doLine();break;
                        }
                    }
                    AppHelper.showMsg( getName() + ":切换成功");
                    return true;
                }
                @Override
                public String getName() {
                    return KtvBtLineName[finalI];
                }

                @Override
                public int getCode() {
                    return KtvBtLineCode[finalI];
                }
            });
        }
        setButtonBaseList(list);
    }
    String showInfo = null;
    @Override
    public String getShowInfo() {
        return showInfo;
    }
}
