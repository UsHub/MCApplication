package com.gameex.dw.mcapplication.adapterPack.photo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.dataPack.photo.PhotoGroup;

import java.util.List;

public class PhotoPopupAdapter extends RecyclerView.Adapter<PhotoPopupAdapter.PhotoPopupHolder> {
    private Context mContext;
    private List<PhotoGroup> mPhotoGroups;
    private LocalBroadcastManager mLocalBroadcastManager;

    public PhotoPopupAdapter(Context context,List<PhotoGroup> photoGroups){
        this.mContext=context;
        this.mPhotoGroups=photoGroups;
        mLocalBroadcastManager=LocalBroadcastManager.getInstance(context);
    }

    @Override
    public PhotoPopupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.photo_popup_window_item,null);
        return new PhotoPopupHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoPopupHolder holder, int position) {
        PhotoGroup photoGroup=mPhotoGroups.get(position);
        Glide.with(mContext)
                .load(photoGroup.getTopPhotoPath())
                .centerCrop()
                .override(80,80)
                .into(holder.floderFirstImage);
        holder.floderName.setText(photoGroup.getFloderPath());
        holder.floderImageCount.setText(photoGroup.getPhotoCount()+"张");
    }

    @Override
    public int getItemCount() {
        return mPhotoGroups.size();
    }

    class PhotoPopupHolder extends RecyclerView.ViewHolder{
        ImageView floderFirstImage;
        TextView floderName,floderImageCount;

        public PhotoPopupHolder(View itemView) {
            super(itemView);
            floderFirstImage=itemView.findViewById(R.id.photo_floder_first_image);
            floderName=itemView.findViewById(R.id.photo_floder_name);
            floderName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    intent.setAction("PHOTOLIST_ACTION");
                    intent.putExtra("PhotoList",
                            mPhotoGroups.get(getAdapterPosition()).getFloderPath());
                    mLocalBroadcastManager.sendBroadcast(intent);
                }
            });
            floderImageCount=itemView.findViewById(R.id.photo_floder_image_count);
        }
    }
}
