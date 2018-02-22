package com.thunder.ktv.androidboardtest.uartfun;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.Arrays;

/**
 * Created by chengkai on 18-2-13.
 */

public class SeekFun extends AbsFunction {

    public byte minCode = 0;
    public byte maxCode = (byte) 0xff;
    double minShowVol = 0.00;
    double maxShowVol = 0;

    public SeekFun(String showName, byte[] baseCode,byte minCode,byte maxCode) {
        super(showName);
        writeCode = new byte[baseCode.length + 1];
        System.arraycopy(baseCode,0,writeCode,0,baseCode.length);
        this.minCode = minCode;
        this.maxCode = maxCode;
    }
    @Override
    public boolean doAction(Object o) {
        writeCode[writeCode.length - 1] = getSeekCode((Integer) o);
        int fd = TDHardwareHelper.nativeOpenUart(UART_DEV.getBytes(),UART_RATE);
        showInfo = "["+ getShowInfoByPercent((Integer) o) +"]" + byteCode2String(writeCode);
        AppHelper.showMsg("[Tx]:" + byteCode2String(writeCode));
        if(fd < 0){
            //AppHelper.showMsg("串口"+UART_DEV+"打開失敗！！");
            return false;
        }
        int writeLen = TDHardwareHelper.nativeWriteUart(fd,writeCode,writeCode.length);
        if(writeLen != writeCode.length){
            AppHelper.showMsg("串口寫入失敗！！" + byteCode2String(writeCode));
        }else{
            AppHelper.showMsg("串口寫入成功！！" + byteCode2String(writeCode));
        }
        return false;
    }
    private byte getSeekCode(int percent)
    {
        return (byte) ((maxCode - minCode) * percent / 100);
    }
    private String getShowInfoByPercent(int percent)
    {
        return String.format("-%ddB",percent);
    }
    public String getShowName()
    {
        return showName;
    }
}
