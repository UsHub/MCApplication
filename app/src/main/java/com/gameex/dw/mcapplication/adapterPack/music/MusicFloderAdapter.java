package com.gameex.dw.mcapplication.adapterPack.music;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.activity.SongListActivity;
import com.gameex.dw.mcapplication.dataPack.music.MusicFloderGroup;
import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;

import java.util.HashMap;
import java.util.List;

public class MusicFloderAdapter extends RecyclerView.Adapter<MusicFloderAdapter.MusicFloderHolder> {
    private Context mContext;
    private List<MusicFloderGroup> mMusicFloderGroups;
    private LocalBroadcastManager mLocalBroadcastManager;
    private PopupWindow floderMusicPopupWindow;
    private LinearLayout mLinearLayout;
    public MusicFloderAdapter(Context context,List<MusicFloderGroup> musicFloderGroups){
        this.mContext=context;
        this.mMusicFloderGroups=musicFloderGroups;
        mLinearLayout=SongListActivity.getLinearLayout();
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(context);
    }

    @Override
    public MusicFloderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.music_floder_item,null);
        return new MusicFloderHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicFloderHolder holder, int position) {
        MusicFloderGroup musicFloderGroup=mMusicFloderGroups.get(position);
        holder.floderName.setText(musicFloderGroup.getFloderName());
        holder.musicCount.setText(String.valueOf(musicFloderGroup.getMusicCount()));

    }

    @Override
    public int getItemCount() {
        return mMusicFloderGroups.size();
    }

    class MusicFloderHolder extends RecyclerView.ViewHolder{
        private TextView floderName,musicCount;

        public MusicFloderHolder(View itemView) {
            super(itemView);
            floderName=itemView.findViewById(R.id.song_floder_name);
            floderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFloderPopup(SongListActivity.getmMusicMap().get(
                            mMusicFloderGroups.get(getAdapterPosition()).getFloderName()));
                }
            });
            musicCount=itemView.findViewById(R.id.floder_song_count);
        }
    }

    /** 音乐文件夹中的音乐列表弹出框 */
    private void getFloderPopup(List<MusicInfo> musicInfos){
        View floderMusicPopupView= LayoutInflater.from(mContext)
                .inflate(R.layout.music_floder_popup_main,null);
        Button button=floderMusicPopupView.findViewById(R.id.floder_music_sure_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "确定！", Toast.LENGTH_SHORT).show();
            }
        });
        RecyclerView floderMusicRecycler=floderMusicPopupView
                .findViewById(R.id.music_floder_popup_recycler);
        floderMusicRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        floderMusicRecycler.addItemDecoration(new DividerItemDecoration
                (mContext,DividerItemDecoration.VERTICAL));
        MusicFloderPopupAdapter musicFloderPopupAdapter=new MusicFloderPopupAdapter(
                musicInfos,mContext);
        floderMusicRecycler.setAdapter(musicFloderPopupAdapter);
        floderMusicPopupWindow=new PopupWindow(floderMusicPopupView
                ,ViewGroup.LayoutParams.MATCH_PARENT,900);
        floderMusicPopupWindow.setAnimationStyle(R.style.AnimationFloderMusic);
        floderMusicPopupWindow.setBackgroundDrawable(new ColorDrawable(
                0xe0000000));
        floderMusicPopupWindow.setFocusable(true);
        floderMusicPopupWindow.setOutsideTouchable(true);
        floderMusicPopupWindow.update();
        floderMusicPopupWindow.showAtLocation(mLinearLayout
                , Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,50);
    }
}
