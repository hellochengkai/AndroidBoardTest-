package com.thunder.ktv.androidboardtest.function.basefun;

import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class SwitchListFun extends AbsFunction{
    public interface SwitchBase{
        boolean doAction(boolean is);
        String getName();
    }
    public List<SwitchBase> switchBaseList  = null;

    public void setSwitchBaseList(List<SwitchBase> switchBaseList) {
        this.switchBaseList = switchBaseList;
    }

    public SwitchListFun(String showName) {
        super(MyListViewAdapter.ItemViewTypeSwitch, showName, null);
    }

    @Override
    public boolean doAction(Object o) {
        return false;
    }
}
