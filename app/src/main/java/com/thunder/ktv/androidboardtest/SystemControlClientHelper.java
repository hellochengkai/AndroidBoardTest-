package com.thunder.ktv.androidboardtest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.thunder.android.thunderaidl.ISystemControlCallback;
import com.thunder.android.thunderaidl.ISystemControlManager;


/***
 *
 */

public class SystemControlClientHelper {

    public static final String TAG = SystemControlClientHelper.class.getSimpleName();
    private static SystemControlClientHelper systemControlClientManager;
    private ISystemControlCallback.Stub systemControlCallback;
    private ISystemControlManager systemControlManager;
    private boolean thunderSystemServiceConnected;
    private boolean isFirstConnected = true;
    private Context context;
    private RomUpgradeCallback romUpgradeCallback;
    private ServiceConnectListener serviceConnectListener;

    private SystemControlClientHelper() {
    }

    public static synchronized SystemControlClientHelper getInstance() {
        if (systemControlClientManager == null) {
            systemControlClientManager = new SystemControlClientHelper();
        }

        return systemControlClientManager;
    }

    public void initServer(Context context) {
        Log.d(TAG, "initServer");
        this.context = context;
        systemControlCallback = new ISystemControlCallback.Stub() {

            @Override
            public void romUpgradeCallback(int type, String msg) throws RemoteException {
            }

            @Override
            public void WriteLogCallback(byte[] log) throws RemoteException {

            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                //等待3秒，防止机顶盒启动时，服务没有准备好，导致无法启动服务的问题
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                bindThunderSystemService();
            }
        }).start();
    }
    public static final String SYSTEM_SERVICE_ACTION = "com.thunder.android.thundersystemservice.service";
    public static final String SYSTEM_SERVICE_PACKAGE_NAME = "com.thunder.android.thundersystemservice";

    private void bindThunderSystemService() {
        Log.d(TAG, "bindThunderSystemService,ThunderSystemServiceConnected = " + thunderSystemServiceConnected);
        if (!thunderSystemServiceConnected) {
            Intent intent = new Intent();
            intent.setAction(SYSTEM_SERVICE_ACTION);
            intent.setPackage(SYSTEM_SERVICE_PACKAGE_NAME);
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else{
            //service已连接
            if (serviceConnectListener != null) {
                serviceConnectListener.isConnected();
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "远程服务连接");
            Log.d(TAG, "onServiceConnected componentName = " + name);
            systemControlManager = ISystemControlManager.Stub.asInterface(service);
            thunderSystemServiceConnected = true;
            if (serviceConnectListener != null) {
                serviceConnectListener.isConnected();
            }
//            registerSystemControlCallback();
            if (isFirstConnected) {
                isFirstConnected = false;
            }

            execShellCmd("mkdir /sdcard/xx1test");
//            execShellCmd(AppConstant.CMD_KILL_SP_TS_DAEMON);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "远程服务断开");
            Log.d(TAG, "onServiceDisconnected componentName = " + name);
            thunderSystemServiceConnected = false;
//            unRegisterSystemControlCallback();
            if (serviceConnectListener != null) {
                serviceConnectListener.disconnected();
            }
        }
    };

    private void registerSystemControlCallback() {
        if (thunderSystemServiceConnected) {
            try {
                systemControlManager.registerCallback(systemControlCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            bindThunderSystemService();
        }
    }

    private void unRegisterSystemControlCallback() {
        if (thunderSystemServiceConnected) {
            try {
                systemControlManager.unregisterCallback(systemControlCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public int reboot() {
        try {
            if (systemControlManager != null) {
                return  systemControlManager.reboot();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public int powerOff() {
        try {
            if (systemControlManager != null) {
                return systemControlManager.powerOff();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public int startAdb() {
        try {
            if (systemControlManager != null) {
                return systemControlManager.startAdb();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public int stopAdb() {
        try {
            if (systemControlManager != null) {
                return systemControlManager.stopAdb();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public int execPm(String args) {
        int ret = -1;
        try {
            if (systemControlManager != null) {
                ret = systemControlManager.execPm(args);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return ret;
    }

    public int execAm(String args) {
        try {
            if (systemControlManager != null) {
                return systemControlManager.execAm(args);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public int setScreenOrientationLandScape() {
        Log.d(TAG,"setScreenOrientationLandScape begin");
        try {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (systemControlManager != null) {
                int ret = systemControlManager.setScreenOrientationLandScape();
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        Log.d(TAG,"setScreenOrientationLandScape end");
        return -1;
    }

    public int setScreenOrientationPortrait() {
        Log.d(TAG,"setScreenOrientationPortrait begin");
        try {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (systemControlManager != null) {
                int ret = systemControlManager.setScreenOrientationPortrait();
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        Log.d(TAG,"setScreenOrientationPortrait end");
        return -1;
    }

    public int chmod(String fileName, String mode) {
        try {
            if (systemControlManager != null) {
                return systemControlManager.chmod(fileName, mode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public int insmod(String koPath) {
        try {
            if (systemControlManager != null) {
                return systemControlManager.insmod(koPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public boolean updateRom(String updateFilePath) {
        try {
            if (systemControlManager != null) {
                return systemControlManager.updateRom(updateFilePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean startReportLog(String logcatArg)
    {
        try {
            if (systemControlManager != null) {
                systemControlManager.addReportStream(null,null);
                systemControlManager.startCaptureLog(logcatArg);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean stopReportLog()
    {
        try {
            if (systemControlManager != null) {
                systemControlManager.removeReportStream(null);
                systemControlManager.stopCaptureLog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void setScreenUseable(int useable) {
        Log.d(TAG,"setScreenUseable begin");
        try {
            if (systemControlManager != null) {
                systemControlManager.setScreenUseable(useable);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Log.d(TAG,"setScreenUseable end");
        return;
    }

    public void execShellCmd(String cmd) {
        Log.d(TAG,"execShellCmd begin:" + cmd);
        try {
            if (systemControlManager != null) {
                systemControlManager.execShellCmd(cmd);
                Log.d(TAG,"execShellCmd end " + cmd);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Log.d(TAG,"setScreenUseable end");
        return;
    }


    public int gpioDirSet(long gpio_no, long dir) {
        int ret = -1;
        Log.d(TAG,"gpioDirSet begin: gpio_no :" + gpio_no + "dir:" + dir);
        try {
            if (systemControlManager != null) {
                ret = systemControlManager.gpioDirSet(gpio_no,dir);
                Log.d(TAG,"gpioDirSet end gpio_no:" + gpio_no + " dir:" + dir);
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        }
        Log.d(TAG,"gpioDirSet end");
        return ret;
    }
    public int gpioBitSet(long gpio_no, long bit) {
        int ret = -1;
        Log.d(TAG,"gpioBitSet begin: gpio_no :" + gpio_no + "bit:" + bit);
        try {
            if (systemControlManager != null) {
                ret = systemControlManager.gpioBitSet(gpio_no,bit);
                Log.d(TAG,"gpioBitSet end gpio_no:" + gpio_no + " bit:" + bit);
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        }
        Log.d(TAG,"gpioBitSet end");
        return ret;
    }

    public int gpioBitGet(long gpio_no) {
        int ret = -1;
        Log.d(TAG,"gpioBitGet begin: gpio_no :" + gpio_no);
        try {
            if (systemControlManager != null) {
                ret = systemControlManager.gpioBitGet(gpio_no);
                Log.d(TAG,"gpioBitGet end gpio_no:" + gpio_no + "ret " + ret);
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        }
        Log.d(TAG,"gpioBitGet end");
        return ret;
    }
    public int regRead(long reg_addr) {
        int ret = -1;
        Log.d(TAG,"regRead begin: reg_addr :" + reg_addr);
        try {
            if (systemControlManager != null) {
                ret = systemControlManager.regRead(reg_addr);
                Log.d(TAG,"regRead end reg_addr:" + reg_addr + "ret " + ret);
                return ret;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        }
        Log.d(TAG,"regRead end");
        return ret;
    }

    public void onDestroy() {
        if (thunderSystemServiceConnected) {
        } else {
            bindThunderSystemService();
        }
    }

    public boolean isServiceConnected() {
        return thunderSystemServiceConnected;
    }

    public void setRomUpgradeCallback(RomUpgradeCallback romUpgradeCallback) {
        this.romUpgradeCallback = romUpgradeCallback;
    }

    public void setServiceConnectListener(ServiceConnectListener serviceConnectListener) {
        this.serviceConnectListener = serviceConnectListener;
    }


    public interface RomUpgradeCallback {
        void romUpgradeCallback(int type, String msg);
    }

    public interface ServiceConnectListener {
        void isConnected();
        void disconnected();
    }
}
