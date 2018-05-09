package com.thunder.ktv.androidboardtest.function.basefun;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

/**
 * Created by chengkai on 18-2-13.
 */

abstract public class AbsFunction {
    private static final String TAG = "AbsFunction";
    protected static final String  UART_DEV = "/dev/ttyAMA0";
    protected static final int UART_RATE = 38400;

    public final static int FUN_TYPE_DEF = -1;
    //    public final static int TYPE_DELAY = 0;
    public final static int FUN_TYPE_ECHO_DELAY = 1;
    public final static int FUN_TYPE_KTV_BT_LINE = 2;
    public final static int FUN_TYPE_PLAYER_VOLUME = 3;
    public final static int FUN_TYPE_MIC = 4;
    public final static int FUN_TYPE_MUSIC = 5;
    public final static int FUN_TYPE_ROLANDEFFECT = 6;


    public int funType = FUN_TYPE_DEF;

    public int showType = 0;
    public boolean writeCode(byte[] code) {
        if(code == null){
            return false;
        }
        int fd = TDHardwareHelper.nativeOpenUart(UART_DEV.getBytes(),UART_RATE);
        if(fd < 0){
            AppHelper.showMsg(String.format("串口%s %d 打开失败",UART_DEV,UART_RATE));
            return false;
        }
        int writeLen = TDHardwareHelper.nativeWriteUart(fd, code, code.length);
        if(writeLen != code.length){
            AppHelper.showMsg("[Tx error]:" + byteCode2String(code));
        }else{
            AppHelper.showMsg("[Tx]:" + byteCode2String(code));
        }
        TDHardwareHelper.nativeCloseUart(fd);
        return false;
    }

    public String getShowInfo() {
        return new String();
    }


    public String showName;
    public byte[] command;

    abstract public boolean doAction(Object o);

    public AbsFunction(int funType,int showType,String showName, byte[] command) {
        this.funType = funType;
        this.showType = showType;
        this.showName = showName;
        if(command != null){
            this.command = new byte[command.length + 1];
            System.arraycopy(command,0, this.command,0,command.length);
        }
    }

    protected static String byteCode2String(byte[] code,int len)
    {
        String codeStr = new String();
        if(code == null || len == 0 || code.length < len){
            return codeStr;
        }
        for (int i = 0; i < len; i++){
            codeStr+= String.format("%02x ",code[i]);
        }
        return codeStr;
    }
    public String byteCode2String(byte[] code)
    {
        String codeStr = new String();
        if(code == null || code.length == 0){
            return codeStr;
        }
        for (int i = 0; i < code.length; i++){
            codeStr+= String.format("%02x ",code[i]);
        }
        return codeStr;
    }

    public String getShowName()
    {
        return showName;
    }
}
