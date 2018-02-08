package com.thunder.ktv.thunderjni.manager;

import android.os.Build;
import android.util.Log;



/**
 * Created by qian on 16-3-23.
 */
public class LibsManager {
    private static boolean      isLoaded = false;

    public static final String[] LIBS = new String[] {
            "thunderjni"
    };

    public static void loadNativeLibrarys() throws Exception {
        if(!loadLibs()) {
            throw new Exception("Couldn't load native libs");
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
        String[] libs = LIBS;
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
