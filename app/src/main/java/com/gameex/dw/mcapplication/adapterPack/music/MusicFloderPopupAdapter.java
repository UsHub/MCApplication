package com.gameex.dw.mcapplication.adapterPack.music;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;
import com.gameex.dw.mcapplication.otherClass.MarqueeTextView;

import java.util.List;

public class MusicFloderPopupAdapter extends
        RecyclerView.Adapter<MusicFloderPopupAdapter.FloderPopupHolder> {
    private List<MusicInfo> mMusicInfos;
    private Context mContext;

    public MusicFloderPopupAdapter(List<MusicInfo> musicInfos,Context context){
        this.mMusicInfos=musicInfos;
        this.mContext=context;
    }

    @NonNull
    @Override
    public FloderPopupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext)
                .inflate(R.layout.floder_music_popup_item,null);
        return new FloderPopupHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FloderPopupHolder holder, int position) {
        MusicInfo musicInfo=mMusicInfos.get(position);
        holder.floderMusicName.setText(musicInfo.getTitle());
        holder.floderMusicSingerName.setText(musicInfo.getArtist());
    }

    @Override
    public int getItemCount() {
        return mMusicInfos.size();
    }

    class FloderPopupHolder extends RecyclerView.ViewHolder{
        private CheckBox floderMusicCheck;
        private MarqueeTextView floderMusicName;
        private TextView floderMusicSingerName;

        public FloderPopupHolder(View itemView) {
            super(itemView);
            floderMusicCheck=itemView.findViewById(R.id.floder_music_check);
            floderMusicName=itemView.findViewById(R.id.floder_music_name);
            floderMusicName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "播放音乐！", Toast.LENGTH_SHORT).show();
                }
            });
            floderMusicSingerName=itemView.findViewById(R.id.floder_music_singer_name);
        }
    }
}
