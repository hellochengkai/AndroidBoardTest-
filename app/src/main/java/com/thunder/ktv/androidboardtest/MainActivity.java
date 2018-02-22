package com.thunder.ktv.androidboardtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thunder.ktv.androidboardtest.function.DefaultInfo;
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
        ObjectMapper objectMapper = new ObjectMapper();
        DefaultInfo defaultInfo = DefaultInfo.getInstence();

        String defaultInfoJson = null;
        try {
            defaultInfoJson = objectMapper.writeValueAsString(defaultInfo.getFunctionInfo());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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
        Log.d(TAG, "onCreate: defaultInfoJson " + defaultInfoJson);
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
        DefaultInfo defaultInfo = DefaultInfo.getInstence();
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
        byte[] bytes = {(byte) 0xb0, (byte) 0x03};
        list.add(new SeekFun("MIC Master",bytes,(byte) 0,(byte) 0x7f));

    }
}
