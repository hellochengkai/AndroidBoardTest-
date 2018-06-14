package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;

/**
 * Created by chengkai on 18-6-14.
 */

public class SpeakerOutputFun extends EditorFun implements IBindFrontPanel {
    static byte[] bytes = {(byte) 0xb0,(byte) 0x08};
    private static final byte [] MastVolumUPCode =  {(byte) 0xEF, (byte) 0xc4, (byte) 0x03, (byte) 0xfe};
    private static final byte [] MastVolumDownCode =  {(byte) 0xEF, (byte) 0xc4, (byte) 0x02, (byte) 0xfe};

    public SpeakerOutputFun() {
        super(AbsFunction.FUN_TYPE_MAST_VOLUMER,"Speaker Output Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x03);
    }
    @Override
    public byte[] getUpCode() {
        return MastVolumUPCode;
    }

    @Override
    public byte[] getDownCode() {
        return MastVolumDownCode;
    }
}
