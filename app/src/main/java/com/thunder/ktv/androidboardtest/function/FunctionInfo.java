package com.thunder.ktv.androidboardtest.function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengkai on 18-2-7.
 */

@JsonIgnoreProperties (ignoreUnknown = true)
public class FunctionInfo implements Serializable{
    public String version;
    public List<IFunction> function_list;
    public List<ActionBase> action_list;

    public String getVersion() {
        return version;
    }

    public FunctionInfo setVersion(String version) {
        this.version = version;
        return this;
    }

    public List<IFunction> getFunction_list() {
        return function_list;
    }

    public FunctionInfo setFunction_list(List<IFunction> function_list) {
        this.function_list = function_list;
        return this;
    }

    public List<ActionBase> getAction_list() {
        return action_list;
    }

    public FunctionInfo setAction_list(List<ActionBase> action_list) {
        this.action_list = action_list;
        return this;
    }
}
