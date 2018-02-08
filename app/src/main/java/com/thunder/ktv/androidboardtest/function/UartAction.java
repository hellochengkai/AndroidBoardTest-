package com.thunder.ktv.androidboardtest.function;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by chengkai on 18-2-7.
 */

public class UartAction extends ActionBase{
    private static final String TAG = "UartAction";
    private static final String versionName = "版本号";
    private static final String micupName = "麦克加";
    private static final String micdownName = "麦克减";
    private static final String musicupName = "音量加";
    private static final String musicdownName = "音量减";
    private static final String echoupName = "回响加";
    private static final String echodownName = "回响减";

    private static final int[] version = {0xbf, 0x00, 0x00, 0x00, 0x00};
    private static final int[] micup = {0xff, 0xff, 0xff, 0xff, 0xf1};
    private static final int[] micdown = {0xff,0xff,0xff,0xff,0xf2};
    private static final int[] musicup = {0xff,0xff,0xff,0xff,0xf3};
    private static final int[] musicdown = {0xff,0xff,0xff,0xff,0xf4};
    private static final int[] echoup = {0xff,0xff,0xff,0xff,0xf5};
    private static final int[] echodown = {0xff,0xff,0xff,0xff,0xf6};

    private static String code2Str(int[] code)
    {
        if(code == null)
            return new String();
        String codeStr = null;
        UartCode uartCode = new UartCode(code);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            codeStr = objectMapper.writeValueAsString(uartCode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "code2Str: " + codeStr);
        return codeStr;
    }

    public static int[] Str2code(String string)
    {
        Log.d(TAG, "Str2code: " + string);
        ObjectMapper objectMapper = new ObjectMapper();
        UartCode uartCode = null;
        try {
            uartCode = objectMapper.readValue(string,UartCode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(uartCode == null)
            return null;
        return uartCode.getCode();
    }

    public static List<ActionBase> getDefUartAction()
    {
        String fun = UartFunction.getDefUartFun();
        List<ActionBase> list = new ArrayList<>();
        list.add(new UartAction(versionName,MyListViewAdapter.ShowTypeButton,fun,version));
        list.add(new UartAction(micupName,MyListViewAdapter.ShowTypeSeekBar,fun,micup));
        list.add(new UartAction(micdownName,MyListViewAdapter.ShowTypeSeekBar,fun,micdown));
        list.add(new UartAction(musicupName,MyListViewAdapter.ShowTypeSeekBar,fun,musicup));
        list.add(new UartAction(musicdownName,MyListViewAdapter.ShowTypeSeekBar,fun,musicdown));
        list.add(new UartAction(echoupName,MyListViewAdapter.ShowTypeSeekBar,fun,echoup));
        list.add(new UartAction(echodownName,MyListViewAdapter.ShowTypeSeekBar,fun,echodown));
        return list;
    }

    public UartAction(String name, String showtype,String fun, int[] code) {
        super(name,fun,code2Str(code),showtype);
    }
}
