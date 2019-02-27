package com.gameex.dw.mcapplication.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.dataPack.SerMap;
import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;
import com.gameex.dw.mcapplication.dataPack.photo.PhotoInfo;
import com.gameex.dw.mcapplication.interFace.MusicLoadInterface;
import com.gameex.dw.mcapplication.interFace.MusicPlayInterface;
import com.gameex.dw.mcapplication.otherClass.GlideImageLoader;
import com.gameex.dw.mcapplication.otherClass.MarqueeTextView;
import com.gameex.dw.mcapplication.service.MusicLoad;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private static String ACTION_PHOTOMAP =
            "com.gameex.dw.mcapplication.activity.PHOTOMAP";
    private static String ACTION_MUSICLIST =
            "com.gameex.dw.mcapplication.activity.CLICKMUSICLIST";
    private static int PHOTOPATH_OK=10;
    private String[] mPermission;
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    private String[] permission = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private List<String> mPermissionList = new ArrayList<>();

    private int mCurrentPosition = 0;
    private boolean theFirstPlay = true;
    private ServiceConn mServiceConn;
    private MusicPlayInterface mInterface;
    private Intent intent;
    private Banner mBanner;
    private ImageView albumImage;
    private static SeekBar sSeekBar;
    private static TextView proSeek, totalSeek;
    private MarqueeTextView musicTitle;
    private LinearLayout bannerType;
    private ImageButton proButton, playButton, nextButton, photoMenu
            ,rotate,backToForce,accordian,cube;
    private Button choosePhoto,chooseMusic;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int duration = bundle.getInt("duration");
            int currentPosition = bundle.getInt("currentPosition");
            if (duration / 1000 <= currentPosition / 1000) {
                mInterface.playMusic(++mCurrentPosition);
                musicTitle.setText(mMusicInfos.get(mCurrentPosition).getTitle()
                        + " - " + mMusicInfos.get(mCurrentPosition).getArtist());
            } else {
                seekTime(duration, currentPosition);
            }
        }
    };
    private Intent pIntent;
    private HashMap<String, List<PhotoInfo>> mPhotoMap;
    private List<PhotoInfo> mPhotoInfos;
    private String[] path;

    private MusicConn mMusicConn;
    private MusicLoadInterface mMusicLoad;
    private Intent mIntent;
    private List<MusicInfo> mMusicInfos;
    private HashMap<String,List<MusicInfo>> mMusicMap;

    private InfoColReceiver mInfoColReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;

    private SerMap mSerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();   //隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);    //隐藏状态栏

        sContext = getApplicationContext();

        doRequest();

        initView();

    }

    /**
     * 权限请求
     */
    private void doRequest() {

        mPermissionList.clear();
        for (int i = 0; i < permission.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    permission[i]) !=
                    PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission[i]);
            }
        }
        if (mPermissionList.isEmpty()) { //为空则已经全部授权
            startDataBuild();
        } else {
            mPermission = mPermissionList.toArray(
                    new String[mPermissionList.size()]);//将List转化为数组
            ActivityCompat.requestPermissions(MainActivity.this,
                    mPermission, 2);
        }
    }

    /**
     * UI绑定,注册广播
     */
    private void initView() {
        mBanner = findViewById(R.id.play_image);
        albumImage = findViewById(R.id.album_image);
        albumImage.setOnClickListener(this);

        sSeekBar = findViewById(R.id.mc_seekbar);
        sSeekBar.setOnSeekBarChangeListener(this);
        proSeek = findViewById(R.id.pro_seek);
        totalSeek = findViewById(R.id.total_seek);
        musicTitle = findViewById(R.id.main_musicTitle);
        musicTitle.setOnClickListener(this);

        proButton = findViewById(R.id.pro_music);
        proButton.setOnClickListener(this);
        playButton = findViewById(R.id.play_music);
        playButton.setOnClickListener(this);
        nextButton = findViewById(R.id.ne_music);
        nextButton.setOnClickListener(this);
        rotate=findViewById(R.id.rotate_banner);
        rotate.setOnClickListener(this);
        backToForce=findViewById(R.id.back_to_force_banner);
        backToForce.setOnClickListener(this);
        accordian=findViewById(R.id.accordian_banner);
        accordian.setOnClickListener(this);
        cube=findViewById(R.id.cube_banner);
        cube.setOnClickListener(this);

        bannerType=findViewById(R.id.banner_type);
        choosePhoto=findViewById(R.id.choose_photo);
        choosePhoto.setOnClickListener(this);
        chooseMusic=findViewById(R.id.choose_music);
        chooseMusic.setOnClickListener(this);

        mPhotoMap=new HashMap<>();
        mPhotoInfos=new ArrayList<>();
        mMusicMap=new HashMap<>();
        mMusicInfos=new ArrayList<>();
        mSerMap=new SerMap();

        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.gameex.dw.mcapplication.activity.CLICKMUSICLIST");
        intentFilter.addAction("com.gameex.dw.mcapplication.activity.PHOTOMAP");
        mInfoColReceiver = new InfoColReceiver();
        mLocalBroadcastManager.registerReceiver(mInfoColReceiver, intentFilter);
    }

    /**
     * 启动服务，加载数据
     */
    private void startDataBuild() {

        //获取音乐数据
        mIntent = new Intent(this, MusicLoad.class);
        startService(mIntent);
        mMusicConn = new MusicConn();
        bindService(mIntent, mMusicConn, BIND_AUTO_CREATE);

        //音乐控制服务
        intent = new Intent(MainActivity.this,
                com.gameex.dw.mcapplication.service.MusicPlay.class);
        Messenger messenger = new Messenger(mHandler);
        intent.putExtra("play", messenger);
        startService(intent);
        mServiceConn = new ServiceConn();
        bindService(intent, mServiceConn, BIND_AUTO_CREATE);
    }

    /**
     * 进度条时间更新
     */
    private static void seekTime(int duration, int currentPosition) {
        sSeekBar.setMax(duration);
        sSeekBar.setProgress(currentPosition);
        //歌曲当前时长
        int minute = currentPosition / 1000 / 60;
        int second = currentPosition / 1000 % 60;
        String strMinute = null;
        String strSecond = null;
        if (minute < 10) {
            strMinute = "0" + minute;
        } else {
            strMinute = minute + "";
        }
        if (second < 10) {
            strSecond = "0" + second;
        } else {
            strSecond = second + "";
        }
        proSeek.setText(strMinute + ":" + strSecond);
        //歌曲总时长
        minute = duration / 1000 / 60;
        second = duration / 1000 % 60;
        if (minute < 10) {
            strMinute = "0" + minute;
        } else {
            strMinute = minute + "";
        }
        if (second < 10) {
            strSecond = "0" + second;
        } else {
            strSecond = second + "";
        }
        totalSeek.setText(strMinute + ":" + strSecond);
    }

    /**
     * 进度条监听事件
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        mInterface.seekTo(progress);
    }

    /**
     * 服务链接
     */
    class ServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mInterface = (MusicPlayInterface) service; //获得中间对象
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    class MusicConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMusicLoad = (MusicLoadInterface) service;
            mMusicMap = mMusicLoad.getMusicMap(MainActivity.this);
            if (mMusicMap.size() != 0) {
                mSerMap.setMusicMap(mMusicMap);
                mMusicInfos=mMusicMap.get("全部歌曲");
                unbindService(mMusicConn);
                stopService(mIntent);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    /** 按钮监听事件 */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rotate_banner:
                mBanner.setBannerAnimation(Transformer.RotateDown);
                break;
            case R.id.back_to_force_banner:
                mBanner.setBannerAnimation(Transformer.BackgroundToForeground);
                break;
            case R.id.accordian_banner:
                mBanner.setBannerAnimation(Transformer.Accordion);
                break;
            case R.id.cube_banner:
                mBanner.setBannerAnimation(Transformer.CubeIn);
                break;
            case R.id.choose_photo:
                Intent photoMapIntent = new Intent(MainActivity.this,
                        PhotoListActivity.class);
                startActivityForResult(photoMapIntent,1);
                break;
            case R.id.choose_music:
                Intent intent = new Intent(this, SongListActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("SERMAP",mSerMap);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.album_image:
                Toast.makeText(sContext, "The Album ！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pro_music:
                if (mCurrentPosition == 0)
                    playButton.setImageResource(R.drawable.pause);
                mInterface.playMusic(--mCurrentPosition);
                if (mCurrentPosition < 0)
                    mCurrentPosition = mMusicInfos.size() - 1;
                musicTitle.setText(mMusicInfos.get(mCurrentPosition).getTitle()
                        + " - " + mMusicInfos.get(mCurrentPosition).getArtist());
                break;
            case R.id.play_music:
                if (mCurrentPosition == 0 && theFirstPlay) {
                    mInterface.playMusic(mCurrentPosition);
                    playButton.setImageResource(R.drawable.pause);
                    musicTitle.setText(mMusicInfos.get(mCurrentPosition).getTitle()
                            + " - " + mMusicInfos.get(mCurrentPosition).getArtist());
                    theFirstPlay = false;
                } else {
                    if (mInterface.isPlaying()) {
                        playButton.setImageResource(R.drawable.play);
                        mInterface.pauseMusic();
                    } else {
                        playButton.setImageResource(R.drawable.pause);
                        mInterface.continueMusic();
                    }
                }
                break;
            case R.id.ne_music:
                if (mCurrentPosition == 0)
                    playButton.setImageResource(R.drawable.pause);
                mInterface.playMusic(++mCurrentPosition);
                if (mCurrentPosition > mMusicInfos.size() - 1)
                    mCurrentPosition = 0;
                musicTitle.setText(mMusicInfos.get(mCurrentPosition).getTitle()
                        + " - " + mMusicInfos.get(mCurrentPosition).getArtist());
                break;
            case R.id.main_musicTitle:
                Toast.makeText(sContext, "The Music !", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 音乐试听
     *
     * @prama mCurrentPosition
     * @return
     */
    public class InfoColReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_MUSICLIST)) {
                mCurrentPosition = intent.getExtras().getInt("CLICK_POSITION");
                mInterface.playMusic(mCurrentPosition);
                playButton.setImageResource(R.drawable.pause);
                musicTitle.setText(mMusicInfos.get(mCurrentPosition).getTitle()
                        + " - " + mMusicInfos.get(mCurrentPosition).getArtist());
            }
        }
    }

    /** 权限请求操作 */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                int i=0;
                for (; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选 禁止后不在询问
                        boolean showRequestPermission =
                                ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                                        permission[i]);
                        if (showRequestPermission) {
                            Toast.makeText(sContext, "不在询问！", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(sContext, "下次询问！", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                if (i==grantResults.length){
                    startDataBuild();
                    Toast.makeText(sContext, "授权成功！", Toast.LENGTH_SHORT).show();
                }else{
                    ActivityCompat.requestPermissions(MainActivity.this,
                            mPermission, 2);
                    Toast.makeText(sContext, "请完成授权！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /** 接收并处理其他activity返回的数据 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==PHOTOPATH_OK){
            Bundle bundle=data.getExtras();
            path=bundle.getStringArray("PhotoPath");
            mBanner.setImages(Arrays.asList(path))
                    .setImageLoader(new GlideImageLoader())
                    .setDelayTime(2000)
                    .start();
            bannerType.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConn);
        stopService(intent);

        mLocalBroadcastManager.unregisterReceiver(mInfoColReceiver);
        finish();
    }
}
