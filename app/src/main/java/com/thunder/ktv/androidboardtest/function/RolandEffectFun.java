package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class RolandEffectFun extends ButtonListFun {
    private String msg = null;
    List list = new ArrayList();
    public RolandEffectFun() {
        super(FUN_TYPE_ROLANDEFFECT,"效果");
        List list1 = new ArrayList();
        list1.add(new RolandPrmFun("默认效果","/sdcard/roland/02.prm"));
        list1.add(new RolandPrmFun("效果1","/sdcard/roland/01.prm"));
        list1.add(new RolandPrmFun("效果3","/sdcard/roland/03.prm"));
        list1.add(new RolandPrmFun("效果4","/sdcard/roland/04.prm"));
        for (int i = 0; i< list1.size();i ++){
            int finalI = i;
            RolandPrmFun rolandPrmFun = (RolandPrmFun) list1.get(finalI);
            list.add(new ButtonBase() {
                @Override
                public boolean doAction() {
                    boolean ret = rolandPrmFun.doAction(null);
                    msg = getName() + ":";
                    msg += rolandPrmFun.getShowInfo();
                    return ret;
                }
                @Override
                public String getName() {
                    return rolandPrmFun.getShowName();
                }

                @Override
                public int getCode() {
                    return finalI + 1;
                }
            } );
        }
        setButtonBaseList(list);
    }
    public String getShowInfo() {
        return msg;
    }

}
