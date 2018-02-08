package com.thunder.ktv.androidboardtest.function;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chengkai on 18-2-7.
 */

@JsonIgnoreProperties (ignoreUnknown = true)
public class ActionBase implements Serializable {
    public String show_name;
    public String fun;
    public String cmd;
    public String view_type;

    public ActionBase() {
    }

    public ActionBase(String show_name, String fun, String cmd, String view_type) {
        this.show_name = show_name;
        this.fun = fun;
        this.cmd = cmd;
        this.view_type = view_type;
    }

    public String getView_type() {
        return view_type;
    }

    public void setView_type(String view_type) {
        this.view_type = view_type;
    }

    public String getShow_name() {
        return show_name;
    }

    public void setShow_name(String show_name) {
        this.show_name = show_name;
    }

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    public void doAction()
    {

    }
}
