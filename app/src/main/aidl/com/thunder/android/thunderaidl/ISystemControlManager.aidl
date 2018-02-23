// ISystemControlManager.aidl
package com.thunder.android.thunderaidl;
import com.thunder.android.thunderaidl.ISystemControlCallback;

// Declare any non-default types here with import statements

interface ISystemControlManager {
    void registerCallback(ISystemControlCallback cb);
    void unregisterCallback(ISystemControlCallback cb);

    int reboot();
    int powerOff();
    int startAdb();
    int stopAdb();
    int execPm(in String args);
    int execAm(in String args);
    int setScreenOrientationLandScape();
    int setScreenOrientationPortrait();
    int chmod(String fileName, String mode);
    int insmod(String koPath);
    boolean updateRom(String updateFilePath);
    void startCaptureLog(String logcatArg);
    void stopCaptureLog();
    void addReportStream(String name,ISystemControlCallback cb);
    void removeReportStream(String name);
    void setScreenUseable(in int value);
    int execShellCmd(in String shellCmd);
    int gpioDirSet(long gpio_no, long dir);
    int gpioBitSet(long gpio_no, long bit);
    int gpioBitGet(long gpio_no);
    int  regWrite(long reg_addr, long value);
    int regRead(long reg_addr);
}
