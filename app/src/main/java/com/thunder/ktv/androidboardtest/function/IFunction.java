package com.thunder.ktv.androidboardtest.function;

import android.view.View;

/**
 * Created by chengkai on 18-2-7.
 */

public abstract class IFunction {
    public FunctionBase functionBase = null;
    abstract public boolean doFunction(Object o, View view);

    @Override
    public String toString() {
        return String.format("name %s\ntype %s\nargs %s",functionBase.getName(),functionBase.getType(),functionBase.getArgs().toString());
    }
}
