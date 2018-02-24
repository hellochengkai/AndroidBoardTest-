package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

/**
 * Created by chengkai on 18-2-24.
 */

public class PlayerVolumeFun extends AbsFunction{
    public PlayerVolumeFun(String showName) {
        super(MyListViewAdapter.ItemViewTypeSeekBar,showName, null,(byte) 0,(byte) 100,(byte) 100);
    }
    int vol = 0;
    @Override
    public boolean doAction(Object o) {
        vol = (int) o;
        AppHelper.getThPlayer().setVolume(vol);
        return false;
    }
    public String getShowInfo()
    {
        return String.format("设置音量 %d",
                vol);
    }
}
