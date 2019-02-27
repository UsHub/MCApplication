package com.gameex.dw.mcapplication.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;

import com.gameex.dw.mcapplication.dataPack.photo.PhotoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhotoLoad extends Service {
    private HashMap<String ,List<PhotoInfo>> mPhotoMap;
    private List<PhotoInfo> mPhotoInfos;
    private LocalBroadcastManager mLocalBroadcastManager;
    /**projection,体现要查询返回的列*/
    private String[] projection = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
    };
    /**查询的，where子句*/
    private String where = MediaStore.Images.Media.MIME_TYPE+"=? or "
            + MediaStore.Images.Media.MIME_TYPE+"=?";
    /**查询条件的属性值*/
    private String[] whereArgs = {"image/jpeg","image/png"};

    public PhotoLoad() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPhotoMap=new HashMap<String ,List<PhotoInfo>>();
        mPhotoInfos=new ArrayList<PhotoInfo>();
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Cursor mCursor=this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,where,whereArgs, MediaStore.Images.Media.DATE_MODIFIED+" desc");
        while (mCursor.moveToNext()){
            Long id=mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media._ID));
            String name=mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.TITLE));
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String parentPath=new File(path).getParentFile().getName();
            if(path!=null && path.length()>0){
                PhotoInfo photoInfo=new PhotoInfo(id,name,path);
                mPhotoInfos.add(photoInfo);
                if (!mPhotoMap.containsKey(parentPath)){
                    List<PhotoInfo> childList=new ArrayList<>();
                    PhotoInfo childPhoto=new PhotoInfo(id,name,path);
                    childList.add(childPhoto);
                    mPhotoMap.put(parentPath,childList);
                }else{
                    mPhotoMap.get(parentPath).add(new PhotoInfo(id,name,path));
                }
            }
            //添加全部图片的集合
            mPhotoMap.put("全部图片",mPhotoInfos);
        }
        Intent intent1=new Intent();
        intent1.setAction("com.gameex.dw.mcapplication.activity.PHOTOMAP");
        intent1.putExtra("PhotoMap",mPhotoMap);
        mLocalBroadcastManager.sendBroadcast(intent1);
        mCursor.close();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
