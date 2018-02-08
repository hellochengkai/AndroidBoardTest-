package com.thunder.ktv.androidboardtest.function;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chengkai on 18-2-7.
 */

public class UartFunction extends IFunction{
    private static final String TAG = "UartFunction";
    private static final String  UART_DEV = "/dev/ttyAMA0";
    private static final String  UART_NAME = "串口方法0";
    private static final int UART_RATE = 38400;

    static public String getDefUartFun()
    {
        return UART_NAME;
    }
    public static IFunction getDefUartFunction()
    {
        List<String> list = new ArrayList<>();
        list.add(UART_DEV);
        list.add("" + UART_RATE);
        return new UartFunction(UART_DEV,UART_RATE,UART_NAME);
    }

    public UartFunction(String dev,int rate,String name) {
        List<String> list = new ArrayList<>();
        list.add(dev);
        list.add("" + rate);
        functionBase = new FunctionBase()
                    .setName(name)
                    .setType("uart")
                    .setArgs(list);
    }

    private int fd = 0;
    @Override
    public boolean doFunction(Object o, View view) {
        boolean ret = false;
        Log.d(TAG, "doFunction: " + this.toString() + " o == " + o);
        int[] writeData = UartAction.Str2code((String) o);
        if(writeData == null){
            AppHelper.showMsg(String.format("码值解析错误,%s",o));
            return ret;
        }
        String dev = functionBase.getArgs().get(0);
        int rate =Integer.parseInt(functionBase.getArgs().get(1));
        fd = TDHardwareHelper.nativeOpenUart(dev.getBytes(),rate);
        if(fd <= 0){
            AppHelper.showMsg(String.format("串口%s %d 打开失败",dev,rate));
            fd = 0;
            return ret;
        }
        byte[] writeBytes = new byte[writeData.length];
        for(int i = 0; i < writeData.length;i++){
            writeBytes[i] = (byte) writeData[i];
        }
        int writelen = TDHardwareHelper.nativeWriteUart(fd, writeBytes,writeBytes.length);
        if(writelen != writeBytes.length){
            AppHelper.showMsg(String.format("数据写入失败 writelen %d",writelen));
        }
        byte[] byteread = new byte[10];
        int readlen = TDHardwareHelper.nativeReadUart(fd,byteread,byteread.length);
        if(readlen > 0){
            String msg = new String("read data:");
            for (int i = 0;i < readlen;i++){
                msg+= String.format("0x%x, ",byteread[i]);
            }
//            AppHelper.showMsg(String.format(msg));
            TextView textView = (TextView) view;
            textView.setText(msg);
        }
        TDHardwareHelper.nativeCloseUart(fd);
        ret = true;
        return ret;
    }
}
