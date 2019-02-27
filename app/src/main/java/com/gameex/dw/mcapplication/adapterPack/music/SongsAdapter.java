package com.gameex.dw.mcapplication.adapterPack.music;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.dataPack.music.MusicFloderGroup;
import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;
import com.gameex.dw.mcapplication.otherClass.MarqueeTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongsAdapter
        extends RecyclerView.Adapter<SongsAdapter.MusicHolder>{

    private List<MusicInfo> mMusicInfos;
    private Context mContext;
    private List<MusicInfo> uMusicInfos=new ArrayList<MusicInfo>();
    private LocalBroadcastManager mLocalBroadcastManager;

    public SongsAdapter(List<MusicInfo> musicInfos, Context context){
        this.mMusicInfos=musicInfos;
        this.mContext=context;
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(context);
    }

    public class MusicHolder extends RecyclerView.ViewHolder{
//        private CardView mCardView;
        private ImageView checkedImage;
        private TextView numTextView;
        private MarqueeTextView songTextView;
        private MarqueeTextView singerTextView;

        public MusicHolder(View itemView) {
            super(itemView);
//            mCardView=itemView.findViewById(R.id.music_card);
//            mCardView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, String.valueOf(getAdapterPosition())+
//                            "--"+mMusicInfos.get(getAdapterPosition()).getUrl(),
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
            //选中后，显示chckedImage图片
            checkedImage=itemView.findViewById(R.id.checked_image);
            numTextView=itemView.findViewById(R.id.song_num);
            numTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uMusicInfos.size()==0) {
                        if (checkedImage.getVisibility()==View.INVISIBLE){
                            uMusicInfos.add(mMusicInfos.get(getAdapterPosition()));
                            checkedImage.setVisibility(View.VISIBLE);
                        }
                    }else{
                        int i=0;
                        for (;i<uMusicInfos.size();i++) {
                            if (uMusicInfos.get(i) == mMusicInfos.get(getAdapterPosition())) {
                                uMusicInfos.remove(i);
                                i--;
                                checkedImage.setVisibility(View.INVISIBLE);
                                break;
                            }
                        }
                        if (i==uMusicInfos.size()) {
                            uMusicInfos.add(mMusicInfos.get(getAdapterPosition()));
                            checkedImage.setVisibility(View.VISIBLE);
                        }
                    }
//                    ListIterator<MusicInfo> musicInfoIterator=uMusicInfos.listIterator();
//                    if (uMusicInfos.size()==0) {
//                        if (checkedImage.getVisibility()==View.INVISIBLE){
//                            uMusicInfos.add(mMusicInfos.get(getAdapterPosition()));
//                            checkedImage.setVisibility(View.VISIBLE);
//                        }
//                    }else{
//                        while (musicInfoIterator.hasNext()) {
//                            if (musicInfoIterator.next() == mMusicInfos.get(getAdapterPosition())) {
//                                musicInfoIterator.remove();
//                                checkedImage.setVisibility(View.INVISIBLE);
//                                break;
//                            } else {
//                                if (!musicInfoIterator.hasNext()) {
//                                    uMusicInfos.add(mMusicInfos.get(getAdapterPosition()));
//                                    checkedImage.setVisibility(View.VISIBLE);
//                                } else {
//                                    continue;
//                                }
//                            }
//                        }
//                    }
                }
            });
            //点击播放
            songTextView=itemView.findViewById(R.id.song_singer);
            songTextView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setAction("com.gameex.dw.mcapplication.activity.CLICKMUSICLIST");
                    intent.putExtra("CLICK_POSITION",getAdapterPosition());
                    mLocalBroadcastManager.sendBroadcast(intent);
                }
            });
            singerTextView=itemView.findViewById(R.id.text_artist);
        }
    }

    @Override
    public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View musicView=LayoutInflater.from(mContext).inflate(R.layout.songs_item,null);
        return new MusicHolder(musicView);
    }

    @Override
    public void onBindViewHolder(MusicHolder holder, int position) {
        MusicInfo musicInfo=mMusicInfos.get(position);
        holder.numTextView.setText(String.valueOf(musicInfo.getNum()));
        holder.songTextView.setText(musicInfo.getTitle());
        holder.singerTextView.setText("-"+musicInfo.getArtist());
        int i;
        for (i=0;i<uMusicInfos.size();i++)
            if (uMusicInfos.get(i).getNum()==musicInfo.getNum()){
                holder.checkedImage.setVisibility(View.VISIBLE);
                break;
            }
        if (i==uMusicInfos.size()) {
            holder.checkedImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mMusicInfos.size();
    }
}
