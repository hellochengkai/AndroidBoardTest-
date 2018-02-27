package com.thunder.ktv.androidboardtest.function;
import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.thunderapi.TDHardwareHelper;
/**
 * Created by chengkai on 18-2-22.
 */
public class ButtonFun extends AbsFunction{
    private static final String TAG = "ButtonFun";

    public ButtonFun(String showName, byte[] command) {
        super(MyListViewAdapter.ItemViewTypeButton,showName, command);
    }
    byte[] byteread = new byte[10];
    int readlen = 0;
    @Override
    public boolean doAction(Object o) {
        int fd;
        boolean ret = false;
        fd = TDHardwareHelper.nativeOpenUart(UART_DEV.getBytes(),UART_RATE);
        if(fd <= 0){
            AppHelper.showMsg(String.format("串口%s %d 打开失败",UART_DEV,UART_RATE));
            return ret;
        }
        AppHelper.showMsg("[Tx]:" + byteCode2String(command));
        int writeLen = TDHardwareHelper.nativeWriteUart(fd, command,command.length);
        if(writeLen != command.length){
            AppHelper.showMsg("串口寫入失敗！！" + byteCode2String(command));
        }else{
            AppHelper.showMsg("串口寫入成功！！" + byteCode2String(command));
        }
        readlen = TDHardwareHelper.nativeReadUart(fd,byteread,byteread.length);
        if(readlen > 0){
            AppHelper.showMsg("{Rx}:" + byteCode2String(byteread,readlen));
        }else{
            AppHelper.showMsg("{Rx}:读取失败");
        }
        TDHardwareHelper.nativeCloseUart(fd);
        ret = true;
        return ret;
    }

    @Override
    public String getShowInfo() {
        if(readlen > 0){
            return String.format("Command:%02x %02x %02x, Rx:%s",
                    command[0], command[1], command[2],byteCode2String(byteread,readlen));
        }else{
            return String.format("Command:%02x %02x %02x",
                    command[0], command[1], command[2]);
        }

    }
}
