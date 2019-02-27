package com.gameex.dw.mcapplication.interFace;

import android.content.Context;

import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;

import java.util.HashMap;
import java.util.List;

public interface MusicLoadInterface {
    HashMap<String,List<MusicInfo>> getMusicMap(Context context);
    List<HashMap<String,String>> getMusicMap(List<MusicInfo> musicInfos);
    String formatTime(Long Time);
}
