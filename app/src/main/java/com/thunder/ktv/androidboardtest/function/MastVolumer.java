package com.thunder.ktv.androidboardtest.function;

import android.graphics.MaskFilter;
import android.util.Log;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.PT2033Helper;
import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * Created by chengkai on 18-5-10.
 */

public class MastVolumer extends SeekFun implements IBindFrontPanel{
    private static final String TAG = "MastVolumer";
    private static final byte [] MastVolumUPCode =  {(byte) 0xEF, (byte) 0xc4, (byte) 0x03, (byte) 0xfe};
    private static final byte [] MastVolumDownCode =  {(byte) 0xEF, (byte) 0xc4, (byte) 0x02, (byte) 0xfe};


    public MastVolumer() {
        super(FUN_TYPE_MAST_VOLUMER, "主音量", null, (byte) 0xe,(byte) 0);
        setSetup(1);
        mastVolumerInfoMap = new HashMap<>();
        mastVolumerInfoMap.put(0,new MastVolumerInfo(0x38));
        mastVolumerInfoMap.put(1,new MastVolumerInfo(0x30));
        mastVolumerInfoMap.put(2,new MastVolumerInfo(0x28));
        mastVolumerInfoMap.put(3,new MastVolumerInfo(0x20));
        mastVolumerInfoMap.put(4,new MastVolumerInfo(0x18));
        mastVolumerInfoMap.put(5,new MastVolumerInfo(0x10));
        mastVolumerInfoMap.put(6,new MastVolumerInfo(0x8));
        mastVolumerInfoMap.put(7,new MastVolumerInfo(0x7));
        mastVolumerInfoMap.put(8,new MastVolumerInfo(0x6));
        mastVolumerInfoMap.put(9,new MastVolumerInfo(0x5));
        mastVolumerInfoMap.put(10,new MastVolumerInfo(0x4));
        mastVolumerInfoMap.put(11,new MastVolumerInfo(0x3));
        mastVolumerInfoMap.put(12,new MastVolumerInfo(0x2));
        mastVolumerInfoMap.put(13,new MastVolumerInfo(0x1));
        mastVolumerInfoMap.put(14,new MastVolumerInfo(0x0));
        doAction(cur);
    }

    Map <Integer,MastVolumerInfo> mastVolumerInfoMap = null;


    class MastVolumerInfo{
        byte volume = 0;
        String dBStr = null;

        public MastVolumerInfo(int volume) {
            this.volume = (byte) volume;
            int a = (volume >> 3)& 0x07;
            int b = (volume)& 0x07;
            dBStr = String.format("%.2f dB",a*(-10) + b*(-1.25));
        }
    }

    MastVolumerInfo curMastVolumerInfo = null;

    @Override
    public boolean doAction(Object o) {
        cur = (int) o;
        curMastVolumerInfo = mastVolumerInfoMap.get(cur);
        if(curMastVolumerInfo == null){
            Log.d(TAG, "doAction: " + cur);
            return false;
        }

//        volume = (byte) (max - cur);
        PT2033Helper.getInstance().setvolume(curMastVolumerInfo.volume);

//        int a = (volume >> 3)& 0x07;
//        int b = (volume)& 0x07;
//        dBstr = String.format("%.2f dB",a*(-10) + b*(-1.25));
        String msg = "PT2033 设置主音量:" + curMastVolumerInfo.dBStr;
        AppHelper.showMsg(msg);
        return false;
    }

    @Override
    public byte[] getUpCode() {
        return MastVolumUPCode;
    }

    @Override
    public byte[] getDownCode() {
        return MastVolumDownCode;
    }
    @Override
    public String getShowInfo()
    {
        if(curMastVolumerInfo == null){
            return "设置主音量 失败";
        }
        return String.format("设置主音量{%d%%} :%s (0x%x [ %s ])",(int)((cur * 100 )/max),curMastVolumerInfo.dBStr,curMastVolumerInfo.volume,PT2033Helper.codeToBinaryStr(curMastVolumerInfo.volume));
    }
}
