package com.thunder.ktv.androidboardtest.function;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by chengkai on 18-2-7.
 */

public class DefaultInfo {

    private static final int UART_NUM = 1;

    private static DefaultInfo instance;
    private FunctionInfo functionInfo = null;

    public FunctionInfo getFunctionInfo() {
        return functionInfo;
    }
    static Map <String,Integer[]>uartActionMap = new HashMap<>();

    private DefaultInfo() {
        List<IFunction> functionBaseList = new LinkedList<>();
        functionBaseList.add(UartFunction.getDefUartFunction());
        functionInfo = new FunctionInfo()
        .setVersion("V1.0")
        .setFunction_list(functionBaseList)
        .setAction_list(UartAction.getDefUartAction());
    }
    public static DefaultInfo getInstence()
    {
        if(instance == null){
            synchronized (DefaultInfo.class){
                if(instance == null){
                    instance = new DefaultInfo();
                }
            }
        }
        return instance;
    }
}
