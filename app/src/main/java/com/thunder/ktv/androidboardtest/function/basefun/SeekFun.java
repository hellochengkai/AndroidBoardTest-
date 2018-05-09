package com.thunder.ktv.androidboardtest.function.basefun;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

/**
 * Created by chengkai on 18-2-13.
 */

abstract public class SeekFun extends AbsFunction {
    public byte def = 0;
    public int cur;
    public byte max = 100;
    public SeekFun(int funType,String showName, byte[] command,byte max, byte def) {
        super(funType,MyListViewAdapter.ItemViewTypeSeekBar,showName,command);
        this.def = def;
        this.cur = def;
        this.max = max;
    }
    int setup = 10;
    public void up()
    {
        cur+=setup;
        if(cur > max){
            cur = max;
        }
        doAction(cur);
        AppHelper.getMainActivity().upDataView();
    }
    public void down()
    {
        cur-=setup;
        if(cur < 0){
            cur = 0;
        }
        doAction(cur);
        AppHelper.getMainActivity().upDataView();
    }
}
