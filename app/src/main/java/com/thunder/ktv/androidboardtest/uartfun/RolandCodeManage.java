package com.thunder.ktv.androidboardtest.uartfun;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chengkai on 18-2-22.
 */

public class RolandCodeManage {
    private static Map<Short,Byte> codeMap = new HashMap<>();
    private static final String TAG = "RolandCodeManage";
    public Map<Short, Byte> getCodeMap() {
        return codeMap;
    }
    static public boolean addCode(short command,byte code)
    {
        Byte curCode = codeMap.get(command);
        if(curCode == null || curCode != code){
            Log.d(TAG, String.format("addCode %04x %02x",command,code));
            codeMap.put(command,code);
            return true;
        }
        return false;
    }
}
