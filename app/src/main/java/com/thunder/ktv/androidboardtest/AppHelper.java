package com.thunder.ktv.androidboardtest;

import android.content.Context;
import android.widget.Toast;

import java.util.Observable;

/**
 * Created by chengkai on 18-2-8.
 */

public class AppHelper{
    static Context context;

    static public Context getContext() {
        return context;
    }

    static public void setContext(Context context) {
        AppHelper.context = context;
    }
    public static String allmsg = new String();
    private static int msgConut = 0;
    public static void showMsg(String msg)
    {
        AppHelper.allmsg  = AppHelper.allmsg  + msgConut+":" + msg +"\n";
        if(updataMsg == null){
            Toast.makeText(AppHelper.getContext(), msg,
                    Toast.LENGTH_SHORT).show();
        }else{
            updataMsg.updataMsg(AppHelper.allmsg);
        }
        msgConut++;
    }
    interface UpdataMsg
    {
        void updataMsg(String msg);
    }
    static UpdataMsg updataMsg = null;

    public static void setUpdataMsg(UpdataMsg updataMsg) {
        AppHelper.updataMsg = updataMsg;
    }
    public static void clearMsg()
    {
        allmsg = new String();
        msgConut = 0;
    }
}
