package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-5-9.
 */

public class KtvBtLineFun extends ButtonListFun {
    List<ButtonBase> list = new ArrayList();
    private final int[] KtvBtLineCode = {0x01,0x02,0x03};
    private final String[] KtvBtLineName = {"KTV模式","蓝牙模式","线路输入模式"};

    public KtvBtLineFun() {
        super(FUN_TYPE_KTV_BT_LINE, "效果切换");
        for (int i = 0; i < KtvBtLineName.length;i ++){
            int finalI = i;
            list.add(new ButtonBase() {
                @Override
                public boolean doAction() {
                    AppHelper.showMsg( getName() + ":切换效果" + getName());
                    showInfo = "当前模式:" + KtvBtLineName[finalI];
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
