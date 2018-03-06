package com.thunder.ktv.androidboardtest.function;

/**
 * Created by chengkai on 18-2-27.
 */

public class EditorFun extends SeekFun{

    public final static int TYPE_UNKNOW = -1;
//    public final static int TYPE_DELAY = 0;
    public final static int TYPE_ECHO_DELAY = 1;
    public final static int TYPE_MIC = 2;
    public final static int TYPE_MUSIC = 3;
    public int codeType = TYPE_UNKNOW;
    byte offsetCode = 0;
    public EditorFun(int codeType,String showName, byte[] command, byte minCode, byte maxCode, byte defCode) {
        super(showName, command,(byte) (maxCode - minCode), (byte) (defCode - minCode));
        offsetCode = minCode;
        this.codeType = codeType;
        this.command[this.command.length - 1] = defCode;
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
        return writeCode(command);
    }

    public String getShowInfo()
    {
        return String.format("{%3d%%}Command:%02x %02x %02x,  Min %02x,Max %02x,Def %02x",
                getCurPercent(command[command.length - 1]), command[0], command[1], command[2],offsetCode,max + offsetCode,def + offsetCode);
    }
}
