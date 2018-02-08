package com.thunder.ktv.androidboardtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thunder.ktv.androidboardtest.function.DefaultInfo;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.manager.LibsManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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
        Log.d(TAG, "onCreate: defaultInfoJson " + defaultInfoJson);
        try {
            LibsManager.loadNativeLibrarys();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initview();
    }
    void initview()
    {
        Button button = new Button(getApplicationContext());
        button.setText("asasa");
        RecyclerView recyclerView = findViewById(R.id.list);
        DefaultInfo defaultInfo = DefaultInfo.getInstence();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        MyListViewAdapter mAdapter = new MyListViewAdapter(defaultInfo.getFunctionInfo());
        // 设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        recyclerView.setAdapter(mAdapter);
    }
}
