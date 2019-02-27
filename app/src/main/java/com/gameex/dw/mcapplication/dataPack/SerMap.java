package com.gameex.dw.mcapplication.dataPack;

import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;
import com.gameex.dw.mcapplication.dataPack.photo.PhotoInfo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class SerMap implements Serializable{
    private HashMap<String,List<MusicInfo>> mMusicMap;
    private HashMap<String,List<PhotoInfo>> mPhotoMap;

    public HashMap<String, List<MusicInfo>> getMusicMap() {
        return mMusicMap;
    }

    public void setMusicMap(HashMap<String, List<MusicInfo>> musicMap) {
        mMusicMap = musicMap;
    }

    public HashMap<String, List<PhotoInfo>> getPhotoMap() {
        return mPhotoMap;
    }

    public void setPhotoMap(HashMap<String, List<PhotoInfo>> photoMap) {
        mPhotoMap = photoMap;
    }
}
