package com.thunder.ktv.androidboardtest.function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class MusicLevelFun extends EditorFun {

    byte[] bytes = null;
    List list = null;

    void initData(int codeType,int defCode)
    {
        list = new ArrayList();
        bytes = new byte[2];
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x41;
        list.add(new EditorFun(codeType,"外放音乐音量 Speaker MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) defCode));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x07;
        list.add(new EditorFun(codeType,"耳机音乐音量 Headphone MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) defCode));

    }
    public MusicLevelFun(int codeType,String showName, byte[] command,byte minCode, byte maxCode, byte defCode) {
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
