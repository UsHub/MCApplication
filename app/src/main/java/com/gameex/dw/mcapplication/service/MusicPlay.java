package com.gameex.dw.mcapplication.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.gameex.dw.mcapplication.activity.MainActivity;
import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;
import com.gameex.dw.mcapplication.interFace.MusicPlayInterface;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlay extends Service implements MediaPlayer.OnCompletionListener {

    private Messenger mMessenger;
    private MediaPlayer mPlayer;
    private Timer mTimer;
    private List<MusicInfo> mMusicInfos;
    private int mCurrentPosition;   //记录当前播放歌曲位置
    private MusicInfoReceiver mMusicInfoReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;

    public MusicPlay() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);

        mMusicInfoReceiver = new MusicInfoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("MUSICINFO");
        mLocalBroadcastManager.registerReceiver(mMusicInfoReceiver, intentFilter);
        mPlayer = new MediaPlayer();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mMessenger==null){
            mMessenger = (Messenger) intent.getExtras().get("play");
        }
        return super.onStartCommand(intent, START_REDELIVER_INTENT, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return new MusicControlInterface();
    }

    //播放音乐
    public void playMusic(int position) {
        if (position < 0) {
            position = mMusicInfos.size() - 1;
            mCurrentPosition = position;
        } else if (position > mMusicInfos.size() - 1) {
            position = 0;
            mCurrentPosition = 0;
        }
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
                mPlayer.setOnCompletionListener(this);
            }
            mPlayer.reset();    //重置
            mPlayer.setDataSource(mMusicInfos.get(position).getUrl());      //加载多媒体文件
            mPlayer.prepare();
            mPlayer.start();
            addTimer();
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(this, "播放错误！", Toast.LENGTH_SHORT).show();
        }
    }

    //暂停播放
    public void pauseMusic() {
        mPlayer.pause();
    }

    //继续播放
    public void continueMusic() {
        mPlayer.start();
    }

    //设置进度条位置
    public void seekTo(int progress) {
        mPlayer.seekTo(progress);
    }

    //设置播放进度
    public void addTimer() {
        if (mTimer == null) {
            mTimer = new Timer(); //创建计时器对象
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int duration = mPlayer.getDuration(); //获取歌曲总时长
                    int currentPosition = mPlayer.getCurrentPosition();
                    Message message = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    message.setData(bundle);
                    try {
                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }, 5, 500);   //开始计时任务后的5毫秒，第一次执行run,以后每500毫秒执行一次
        }
    }

    //播放完成监听
    @Override
    public void onCompletion(MediaPlayer mp) {
//        Toast.makeText(MainActivity.getContext(), "播放完毕！", Toast.LENGTH_SHORT).show();
//        playMusic(++mCurrentPosition);
    }

    /** 实现音乐接口的音乐控制类 */
    class MusicControlInterface extends Binder implements MusicPlayInterface {

        @Override
        public void playMusic(int position) {
            MusicPlay.this.playMusic(position);
        }

        @Override
        public void pauseMusic() {
            MusicPlay.this.pauseMusic();
        }

        @Override
        public void continueMusic() {
            MusicPlay.this.continueMusic();
        }

        @Override
        public void seekTo(int progress) {
            MusicPlay.this.seekTo(progress);
        }

        @Override
        public boolean isPlaying() {
            if (mPlayer.isPlaying())
                return true;
            else
                return false;
        }
    }

    public class MusicInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mMusicInfos = (List<MusicInfo>) intent.getSerializableExtra("getMusicInfo");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
        mLocalBroadcastManager.unregisterReceiver(mMusicInfoReceiver);
    }
}
