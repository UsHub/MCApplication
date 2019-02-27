package com.gameex.dw.mcapplication.dataPack.photo;

import java.io.Serializable;

public class PhotoInfo implements Serializable{
    private long id;
    private String name,url;

    public PhotoInfo(Long id,String name,String url){
        this.id=id;
        this.name=name;
        this.url=url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
