package com.thunder.ktv.androidboardtest.function.basefun;

import com.thunder.ktv.androidboardtest.function.FrontPanelFun;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class SwitchListFun extends AbsFunction{

    public abstract class SwitchBase implements IFrontPanelDoAction {
        public boolean isChecked = false;
        public abstract String getName();
    }
    public List<IFrontPanelDoAction> switchBaseList  = null;
    public SwitchListFun(int funType,String showName) {
        super(funType,MyListViewAdapter.ItemViewTypeSwitch, showName, null);
        switchBaseList = new ArrayList<>();
    }

    @Override
    public boolean doAction(Object o) {
        return FrontPanelFun.doActionByFrontCodeButtonList(switchBaseList, (byte[]) o);
    }
}
