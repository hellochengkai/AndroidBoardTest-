package com.thunder.ktv.androidboardtest.function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class EchoFun extends EditorFun{

    byte[] bytes = null;
    List list = null;

    void initData(int codeTypem,int defCode)
    {
        list = new ArrayList();
        bytes = new byte[2];
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x42;
        list.add(new EditorFun(codeType,"外放延时 Speaker MIC Delay Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) defCode));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x43;
        list.add(new EditorFun(codeType,"外放混响 Speaker MIC Reverb Leve",bytes,(byte) 0x00,(byte) 0x7f, (byte) defCode));

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x1d;
        list.add(new EditorFun(codeType,"耳机延时 Headphone MIC Delay Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) defCode));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x1e;
        list.add(new EditorFun(codeType,"耳机混响 Headphone MIC Reverb Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) defCode));

    }
    public EchoFun(int codeType,String showName, byte[] command,byte minCode, byte maxCode, byte defCode) {
        super(codeType,showName, command,minCode, maxCode,defCode);
        initData(codeType,defCode);
    }

    @Override
    public boolean doAction(Object o) {
        for (int i = 0; i < list.size();i ++){
            EditorFun editorFun = (EditorFun) list.get(i);
            editorFun.doAction(o);
        }
        return false;
    }

    public String getShowInfo()
    {
        String msg = new String();
        for (int i = 0; i < list.size();i ++){
            EditorFun editorFun = (EditorFun) list.get(i);
            msg+="{"+ editorFun.getCurPercent(editorFun.command[editorFun.command.length - 1]) +"%}" + editorFun.showName + "  -->" + editorFun.byteCode2String(editorFun.command);
            msg+="\n";
        }
        return msg;
    }
}
