package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class VersionFun extends ButtonListFun {

    String msg = new String();
    byte[] byteread = new byte[10];
    boolean getVersion(String name ,byte[] command)
    {
        if(command == null){
            return false;
        }
        boolean ret = false;
        int readlen;
        if(writeCode(command)){
            readlen = readCode(byteread,byteread.length);
            if(readlen > 0){
                ret = true;
                msg = name + ":" + "{Rx}:" + byteCode2String(byteread,readlen);
            }
        }
        return ret;
    }


    public VersionFun() {
        super(FUN_TYPE_DEF,"版本号");
        List list = new ArrayList();
        list.add(new ButtonBase() {
            @Override
            public boolean doAction() {
                byte[] bytes = new byte[3];
                bytes[0] = (byte) 0xbf;bytes[1] = (byte) 0x00;bytes[2] = (byte) 0x00;
                return getVersion(getName(),bytes);
            }
            @Override
            public String getName() {
                return "版本号 Version Request";
            }

            @Override
            public int getCode() {
                return 0;
            }
        });
        list.add(new ButtonBase() {
            @Override
            public boolean doAction() {
                byte[] bytes = new byte[3];
                bytes[0] = (byte) 0xbf;bytes[1] = (byte) 0x04;bytes[2] = (byte) 0x00;
                return getVersion(getName(),bytes);
            }
            @Override
            public String getName() {
                return "内部版本号 Build No Request";
            }

            @Override
            public int getCode() {
                return 0;
            }
        });
        setButtonBaseList(list);
    }
    public String getShowInfo() {
        return msg;
    }
}
