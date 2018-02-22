package com.thunder.ktv.androidboardtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thunder.ktv.androidboardtest.uartfun.AbsFunction;
import com.thunder.ktv.androidboardtest.uartfun.SeekFun;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.manager.LibsManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button buttonClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppHelper.setContext(getApplicationContext());
        AppHelper.setUpdataMsg(new AppHelper.UpdataMsg() {
            @Override
            public void updataMsg(String msg) {
                Log.d(TAG, "onCreate: updataMsg " + msg);
                EditText editText = findViewById(R.id.msg);
                editText.setText(msg);
                editText.setSelection(editText.getText().length());
            }
        });
        try {
            LibsManager.loadNativeLibrarys();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData();
        initview();
    }
    List<AbsFunction> list = null;
    void initview()
    {
        Button button = new Button(getApplicationContext());
        button.setText("asasa");
        RecyclerView recyclerView = findViewById(R.id.list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        MyListViewAdapter mAdapter = new MyListViewAdapter(defaultInfo.getFunctionInfo());
        MyListViewAdapter mAdapter = new MyListViewAdapter(list);
        // 设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        recyclerView.setAdapter(mAdapter);
        buttonClear = findViewById(R.id.bt_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.msg);
                editText.setText("");
            }
        });
    }
    void initData(){
        list = new ArrayList<>();
        byte[] bytes = new byte[2];

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x03;
        list.add(new SeekFun("MIC Master",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));

        bytes[0] = (byte) 0xb3;bytes[1] = (byte) 0x02;
        list.add(new SeekFun("Key Control Pitch 音乐变调",bytes,(byte) 0x34,(byte) 0x4c, (byte) 0x40));
        bytes[0] = (byte) 0xb3;bytes[1] = (byte) 0x03;
        list.add(new SeekFun("Key Control Pitch Fine 音乐变调,微调",bytes,(byte) 0x0e,(byte) 0x72, (byte) 0x40));

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x41;
        list.add(new SeekFun("Speaker MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x07;
        list.add(new SeekFun("Headphone MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));


        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x42;
        list.add(new SeekFun("Speaker MIC Delay Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x43;
        list.add(new SeekFun("Speaker MIC Reverb Leve",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x1d;
        list.add(new SeekFun("Headphone MIC Delay Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x1e;
        list.add(new SeekFun("Headphone MIC Reverb Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));

    }
}
