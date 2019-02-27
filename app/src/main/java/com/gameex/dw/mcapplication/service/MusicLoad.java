package com.gameex.dw.mcapplication.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;

import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;
import com.gameex.dw.mcapplication.interFace.MusicLoadInterface;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MusicLoad extends Service {
    private List<MusicInfo> musicInfos;
    private HashMap<String,List<MusicInfo>> mMusicMap;
    private LocalBroadcastManager mLocalBroadcastManager;

    public MusicLoad() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicInfos=new ArrayList<>();
        mMusicMap=new HashMap<>();
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LoadControl();
    }

    /** 从设备中获取歌曲信息，保存在List中
    * @return
    */
    public HashMap<String,List<MusicInfo>> getMusicMap(Context context){
        Cursor cursor=context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        int num=1;
        while (cursor.moveToNext()){
            int isMusic=cursor.getInt(      //是否为音乐
                    cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if(isMusic!=0){
                long id=cursor.getLong(     //音乐ID
                        cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title=cursor.getString(    //音乐标题
                        cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist=cursor.getString(     //艺术家
                        cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                long duration=cursor.getLong(       //音乐时长
                        cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                long size=cursor.getLong(       //文件大小
                        cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                String url=cursor.getString(        //文件路径
                        cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String parentPath=new File(url).getParentFile().getName();
                MusicInfo musicInfo=new MusicInfo(num,id,title,artist,duration,size,url);
                musicInfos.add(musicInfo);
                if (!mMusicMap.containsKey(parentPath)){
                    List<MusicInfo> childList=new ArrayList<>();
                    MusicInfo childMusic=new MusicInfo(num,id,title,artist,duration,size,url);
                    childList.add(childMusic);
                    mMusicMap.put(parentPath,childList);
                }else{
                    mMusicMap.get(parentPath).add(new MusicInfo(num,id,title,artist,duration,size,url));
                }
            }
            mMusicMap.put("全部歌曲",musicInfos);
            num++;
        }
        Intent intent=new Intent();
        intent.setAction("MUSICINFO");
        intent.putExtra("getMusicInfo",(Serializable)musicInfos);
        mLocalBroadcastManager.sendBroadcast(intent);
        cursor.close();
        return mMusicMap;
    }

    /** 往List集合中添加Map对象数据，每一个Map对象存放一首音乐的所有属性
     * @param musicInfos
     * @return
     */
    public List<HashMap<String,String>> getMusicMap(List<MusicInfo> musicInfos){
        List<HashMap<String,String>> musicMap=new ArrayList<HashMap<String, String>>();

        int i=0;    //定义歌曲序列
        for (Iterator iterator=musicInfos.iterator(); iterator.hasNext();){
            i++;
            MusicInfo musicInfo=(MusicInfo)iterator.next();
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("number",String.valueOf(i));
            map.put("id",String.valueOf(musicInfo.getId()));
            map.put("title",musicInfo.getTitle());
            map.put("artist",musicInfo.getArtist());
            map.put("duration",formatTime(musicInfo.getDuration()));
            map.put("size",String.valueOf(musicInfo.getSize()));
            map.put("url",musicInfo.getUrl());
            musicMap.add(map);
        }
        return musicMap;
    }

    /**
     * 格式化时间，将毫秒转换为分：秒格式
     * @param time
     * @return
     */
    public String formatTime(long time){
        String minute=time/(1000*60)+"";
        String second=time%(1000*60)+"";
        if (minute.length()<2){
            minute="0"+minute;
        }
        if(second.length()==4){
            second="0"+second;
        }else if (second.length()==3){
            second="00"+second;
        }else if (second.length()==2){
            second="000"+second;
        }else if(second.length()==1){
            second="0000"+second;
        }
        return minute+":"+second.trim().substring(0,2);
    }

    class LoadControl extends Binder implements MusicLoadInterface {

        @Override
        public HashMap<String,List<MusicInfo>> getMusicMap(Context context) {
            return MusicLoad.this.getMusicMap(context);
        }

        @Override
        public List<HashMap<String, String>> getMusicMap(List<MusicInfo> musicInfos) {
            return MusicLoad.this.getMusicMap(musicInfos);
        }

        @Override
        public String formatTime(Long Time) {
            return MusicLoad.this.formatTime(Time);
        }
    }
}
