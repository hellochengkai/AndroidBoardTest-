package com.thunder.ktv.androidboardtest.function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by chengkai on 18-2-8.
 */
@JsonIgnoreProperties (ignoreUnknown = true)
public class UartCode implements Serializable {
    public int[] code;

    public UartCode() {
    }

    public UartCode(int[] code) {
        this.code = code;
    }

    public int[] getCode() {
        return code;
    }

    public void setCode(int[] code) {
        this.code = code;
    }
}
