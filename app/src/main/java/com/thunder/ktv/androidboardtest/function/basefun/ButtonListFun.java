package com.thunder.ktv.androidboardtest.function.basefun;

import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class ButtonListFun extends AbsFunction {

    public List<ButtonBase> buttonBaseList = null;
    public interface ButtonBase{
        boolean doAction();
        String getName();
    }

    public void setButtonBaseList(List<ButtonBase> buttonBaseList) {
        this.buttonBaseList = buttonBaseList;
    }

    public ButtonListFun(String showName) {
        super(MyListViewAdapter.ItemViewTypeButton, showName, null);
    }

    @Override
    public boolean doAction(Object o) {
        return false;
    }
}
