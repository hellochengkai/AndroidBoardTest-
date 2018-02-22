package com.thunder.ktv.androidboardtest.uartfun;

/**
 * Created by chengkai on 18-2-13.
 */

abstract public class AbsFunction {
    private static final String TAG = "AbsFunction";
    protected static final String  UART_DEV = "/dev/ttyAMA0";
    protected static final int UART_RATE = 38400;


    public String getShowInfo() {
        return new String();
    }


    protected String showName;
    protected byte[] command;
    public byte minCode = 0;
    public byte maxCode = (byte) 0xff;
    public byte defCode = 0;

    abstract public boolean doAction(Object o);

    public AbsFunction(String showName, byte[] command, byte minCode, byte maxCode, byte defCode) {
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
