package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;

/**
 * Created by chengkai on 18-5-14.
 */

public class MicFun extends EditorFun implements IBindFrontPanel{
    private static final byte [] MicUPCode =    {(byte) 0xEF, (byte) 0xc2, (byte) 0x02, (byte) 0xfe};
    private static final byte [] MicDownCode =    {(byte) 0xEF, (byte) 0xc2, (byte) 0x03, (byte) 0xfe};
    @Override
    public byte[] getUpCode() {
        return MicUPCode;
    }
    @Override
    public byte[] getDownCode() {
        return MicDownCode;
    }
    static byte[] bytes = {(byte) 0xb0,(byte) 0x03};
    public MicFun() {
        super(AbsFunction.FUN_TYPE_MIC, "麦克风主音量 MIC Master", bytes, (byte) 0x00,(byte) 0x7f, (byte) 0x4d);
    }
}
