package com.thunder.ktv.androidboardtest.uartfun;

/**
 * Created by chengkai on 18-2-13.
 */

abstract public class AbsFunction {
    private static final String TAG = "AbsFunction";
    protected static final String  UART_DEV = "/dev/ttyAMA0";
    protected static final int UART_RATE = 38400;

    protected String showInfo;

    public String getShowInfo() {
        return showInfo;
    }

    public void setShowInfo(String showInfo) {
        this.showInfo = showInfo;
    }

    protected String showName;
    protected byte[] writeCode;
    abstract public boolean doAction(Object o);

    public AbsFunction(String showName) {
        this.showName = showName;
    }

    protected String byteCode2String(byte[] code)
    {
        String codeStr = new String();
        if(code == null || code.length == 0){
            return codeStr;
        }
        for (int i = 0; i < code.length; i++){
//            if(i == code.length - 1){
//                codeStr+= String.format("0x%02x",code[i]);
//            }else {
//                codeStr+= String.format("0x%02x,",code[i]);
//            }
            codeStr+= String.format("%02x ",code[i]);
        }
        return codeStr;
    }
}
