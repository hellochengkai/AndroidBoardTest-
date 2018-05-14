package com.thunder.ktv.androidboardtest.function;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-3-9.
 */

public class MusicLevelFun extends EditorFun implements IBindFrontPanel{

    byte[] bytes = null;
    List list = null;

    private static final byte [] MusicUPCode =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MusicDownCode =    {(byte) 0xEF, (byte) 0xc1, (byte) 0x03, (byte) 0xfe};
    @Override
    public byte[] getUpCode() {
        return MusicUPCode;
    }

    @Override
    public byte[] getDownCode() {
        return MusicDownCode;
    }
    void initData(int defCode)
    {
        list = new ArrayList();
        bytes = new byte[2];
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x41;
        list.add(new EditorFun(FUN_TYPE_DEF,"外放音乐音量 Speaker MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) defCode));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x07;
        list.add(new EditorFun(FUN_TYPE_DEF,"耳机音乐音量 Headphone MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) defCode));

    }
    public MusicLevelFun() {
        super(FUN_TYPE_MUSIC,"音乐音量", null,(byte) 0x00,(byte) 0x7f, (byte) 0x4d);
        initData(0x4d);
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
