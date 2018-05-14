package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;

/**
 * Created by chengkai on 18-2-24.
 */

public class PlayerVolumeFun extends SeekFun {
    public PlayerVolumeFun() {
        super(FUN_TYPE_PLAYER_VOLUME,"视频播放器音量", null,(byte) 100,(byte) 60);
    }
    @Override
    public boolean doAction(Object o) {
        cur = (int) o;
        AppHelper.getThPlayer().setVolume(cur);
        return false;
    }
    public String getShowInfo()
    {
        return String.format("设置音量 %d",
                cur);
    }

    @Override
    public byte[] getUpCode() {
        return null;
    }

    @Override
    public byte[] getDownCode() {
        return null;
    }
}
