package com.thunder.ktv.androidboardtest.function.basefun;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.util.Arrays;

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

    public void setSetup(int setup) {
        this.setup = setup;
    }

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
    public abstract byte[] getUpCode();
    public abstract byte[] getDownCode();
    public void doUporDown(byte [] bytes)
    {
        if(getUpCode() == null || getDownCode() == null || bytes == null){
            return;
        }
        if(Arrays.equals(bytes,getUpCode())){
            up();
        }else if(Arrays.equals(bytes,getDownCode())){
            down();
        }
    }
}
