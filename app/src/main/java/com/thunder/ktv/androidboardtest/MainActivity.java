package com.thunder.ktv.androidboardtest;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.thunder.ktv.androidboardtest.function.EchoFun;
import com.thunder.ktv.androidboardtest.function.EditorFun;
import com.thunder.ktv.androidboardtest.function.FrontPanelFun;
import com.thunder.ktv.androidboardtest.function.KtvBtLineFun;
import com.thunder.ktv.androidboardtest.function.MastVolumer;
import com.thunder.ktv.androidboardtest.function.MicFun;
import com.thunder.ktv.androidboardtest.function.MusicLevelFun;
import com.thunder.ktv.androidboardtest.function.PlayerVolumeFun;
import com.thunder.ktv.androidboardtest.function.RolandEffectFun;
import com.thunder.ktv.androidboardtest.function.PowerFun;
import com.thunder.ktv.androidboardtest.function.SpeakerOutputFun;
import com.thunder.ktv.androidboardtest.function.VersionFun;
import com.thunder.ktv.androidboardtest.function.VideoControlFun;
import com.thunder.ktv.androidboardtest.player.THPlayer;
import com.thunder.ktv.androidboardtest.function.basefun.AbsFunction;
import com.thunder.ktv.androidboardtest.player.listener.IThunderPlayerListener;
import com.thunder.ktv.androidboardtest.view.MyListViewAdapter;
import com.thunder.ktv.thunderjni.manager.LibsManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button buttonClear;
    THPlayer thPlayer;
    private String[] thPlayerList = {
            "/sdcard/video/1.ts",
            "/sdcard/video/2.ts",
            "/sdcard/video/3.ts",
            "/sdcard/video/4.ts",
            "/sdcard/video/5.ts",
            "/sdcard/video/6.ts",
            "/sdcard/video/7.ts",
            "/sdcard/video/8.ts",
            "/sdcard/video/9.ts",
            "/sdcard/video/10.ts",
            "/sdcard/video/11.ts",
            "/sdcard/video/12.ts",
            "/sdcard/video/13.ts",
            "/sdcard/video/14.ts"
    };

    private int curPlayIndex = 0;
    private Handler handler=null;
    //    SystemControlClientHelper systemControlClientHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建属于主线程的handler
        handler=new Handler();
        AppHelper.setMainActivity(this);
        AppHelper.setContext(getApplicationContext());
        AppHelper.setUpdataMsg(new AppHelper.UpdataMsg() {
            @Override
            public void updataMsg(String msg) {
//                Log.d(TAG, "onCreate: updataMsg " + msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        EditText editText = findViewById(R.id.msg);
                        editText.setText(msg);
                        editText.setSelection(editText.getText().length());
                    }
                });
            }
        });
        try {
            LibsManager.loadNativeLibrarys();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        systemControlClientHelper = SystemControlClientHelper.getInstance();
//        systemControlClientHelper.initServer(getApplicationContext());
        initData();
        initview();
        PT2033Helper.getInstance();
    }
    List<AbsFunction> list = null;
    MyListViewAdapter mAdapter = null;

    public void upDataView()
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void playNextSong()
    {
        curPlayIndex++;
        if(curPlayIndex >= thPlayerList.length){
            curPlayIndex = 0;
        }
    }
    private void playFrontSong()
    {
        curPlayIndex--;
        if(curPlayIndex < 0){
            curPlayIndex = thPlayerList.length - 1;
        }
    }
    private void initview()
    {
        Button button = new Button(getApplicationContext());
        button.setText("asasa");
        RecyclerView recyclerView = findViewById(R.id.list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        MyListViewAdapter mAdapter = new MyListViewAdapter(defaultInfo.getFunctionInfo());
        mAdapter = new MyListViewAdapter(list);
        // 设置布局管理器
        recyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //屏幕中最后一个可见子项的position
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                Log.d(TAG, "onScrollStateChanged: 屏幕中最后一个可见子项的position " + lastVisibleItemPosition);

                //当前屏幕所看到的子项个数
                int visibleItemCount = layoutManager.getChildCount();
                Log.d(TAG, "onScrollStateChanged: 当前屏幕所看到的子项个数 " + visibleItemCount);

                //当前RecyclerView的所有子项个数
                int totalItemCount = layoutManager.getItemCount();
                Log.d(TAG, "onScrollStateChanged: 当前RecyclerView的所有子项个数 " + totalItemCount);

                //RecyclerView的滑动状态
                int state = recyclerView.getScrollState();
                Log.d(TAG, "onScrollStateChanged: RecyclerView的滑动状态 " + state);

                if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE){
                    Log.d(TAG, "onScrollStateChanged: aaaa " );
                }else {
                    Log.d(TAG, "onScrollStateChanged: bbbbbbb " );
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: " + dx);
                Log.d(TAG, "onScrolled: " + dy);
            }
        });
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView textView = findViewById(R.id.tv_video_name);
                            textView.setText(thPlayerList[curPlayIndex]);
                        }
                    });
                    thPlayer.play(thPlayerList[curPlayIndex],holder);
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
        findViewById(R.id.bt_video_front).setOnClickListener(onClickListener);
        findViewById(R.id.bt_video_next).setOnClickListener(onClickListener);
        findViewById(R.id.bt_video_replay).setOnClickListener(onClickListener);
    }
    FrontPanelFun frontPanelFun = null;
    void initData(){
        RolandEffectFun rolandEffectFun = new RolandEffectFun();
        rolandEffectFun.doDefaultEffect();//先设置默认效果

        frontPanelFun = new FrontPanelFun("前面版控制开关");
        list = new ArrayList<>();
        byte[] bytes = new byte[2];

        list.add(new VersionFun());
        list.add(new PowerFun());

        list.add(new SpeakerOutputFun());
//        list.add(new MastVolumer());
        list.add(new EchoFun());
        list.add(new MicFun());
        list.add(new MusicLevelFun());

//        list.add(new GpioSetFun());
        list.add(new VideoControlFun());
        list.add(new KtvBtLineFun());
        list.add(rolandEffectFun);
        list.add(new PlayerVolumeFun());
        bytes[0] = (byte) 0xb3;bytes[1] = (byte) 0x02;
        list.add(new EditorFun(AbsFunction.FUN_TYPE_DEF,"音乐变调 Key Control Pitch",bytes,(byte) 0x34,(byte) 0x4c, (byte) 0x40));

        bytes[0] = (byte) 0xb0;bytes[1] = (byte) 0x0c;
        list.add(new EditorFun(AbsFunction.FUN_TYPE_DEF,"AUX到音乐 AUX to MUSIC Level",bytes,(byte) 0x00,(byte) 0x7f, (byte) 0x00));

        bytes[0] = (byte) 0xb1;bytes[1] = (byte) 0x75;
        list.add(new EditorFun(AbsFunction.FUN_TYPE_DEF,"麦克风反馈抑制 移频量 Feedback Control Freq",bytes,(byte) 0x36,(byte) 0x4a, (byte) 0x40));

        thPlayer = new THPlayer(new IThunderPlayerListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                SurfaceView surfaceView = findViewById(R.id.surface);
                surfaceView.setVisibility(View.GONE);
                surfaceView.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {

            }
        });
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
                case R.id.bt_video_front:{
                    playFrontSong();
                    SurfaceView surfaceView = findViewById(R.id.surface);
                    surfaceView.setVisibility(View.GONE);
                    surfaceView.setVisibility(View.VISIBLE);
                    break;
                }
                case R.id.bt_video_next:{
                    playNextSong();
                    SurfaceView surfaceView = findViewById(R.id.surface);
                    surfaceView.setVisibility(View.GONE);
                    surfaceView.setVisibility(View.VISIBLE);
                    break;
                }
                case R.id.bt_video_replay:{
                    SurfaceView surfaceView = findViewById(R.id.surface);
                    surfaceView.setVisibility(View.GONE);
                    surfaceView.setVisibility(View.VISIBLE);
                    break;
                }
                default:{
                    break;
                }
            }
        }
    };
}
