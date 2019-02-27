package com.gameex.dw.mcapplication.otherClass;

import com.gameex.dw.mcapplication.dataPack.music.MusicFloderGroup;
import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;
import com.gameex.dw.mcapplication.dataPack.photo.PhotoGroup;
import com.gameex.dw.mcapplication.dataPack.photo.PhotoInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataGroup {

    /**
     * 组装分组popupWindow数据源
     * 遍历HashMap将数据组装成List
     * 展示图库分类
     * @param mPhotoMap
     * @return
     */
    public static List<PhotoGroup> subGroupOfPhoto(HashMap<String, List<PhotoInfo>> mPhotoMap){
        if (mPhotoMap.size()==0){
            return null;
        }
        //遍历
        List<PhotoGroup> photoGroupList=new ArrayList<>();
        Iterator<Map.Entry<String,List<PhotoInfo>>> iterator=mPhotoMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,List<PhotoInfo>> entry=iterator.next();
            PhotoGroup photoGroup=new PhotoGroup();
            //根据key获取其中图片list
            String key=entry.getKey();
            List<PhotoInfo> value=entry.getValue();

            photoGroup.setFloderPath(key);//获取该组件文件夹名
            photoGroup.setPhotoCount(value.size());//获取该组图片数量
            photoGroup.setTopPhotoPath(value.get(0).getUrl());//获取该组的第一张图片
            //将全部图片放在第一位置
            if(photoGroup.getFloderPath().equals("全部图片")){
                photoGroupList.add(0,photoGroup);
            }else {
                photoGroupList.add(photoGroup);
            }
        }
        return photoGroupList;
    }

    /**
     * 组装分组popupWindow数据源
     * 遍历HashMap将数据组装成List
     * 展示图库分类
     * @param mMusicFloderMap
     * @return
     */
    public static List<MusicFloderGroup> subGroupOfMusic(HashMap<String,List<MusicInfo>> mMusicFloderMap){
        if (mMusicFloderMap.size()==0){
            return null;
        }
        //遍历
        List<MusicFloderGroup> musicFloderGroupList=new ArrayList<>();
        Iterator<Map.Entry<String,List<MusicInfo>>> iterator=mMusicFloderMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String,List<MusicInfo>> entry=iterator.next();
            MusicFloderGroup musicFloderGroup=new MusicFloderGroup();
            //根据key获取其中音乐list
            String key=entry.getKey();
            List<MusicInfo> value=entry.getValue();

            musicFloderGroup.setFloderName(key);//获取该组件文件夹名
            musicFloderGroup.setMusicCount(value.size());//获取该组图片数量
            musicFloderGroup.setFirstMusicPath(value.get(0).getUrl());//获取该组的第一张图片
            //将全部音乐放在第一位置
            if(musicFloderGroup.getFloderName().equals("全部图片")){
                musicFloderGroupList.add(0,musicFloderGroup);
            }else {
                musicFloderGroupList.add(musicFloderGroup);
            }
        }
        return musicFloderGroupList;
    }
}
