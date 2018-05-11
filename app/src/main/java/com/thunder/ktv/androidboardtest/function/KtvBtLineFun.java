package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

/**
 * Created by chengkai on 18-5-9.
 */

public class KtvBtLineFun extends ButtonListFun implements IBindFrontPanel{
    private final String[] KtvBtLineName = {"KTV模式","蓝牙模式","线路输入模式"};

    private static final byte [] KTVCode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x01, (byte) 0xfe};
    private static final byte [] BTCode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x02, (byte) 0xfe};
    private static final byte [] LINECode =    {(byte) 0xEF, (byte) 0xc6, (byte) 0x03, (byte) 0xfe};

    private static final byte [] KTVCBCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x0a, (byte) 0xfe};
    private static final byte [] BTCBCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x0b, (byte) 0xfe};
    private static final byte [] LINECBCode =    {(byte) 0xEF, (byte) 0xc8, (byte) 0x0c, (byte) 0xfe};

    private ButtonBase ktvButtonBase = new ButtonBase(KTVCode,KTVCBCode,"KTV模式",true) {
        @Override
        public boolean doAction(Object o) {
            showInfo = "当前模式:" + name;
            setGPIO(gpioA,0);
            setGPIO(gpioB,0);
            AppHelper.showMsg( name + ":切换成功");
            if(needCallBackCode){
                FrontPanelFun.frontPanelWriteCode(callBackCode);
            }
            return false;
        }
    };

    private ButtonBase btButtonBase = new ButtonBase(BTCode,BTCBCode,"蓝牙模式",true) {
        @Override
        public boolean doAction(Object o) {
            showInfo = "当前模式:" + name;
            setGPIO(gpioA,1);
            setGPIO(gpioB,0);
            AppHelper.showMsg( name + ":切换成功");
            if(needCallBackCode){
                FrontPanelFun.frontPanelWriteCode(callBackCode);
            }
            return false;
        }
    };

    private ButtonBase lineButtonBase = new ButtonBase(LINECode,LINECBCode,"线路输入模式",true) {
        @Override
        public boolean doAction(Object o) {
            showInfo = "当前模式:" + name;
            setGPIO(gpioA,0);
            setGPIO(gpioB,1);
            AppHelper.showMsg( name + ":切换成功");
            if(needCallBackCode){
                FrontPanelFun.frontPanelWriteCode(callBackCode);
            }
            return false;
        }
    };
    private int gpioA = 2 * 8 + 3;
    private int gpioB = 2 * 8 + 4;

    private void setGPIO(int gpio,int vol){
        int ret = TDHardwareHelper.nativeSetGPIO(gpio,vol);
        String msg = String.format("设置GPIO %d_%d : vol %d, ret %d",gpio/8,gpio%8,vol,ret);
        AppHelper.showMsg(msg);
    }
    public KtvBtLineFun() {
        super(FUN_TYPE_KTV_BT_LINE, "效果切换");
        buttonBaseList.add(ktvButtonBase);
        buttonBaseList.add(btButtonBase);
        buttonBaseList.add(lineButtonBase);
    }
    String showInfo = null;
    @Override
    public String getShowInfo() {
        return showInfo;
    }

    @Override
    public boolean doAction(Object o) {
        return ButtonListFun.GetDoActionByFrontCodeButtonList(buttonBaseList, (byte[]) o).doAction(null);
    }
}
