package com.thunder.ktv.androidboardtest.function;

/**
 * Created by chengkai on 18-2-13.
 */

public class SeekFun extends AbsFunction {
    private int curPercent = 0;

    public SeekFun(String showName, byte[] command,byte minCode,byte maxCode,byte defCode) {
        super(showName,command,minCode,maxCode,defCode);
        curPercent = (defCode - minCode) * 100/(maxCode - minCode);
    }
    public String getShowInfo()
    {
        return String.format("{%3d%%}Command:%02x %02x %02x,  Min %02x,Max %02x,Def %02x",
                curPercent, command[0], command[1], command[2],minCode,maxCode,defCode);
    }
    @Override
    public boolean doAction(Object o) {
        int code = (int) o;
        command[command.length - 1] = (byte) ((byte) code + this.minCode);
        curPercent = (code * 100 )/(maxCode - minCode);
        return writeCode(command);
    }
}
