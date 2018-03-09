package com.thunder.ktv.androidboardtest.player;

import android.util.Log;
import android.view.SurfaceHolder;

import com.thunder.ktv.androidboardtest.player.listener.IThunderPlayerListener;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.MediaInfo;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

/**
 * Created by chengkai on 18-1-17.
 */

public class THPlayer implements IThunderPlayerListener {
    private static final String TAG = "THPlayer";
    IjkMediaPlayer ijkMediaPlayer = null;

    private IThunderPlayerListener iThunderPlayerListener = null;
    public THPlayer(IThunderPlayerListener iThunderPlayerListener) {
        this.iThunderPlayerListener = iThunderPlayerListener;
    }

    private IjkMediaPlayer creatPlayer()
    {
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        setPlayer(ijkMediaPlayer);
        return ijkMediaPlayer;
    }

    private void setPlayer(IjkMediaPlayer ijkMediaPlayer)
    {
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_ERROR);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "soundtouch", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-all-videos", 1); /*open hw codec*/
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);

        ijkMediaPlayer.setOnPreparedListener(this);
        ijkMediaPlayer.setOnCompletionListener(this);
        ijkMediaPlayer.setOnErrorListener(this);
//        ijkMediaPlayer.setLooping(true);
    }

    private String curPath = null;
    private SurfaceHolder curSurfaceHolder = null;
    public void play(String path,SurfaceHolder surfaceHolder) throws IOException {
        if(path == null)
            return;
        curPath = path;
        curSurfaceHolder = surfaceHolder;
        if(ijkMediaPlayer != null){
            release(ijkMediaPlayer);
            ijkMediaPlayer = null;
        }
        ijkMediaPlayer = creatPlayer();
        ijkMediaPlayer.setDataSource(path);
        ijkMediaPlayer.setDisplay(surfaceHolder);
        ijkMediaPlayer.prepareAsync();
    }
    public void stop()
    {
        if(ijkMediaPlayer != null){
            release(ijkMediaPlayer);
            ijkMediaPlayer = null;
        }
    }

    public int audio_select()
    {
        if(ijkMediaPlayer == null)
            return -1;
        int audio_count = 0, index = 0;
        int[] audio_index = {-1, -1};

        ITrackInfo[] trackInfos = ijkMediaPlayer.getTrackInfo();
        if(trackInfos == null)
            return -1;

        for(ITrackInfo trackInfo : trackInfos) {
            if(ITrackInfo.MEDIA_TRACK_TYPE_AUDIO == trackInfo.getTrackType()){
                if(audio_count >= 2)
                    continue;
                audio_index[audio_count] = index;
                audio_count++;
            }
            index++;
        }

        if(audio_count < 1)
            return -1;

        int selAudioIndex = ijkMediaPlayer.getSelectedTrack(ITrackInfo.MEDIA_TRACK_TYPE_AUDIO);

        Log.e(TAG,"sel audio:" + selAudioIndex + ", (" + audio_index[0] + " " + audio_index[1] + ")");

        if(audio_index[0] == selAudioIndex){
            ijkMediaPlayer.selectTrack(audio_index[1]);
        } else{
            ijkMediaPlayer.selectTrack(audio_index[0]);
        }
        return selAudioIndex;
    }
    float volume = 60;
    public void setVolume(float vol)
    {
        volume = vol;
        ijkMediaPlayer.setVolume(vol/100,vol/100);
    }
    public void resume()
    {
        ijkMediaPlayer.start();
    }
    public void pause()
    {
        ijkMediaPlayer.pause();
    }

    public void release(IjkMediaPlayer ijkMediaPlayer){
        if(ijkMediaPlayer == null)
            return;
        ijkMediaPlayer.stop();
        ijkMediaPlayer.reset();
        ijkMediaPlayer.release();
    }

    public static String getMediaInfo(MediaInfo mediaInfo)
    {
        if(mediaInfo == null)
            return new String();
        return String.format("[%s]:Video{%s,%s},Audio{%s,%s}",
                mediaInfo.mMediaPlayerName,
                mediaInfo.mVideoDecoder,
                mediaInfo.mVideoDecoderImpl,
                mediaInfo.mAudioDecoder,
                mediaInfo.mAudioDecoderImpl);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        Log.d(TAG, "onPrepared: ");
        if(!iMediaPlayer.isPlaying()){
            iMediaPlayer.start();
            setVolume(volume);
            MediaInfo mediaInfo = iMediaPlayer.getMediaInfo();
            Log.d(TAG, "onPrepared: mVideoDecoder " + getMediaInfo(mediaInfo));
        }
        if(iThunderPlayerListener!=null){
            iThunderPlayerListener.onPrepared(iMediaPlayer);
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        Log.d(TAG, "onCompletion: ");
        if(iThunderPlayerListener != null){
            iThunderPlayerListener.onCompletion(iMediaPlayer);
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        Log.e(TAG, "onError: ");
        if(iThunderPlayerListener != null){
            iThunderPlayerListener.onError(iMediaPlayer,i,i1);
        }
        return false;
    }
}
