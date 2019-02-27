package com.gameex.dw.mcapplication.dataPack.music;

import java.io.Serializable;

public class MusicInfo implements Serializable{

    int num;
    long id;
    String title;
    String artist;
    long duration;
    long size;
    String url;

    public MusicInfo(int num, long id, String title, String artist
            , long duration, long size, String url) {
        this.num = num;
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.size = size;
        this.url = url;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
