package com.thunder.ktv.androidboardtest.function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengkai on 18-2-7.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class FunctionBase implements Serializable{
    public String type;
    public String name;
    public List<String> args;

    public String getType() {
        return type;
    }

    public FunctionBase setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public FunctionBase setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getArgs() {
        return args;
    }

    public FunctionBase setArgs(List<String> args) {
        this.args = args;
        return this;
    }
}
