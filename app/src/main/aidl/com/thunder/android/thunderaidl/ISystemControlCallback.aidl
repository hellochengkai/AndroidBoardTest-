// ISystemControlCallback.aidl
package com.thunder.android.thunderaidl;

// Declare any non-default types here with import statements

interface ISystemControlCallback {
    /**
    * 固件升级回调
    *
    * @param type 回调类型：0 失败、1 成功
    * @param msg  回调携带信息
    */
    void romUpgradeCallback(in int type,in String msg);
    /**
     *
     * @param log
     */
    void WriteLogCallback(in byte[] log);
}
