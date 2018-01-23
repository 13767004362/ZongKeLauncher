package com.zongke.hapilolauncher.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zongke.hapilolauncher.R;

import java.io.IOException;

/**
 * 背景音乐服务类
 * Created by llj on 2017/10/12.
 */

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;

    //通过binder实现了 调用者（client）与 service之间的通信
    private MusicBinder mBinder = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("llj", "onBind，开始音乐服务");
        if (mediaPlayer == null) {
            // R.raw.mmp是资源文件，MP3格式的
            mediaPlayer = MediaPlayer.create(this, R.raw.backgroud_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 当播放完毕了  有重新开始播放
                    // 循环播放
                    play();
                }
            });
            play();
        }
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    /**
     * 开始播放音乐
     */
    public void play() {
        if (mediaPlayer == null){
            return;
        }
        mediaPlayer.start();
    }

    /**
     * 停止音乐
     */
    public void stop() {
        if (mediaPlayer == null) return;
        // 停止播放
        mediaPlayer.stop();
        try {
            // 为下一次播放做准备
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 记录当前的播放位置、下一次播放的时候接着上一次播放
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
    }

    /**
     * 判断是否在播放音乐
     *
     * @return
     */
    public boolean isPlaying() {
        if (mediaPlayer == null) {return false;}
        return mediaPlayer.isPlaying();
    }

    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }
}
