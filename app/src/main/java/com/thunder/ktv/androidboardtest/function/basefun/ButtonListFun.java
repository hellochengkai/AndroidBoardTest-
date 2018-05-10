package com.thunder.ktv.androidboardtest.function.basefun;

import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.util.Iterator;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class ButtonListFun extends AbsFunction {

    public List<ButtonBase> buttonBaseList = null;
    public interface ButtonBase{
        boolean doAction();
        String getName();
        int getCode();
    }

    public void setButtonBaseList(List<ButtonBase> buttonBaseList) {
        this.buttonBaseList = buttonBaseList;
    }

    public ButtonListFun(int funType,String showName) {
        super(funType,MyListViewAdapter.ItemViewTypeButton, showName, null);
    }

    @Override
    public boolean doAction(Object o) {
        if(buttonBaseList == null)
            return false;
        Iterator iterator = buttonBaseList.iterator();
        while (iterator.hasNext()){
            ButtonBase buttonBase = (ButtonBase) iterator.next();
            if(buttonBase.getCode() == (int)o){
                buttonBase.doAction();
            }
        }
        return false;
    }
}
