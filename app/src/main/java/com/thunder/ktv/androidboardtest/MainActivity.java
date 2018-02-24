package com.thunder.ktv.androidboardtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thunder.ktv.androidboardtest.function.GpioFun;
import com.thunder.ktv.androidboardtest.function.PlayerVolumeFun;
import com.thunder.ktv.androidboardtest.player.THPlayer;
import com.thunder.ktv.androidboardtest.function.AbsFunction;
import com.thunder.ktv.androidboardtest.function.ButtonFun;
import com.thunder.ktv.androidboardtest.function.RolandPrmFun;
import com.thunder.ktv.androidboardtest.function.SeekFun;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.manager.LibsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button buttonClear;
    THPlayer thPlayer;
    TextView textViewVol;
    SystemControlClientHelper systemControlClientHelper;
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
        systemControlClientHelper = SystemControlClientHelper.getInstance();
        systemControlClientHelper.initServer(getApplicationContext());
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
                AppHelper.clearMsg();
            }
        });
        SurfaceView surfaceView = findViewById(R.id.surface);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    thPlayer.play("/sdcard/video/test.ts",holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                thPlayer.stop();
            }
        });
        findViewById(R.id.bt_audio_select).setOnClickListener(onClickListener);
    }
    void initData(){
        list = new ArrayList<>();
        byte[] bytes = new byte[2];

        bytes[0] = (byte) 0xbf;bytes[1] = (byte) 0x00;
        list.add(new ButtonFun("版本号 Version Request",bytes,(byte) 0x00,(byte) 0x00, (byte) 0x00));

        bytes[0] = (byte) 0xbf;bytes[1] = (byte) 0x04;
        list.add(new ButtonFun("内部版本号 Build No Request",bytes,(byte) 0x00,(byte) 0x00, (byte) 0x00));

        list.add(new GpioFun("GPIO 2-3",2*8+3));
//        list.add(new GpioFun("GPIO 2-3 down",2*8+3,0));

        list.add(new GpioFun("GPIO 2-4",2*8+4));
//        list.add(new GpioFun("GPIO 2-4 down",2*8+4,0));

        list.add(new RolandPrmFun("效果1","/sdcard/roland/01.prm"));
        list.add(new RolandPrmFun("效果2","/sdcard/roland/02.prm"));
        list.add(new RolandPrmFun("效果3","/sdcard/roland/03.prm"));
        list.add(new RolandPrmFun("效果4","/sdcard/roland/04.prm"));

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x03;
        list.add(new PlayerVolumeFun("视频播放器音量"));


        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x03;
        list.add(new SeekFun("麦克风主音量 MIC Master",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));

        bytes[0] = (byte) 0xb3;bytes[1] = (byte) 0x02;
        list.add(new SeekFun("音乐变调 Key Control Pitch",bytes,(byte) 0x34,(byte) 0x4c, (byte) 0x40));

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x41;
        list.add(new SeekFun("外放音乐音量 Speaker MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x07;
        list.add(new SeekFun("耳机音乐音量 Headphone MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x42;
        list.add(new SeekFun("外放延时 Speaker MIC Delay Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x43;
        list.add(new SeekFun("外放混响 Speaker MIC Reverb Leve",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x1d;
        list.add(new SeekFun("耳机延时 Headphone MIC Delay Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));
        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x1e;
        list.add(new SeekFun("耳机混响 Headphone MIC Reverb Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x7f));

        thPlayer = new THPlayer(null);
        AppHelper.setThPlayer(thPlayer);
    }

    private int volume = 100;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bt_audio_select:{
                    Button button = (Button) v;
                    button.setText("切换音轨 " +  thPlayer.audio_select());
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };
}
