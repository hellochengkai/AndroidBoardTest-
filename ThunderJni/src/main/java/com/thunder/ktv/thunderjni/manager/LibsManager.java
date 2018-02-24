package com.thunder.ktv.thunderjni.manager;

import android.os.Build;
import android.util.Log;



/**
 * Created by qian on 16-3-23.
 */
public class LibsManager {
    private static boolean      isLoaded = false;

    public static final String[] LIBS7000 = new String[] {
            "7432es",
            "adaptor_7000",
            "thunderjni_v4"
    };

    public static final String[] LIBS8000 = new String[] {
            "th8000",
            "adaptor_8000",
            "thunderjni_v7"
    };


    public static void loadNativeLibrarys() throws Exception {
        if(!loadLibs()) {
            throw new Exception("Couldn't load native libs");
        }
    }

    public static String[] getCurrentLibrarysInfo() {

        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.KITKAT://7000
                return LIBS7000;
            case Build.VERSION_CODES.N://8000
            default:
                return LIBS8000;
        }
    }


    /**
     * loads all native libraries
     * @return true if all libraries was loaded, otherwise return false
     */
    private static boolean loadLibs() {
        if(isLoaded) {
            return true;
        }
        String[] libs = getCurrentLibrarysInfo();
        boolean err = false;
        for(int i=0;i<libs.length;i++) {
            try {
                System.loadLibrary(libs[i]);
                Log.d("LibsManager", "load library " + libs[i] + " success");
            } catch(UnsatisfiedLinkError e) {
                // fatal error, we can't load some our libs
                Log.d("LibsManager", "Couldn't load lib: " + libs[i] + " - " + e.getMessage());
                err = true;
                System.out.println("0000  "+libs[i]);
            }
        }
        if(!err) {
            isLoaded = true;
        }
        return isLoaded;
    }
}
