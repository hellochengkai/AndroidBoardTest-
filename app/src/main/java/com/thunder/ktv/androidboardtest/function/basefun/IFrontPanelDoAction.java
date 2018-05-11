package com.thunder.ktv.androidboardtest.function.basefun;

/**
 * Created by chengkai on 18-5-11.
 */

public interface IFrontPanelDoAction {
    byte[] getCode();
    byte[] getCbCode();
    boolean doAction(Object o);
}
