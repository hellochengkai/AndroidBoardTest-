package com.thunder.ktv.androidboardtest.function;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.ButtonListFun;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chengkai on 18-5-10.
 */

public class VideoControlFun  extends ButtonListFun{
    List list = null;
    public VideoControlFun() {
        super(FUN_TYPE_VIDEO_CONTROL, "视频控制");
        list = new ArrayList();
        list.add(audioChangeButton);
        setButtonBaseList(list);
    }

    ButtonBase audioChangeButton = new ButtonBase() {
        @Override
        public boolean doAction() {
            msg = getName() + ":audio_select == " + AppHelper.getThPlayer().audio_select();
            AppHelper.showMsg(msg);
            return true;
        }

        @Override
        public String getName() {
            return "切换音轨";
        }

        @Override
        public int getCode() {
            return 0x07;
        }
    };
    String msg = null;
    @Override
    public String getShowInfo() {
        return msg;
    }
}
