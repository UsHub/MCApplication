package com.gameex.dw.mcapplication.interFace;

import android.content.Context;

import com.gameex.dw.mcapplication.dataPack.photo.PhotoInfo;

import java.util.List;

public interface PhotoLoadInterface {
    List<PhotoInfo> getPhotoInfos(Context context);
}
