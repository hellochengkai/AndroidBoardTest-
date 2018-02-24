package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

/**
 * Created by chengkai on 18-2-13.
 */

abstract public class AbsFunction {
    private static final String TAG = "AbsFunction";
    protected static final String  UART_DEV = "/dev/ttyAMA0";
    protected static final int UART_RATE = 38400;

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


    protected String showName;
    protected byte[] command;
    public byte minCode = 0;
    public byte maxCode = (byte) 0xff;
    public byte defCode = 0;

    abstract public boolean doAction(Object o);

    public AbsFunction(int showType,String showName, byte[] command, byte minCode, byte maxCode, byte defCode) {
        this.showType = showType;
        this.showName = showName;
        this.minCode = minCode;
        this.maxCode = maxCode;
        this.defCode = defCode;
        if(command != null){
            this.command = new byte[command.length + 1];
            System.arraycopy(command,0, this.command,0,command.length);
            this.command[this.command.length - 1] = defCode;
        }
    }

    protected String byteCode2String(byte[] code,int len)
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
    protected String byteCode2String(byte[] code)
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
