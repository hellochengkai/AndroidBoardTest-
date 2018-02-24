package com.thunder.ktv.androidboardtest.function;

import android.util.Log;

import com.thunder.ktv.androidboardtest.AppHelper;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by chengkai on 18-2-22.
 */

public class RolandPrmFun extends AbsFunction {
    private static final String TAG = "RolandPrmFun";
    private String path = null;
    public RolandPrmFun(String showName,String path) {
        super(MyListViewAdapter.ItemViewTypeButton,showName, null,(byte) 0,(byte) 0,(byte) 0);
        this.path = path;
    }
    byte [] head = new byte[16];


    private int writeCodeNum = 0;
    @Override
    public boolean doAction(Object o) {
        writeCodeNum = 0;
        int readLen = 0;
        File file = new File(path);
        if(!file.exists()){
            AppHelper.showMsg("获取"+path+"文件失败!!!");
            return false;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            readLen = fileInputStream.read(head);
            if(readLen != head.length){
                AppHelper.showMsg("获取头信息失败!!!");
            }
            if(!new String(head).startsWith("Roland")){
                AppHelper.showMsg("获取头信息失败!!!");
            }
            Log.d(TAG, "doAction: head " + new String(head));
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
                short key = (short) ((code[0] << 8) + code[1]);
                if(RolandCodeManage.addCode(key,code[2]))
                {
                    writeCodeNum++;
                    writeCode(code);
                }
            }
            AppHelper.showMsg("写入完毕("+writeCodeNum+")!!!");
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getShowInfo()
    {
        return new String("头信息:" + new String(head) + "写入" + writeCodeNum + "项");
    }
}
