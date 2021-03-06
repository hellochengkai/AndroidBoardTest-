package com.thunder.ktv.androidboardtest.function;

import android.util.Log;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chengkai on 18-2-22.
 */

public class RolandPrmFun extends AbsFunction {
    private static final String TAG = "RolandPrmFun";

    List<byte[]> codeList = null;
    public RolandPrmFun(String showName,String path,boolean needRunInThread) {
        super(FUN_TYPE_DEF,MyListViewAdapter.ItemViewTypeButton,showName, null);
        codeList = new ArrayList<>();
        if(needRunInThread){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadCode(path);
                }
            }).start();
        }else {
            loadCode(path);
        }
    }
    private boolean isInit = false;
    private byte [] head = new byte[16];
    private boolean loadCode(String path)
    {
        File file = new File(path);
        if(!file.exists()){
            AppHelper.showMsg(showName + "获取"+path+"文件失败!!!");
            return false;
        }
        int readLen = 0;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            readLen = fileInputStream.read(head);
            if(readLen != head.length){
                AppHelper.showMsg(showName + "获取头信息失败!!!");
            }
            if(!new String(head).startsWith("Roland")){
                AppHelper.showMsg(showName + "获取头信息失败!!!");
            }
            while (true){
                byte[] code = new byte[3];
                readLen = fileInputStream.read(code);
                if(readLen == code.length){
                    code[0] = (byte) (code[0] | 0xb0);
                    Log.d(TAG, "doAction: read " + byteCode2String(code));
                }else{
                    Log.d(TAG, "doAction: read over " + readLen);
                    break;
                }
                codeList.add(code);
            }
            isInit = true;
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    private int writeCodeNum = 0;
    @Override
    public boolean doAction(Object o) {
        if(isInit == false){
            AppHelper.showMsg(String.format(showName + " 码值未加载！！"));
            return false;
        }
        writeCodeNum = 0;
        Log.d(TAG, "doAction: head " + new String(head));
        Iterator iterator = codeList.iterator();
        while (iterator.hasNext()){
            byte[] code = (byte[]) iterator.next();
            short key = (short) ((code[0] << 8) + code[1]);
            if(RolandCodeManage.addCode(key,code[2])) {
                writeCodeNum++;
                writeCodeNoMsg(code);
            }
        }
        if(writeCodeNum == 0){
            AppHelper.showMsg( showName + " 当前已经是"+showName+",无需切换!!!");
        }else{
            AppHelper.showMsg( showName + " 写入完毕("+writeCodeNum+")!!!");
        }
        return false;
    }

    public String getShowInfo()
    {
        return new String("头信息:" + new String(head) + "写入" + writeCodeNum + "项");
    }
}
