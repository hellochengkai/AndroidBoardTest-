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
        int fd,readlen;
        boolean ret = false;
        fd = TDHardwareHelper.nativeOpenUart(UART_DEV.getBytes(),UART_RATE);
        if(fd <= 0){
            AppHelper.showMsg(String.format("串口%s %d 打开失败",UART_DEV,UART_RATE));
            return ret;
        }
        AppHelper.showMsg("[Tx]:" + byteCode2String(command));
        int writeLen = TDHardwareHelper.nativeWriteUart(fd, command,command.length);
        if(writeLen != command.length){
            AppHelper.showMsg("串口寫入失敗！！" + byteCode2String(command));
        }else{
            AppHelper.showMsg("串口寫入成功！！" + byteCode2String(command));
        }
        readlen = TDHardwareHelper.nativeReadUart(fd,byteread,byteread.length);
        if(readlen > 0){
            AppHelper.showMsg("{Rx}:" + byteCode2String(byteread,readlen));
            msg = name + ":" + "{Rx}:" + byteCode2String(byteread,readlen);
        }else{
            AppHelper.showMsg("{Rx}:读取失败");
            msg = name + ":" + "{Rx}:读取失败";
        }
        TDHardwareHelper.nativeCloseUart(fd);
        ret = true;
        return ret;
    }


    public VersionFun() {
        super("版本号");
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
        });
        setButtonBaseList(list);
    }
    public String getShowInfo() {
        return msg;
    }
}
