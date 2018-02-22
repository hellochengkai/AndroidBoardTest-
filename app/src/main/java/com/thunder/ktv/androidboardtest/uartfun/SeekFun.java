package com.thunder.ktv.androidboardtest.uartfun;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.Arrays;

/**
 * Created by chengkai on 18-2-13.
 */

public class SeekFun extends AbsFunction {
    private int curPercent = 0;
    public byte minCode = 0;
    public byte maxCode = (byte) 0xff;
    public byte defCode = 0;

    public SeekFun(String showName, byte[] command,byte minCode,byte maxCode,byte defCode) {
        super(showName);
        writeCode = new byte[command.length + 1];
        System.arraycopy(command,0,writeCode,0,command.length);
        writeCode[writeCode.length - 1] = defCode;
        this.defCode = defCode;
        this.minCode = minCode;
        this.maxCode = maxCode;
        curPercent = (defCode - minCode) * 100/(maxCode - minCode);
        setShowInfo(getInfo());
    }
    private String getInfo()
    {
        return String.format("{%3d%%}Command:%02x %02x %02x,  Min %02x,Max %02x,Def %02x",
                curPercent,writeCode[0],writeCode[1],writeCode[2],minCode,maxCode,defCode);
    }
    @Override
    public boolean doAction(Object o) {
        int code = (int) o;
        writeCode[writeCode.length - 1] = (byte) ((byte) code + this.minCode);
        curPercent = (code * 100 )/(maxCode - minCode);
        int fd = TDHardwareHelper.nativeOpenUart(UART_DEV.getBytes(),UART_RATE);
        AppHelper.showMsg("[Tx]:" + byteCode2String(writeCode));
        setShowInfo(getInfo());
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
    public String getShowName()
    {
        return showName;
    }
}
