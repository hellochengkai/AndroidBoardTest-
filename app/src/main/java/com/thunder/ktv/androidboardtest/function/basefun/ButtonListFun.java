package com.thunder.ktv.androidboardtest.function.basefun;

import com.thunder.ktv.androidboardtest.function.FrontPanelFun;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class ButtonListFun extends AbsFunction {

    public List<IFrontPanelDoAction> buttonBaseList = null;
    public abstract class ButtonBase implements IFrontPanelDoAction {
        public String name = null;
        public ButtonBase(String name) {
            this.name = name;
        }
    }

    public ButtonListFun(int funType,String showName) {
        super(funType,MyListViewAdapter.ItemViewTypeButton, showName, null);
        buttonBaseList = new ArrayList<>();
    }

    @Override
    public boolean doAction(Object o) {
        return FrontPanelFun.doActionByFrontCodeButtonList(buttonBaseList, (byte[]) o);
    }
}
