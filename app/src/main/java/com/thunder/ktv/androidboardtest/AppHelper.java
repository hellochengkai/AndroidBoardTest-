package com.thunder.ktv.androidboardtest;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chengkai on 18-2-8.
 */

public class AppHelper {
    static Context context;

    static public Context getContext() {
        return context;
    }

    static public void setContext(Context context) {
        AppHelper.context = context;
    }
    public static void showMsg(String msg)
    {
        Toast.makeText(AppHelper.getContext(), msg,
                Toast.LENGTH_SHORT).show();
    }
}
