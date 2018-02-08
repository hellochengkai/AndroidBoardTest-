package com.thunder.ktv.thunderjni.until;

import android.util.Log;

/**
 * @description 日志信息工具类
 * @version 1.0
 * @author lvzhenwei
 * @date 2012-3-31 上午10:20:38 
 * @update 2012-3-31 上午10:20:38 
 */

public class Logger {
	final private static String TAG = "Logger";
	private static boolean enable_debug = true;
	private static boolean debug_able = true;
	private static boolean info_able = true;
	private static boolean verbose = false;
	private static boolean error = true;

	public static void setDebugEnable(boolean enable) {
		enable_debug = enable;
	}

	public static void debug(String tag,String msg){
        if (enable_debug && debug_able && msg != null) {
            Log.d(tag, msg);
        }
    }
     
    public static void debug(String msg){
        if (enable_debug && debug_able && msg != null) {
            Log.d(TAG, msg);
        }
    }

	public static void info(String s) {
		if(enable_debug && info_able && s != null) {
			Log.i(TAG, s);
		}
		
	}
	
	public static void info(String tag,String s) {
		if(enable_debug && info_able && s != null) {
			Log.i(tag, s);
		}
		
	}

	public static void verbose(String s) {
		if(enable_debug && verbose && s != null) {
			Log.v(TAG, s);
		}
		
	}
	
	public static void verbose(String tag,String s) {
		if(enable_debug && verbose && s != null) {
			Log.v(tag, s);
		}
		
	}
	
	public static void error(String tag,String s) {
		if(enable_debug && error && s != null)
			Log.e(tag, s);
	}
	
	public static void error(String s) {
		if(enable_debug && error && s != null)
		Log.e(TAG, s);
	}

	public static void error(Throwable e) {
		if(enable_debug && error && e != null)
		Log.e(TAG, "", e);
	}

	public static void error(String msg, Throwable e) {
		if(enable_debug && error && e != null)
		Log.e(TAG, msg, e);
	}
}
