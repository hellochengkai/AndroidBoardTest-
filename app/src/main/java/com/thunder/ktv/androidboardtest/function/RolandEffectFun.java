package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class RolandEffectFun extends ButtonListFun implements IBindFrontPanel {
    private static final byte [] RolandPrm1Code = {(byte) 0xEF, (byte) 0xc5, (byte) 0x01, (byte) 0xfe};
    private static final byte [] RolandPrm2Code = {(byte) 0xEF, (byte) 0xc5, (byte) 0x02, (byte) 0xfe};
    private static final byte [] RolandPrm3Code = {(byte) 0xEF, (byte) 0xc5, (byte) 0x03, (byte) 0xfe};
    private static final byte [] RolandPrm4Code = {(byte) 0xEF, (byte) 0xc5, (byte) 0x04, (byte) 0xfe};

    private static final byte [] RolandPrm1CBCode = {(byte) 0xEF, (byte) 0xc8, (byte) 0x02, (byte) 0xfe};
    private static final byte [] RolandPrm2CBCode = {(byte) 0xEF, (byte) 0xc8, (byte) 0x03, (byte) 0xfe};
    private static final byte [] RolandPrm3CBCode = {(byte) 0xEF, (byte) 0xc8, (byte) 0x04, (byte) 0xfe};
    private static final byte [] RolandPrm4CBCode = {(byte) 0xEF, (byte) 0xc8, (byte) 0x05, (byte) 0xfe};

    Object[] RolandPrmCodes = {RolandPrm1Code,RolandPrm2Code,RolandPrm3Code,RolandPrm4Code};
    Object[] RolandPrmCBCodes = {RolandPrm1CBCode,RolandPrm2CBCode,RolandPrm3CBCode,RolandPrm4CBCode};
    private String msg = null;
    public RolandEffectFun() {
        super(FUN_TYPE_ROLANDEFFECT,"效果");
        List list = new ArrayList();
        list.add(new RolandPrmFun("默认效果","/sdcard/roland/01.prm",false));
        list.add(new RolandPrmFun("效果2","/sdcard/roland/02.prm",true));
        list.add(new RolandPrmFun("效果3","/sdcard/roland/03.prm",true));
        list.add(new RolandPrmFun("效果4","/sdcard/roland/04.prm",true));
        for (int i = 0; i< list.size();i ++){
            int finalI = i;
            RolandPrmFun rolandPrmFun = (RolandPrmFun) list.get(finalI);
            buttonBaseList.add(new ButtonBase(rolandPrmFun.getShowName()) {
                @Override
                public byte[] getCode() {
                    return (byte[]) RolandPrmCodes[finalI];
                }

                @Override
                public byte[] getCbCode() {
                    return (byte [])RolandPrmCBCodes[finalI];
                }

                @Override
                public boolean doAction(Object o) {
                    boolean ret = rolandPrmFun.doAction(null);
                    msg = name + ":";
                    msg += rolandPrmFun.getShowInfo();
                    FrontPanelFun.frontPanelWriteCode(getCbCode());
                    return ret;
                }
            });
        }
    }
    public String getShowInfo() {
        return msg;
    }
    public void doDefaultEffect()
    {
        buttonBaseList.get(0).doAction(null);
    }
}
