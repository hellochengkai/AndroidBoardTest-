package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.function.basefun.SeekFun;

/**
 * Created by chengkai on 18-2-27.
 */

public class EditorFun extends SeekFun {


    byte offsetCode = 0;
    public EditorFun(int funType,String showName, byte[] command, byte minCode, byte maxCode, byte defCode) {
        super(funType,showName, command,(byte) (maxCode - minCode), (byte) (defCode - minCode));
        offsetCode = minCode;
        if(command != null){
            this.command[this.command.length - 1] = defCode;
        }
        writeCode(this.command,showName);
    }

    int getCurPercent(byte code)
    {
        int curPercent = 0;
        if(max != 0){
            curPercent = ((code - offsetCode)*100)/max;
        }
        return curPercent;
    }

    @Override
    public boolean doAction(Object o) {
        int code = (int) o;
        command[command.length - 1] = (byte) ((byte) code + this.offsetCode);
        return writeCode(command,showName);
    }

    public String getShowInfo()
    {
        return String.format("{%3d%%}Command:%02x %02x %02x,  Min %02x,Max %02x,Def %02x",
                getCurPercent(command[command.length - 1]), command[0], command[1], command[2],offsetCode,max + offsetCode,def + offsetCode);
    }
    @Override
    public byte[] getUpCode() {
        return null;
    }

    @Override
    public byte[] getDownCode() {
        return null;
    }
}
