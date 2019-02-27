package com.gameex.dw.mcapplication.adapterPack.photo;

import android.content.Context;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.activity.PhotoBrowseActivity;
import com.gameex.dw.mcapplication.dataPack.photo.PhotoInfo;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter <PhotoAdapter.PhotoHolder>{
    private List<PhotoInfo> photoList;
    private Context mContext;
    public static List<PhotoInfo> uPhotoList=new ArrayList<PhotoInfo>();

    public static List<PhotoInfo> getuPhotoList() {
        return uPhotoList;
    }

    public PhotoAdapter(List<PhotoInfo> photoList, Context context){
        this.photoList=photoList;
        this.mContext=context;
        uPhotoList.clear();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private CheckBox checkPhoto;
        public PhotoHolder(View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.photo_image);
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (uPhotoList.size()==0){
                        browseAll(getAdapterPosition());
                    }else{
                        int current=uPhotoList.indexOf(photoList.get(getAdapterPosition()));
                        if (current==-1){
                            browseAll(getAdapterPosition());
                        }else {
                            browseSelect(current);
                        }
                    }
                }
            });
            checkPhoto=itemView.findViewById(R.id.check_photo);
            checkPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ListIterator<PhotoInfo> imagePathIterator=uPhotoList.listIterator();
//                    if (uPhotoList.size()==0) {
//                        if (!checkPhoto.isSelected()){
//                            uPhotoList.add(photoList.get(getAdapterPosition()));
//                        }
//                    }else{
//                        while (imagePathIterator.hasNext())
//                            if (imagePathIterator.next()==photoList.get(getAdapterPosition())){
//                                imagePathIterator.remove();
//                                break;
//                            }else {
//                                if (!imagePathIterator.hasNext()) {
//                                    uPhotoList.add(photoList.get(getAdapterPosition()));
//                                }else{
//                                    continue;
//                                }
//                            }
//                    }
//                    Toast.makeText(mContext, String.valueOf(uPhotoList.size()), Toast.LENGTH_SHORT).show();
                    if (uPhotoList.size()==0) {
                        if (!checkPhoto.isSelected()){
                            uPhotoList.add(photoList.get(getAdapterPosition()));
                        }
                    }else{
                        int i=0;
                        for (;i<uPhotoList.size();i++){
                            if (uPhotoList.get(i)==photoList.get(getAdapterPosition())){
                                uPhotoList.remove(i);
                                i--;
                                break;
                            }
                        }
                        if (i==uPhotoList.size()) {
                            uPhotoList.add(photoList.get(getAdapterPosition()));
                        }
                    }
                    Toast.makeText(mContext, String.valueOf(uPhotoList.size()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View photoView= LayoutInflater.from(mContext).inflate(R.layout.photo_item,null);
        return new PhotoHolder(photoView);
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        PhotoInfo photoInfo=photoList.get(position);
        Glide.with(mContext)
                .load(photoInfo.getUrl())
                .centerCrop()
                .override(100,100)
                .into(holder.mImageView);
        holder.checkPhoto.setChecked(false);
        if (uPhotoList.size()!=0){
            int i;
            for (i=0;i<uPhotoList.size();i++)
                if (uPhotoList.get(i).getId()==photoInfo.getId()){
                    holder.checkPhoto.setChecked(true);
                    break;
                }
            if (i==uPhotoList.size()) {
                holder.checkPhoto.setChecked(false);
            }
        }
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    /** 预览列表图片 */
    private void browseAll(int current){
        Intent intent=new Intent(mContext, PhotoBrowseActivity.class);
        ArrayList<String> photoBrowseList=new ArrayList<>();
        for (int i=0;i<photoList.size();i++){
            photoBrowseList.add(photoList.get(i).getUrl());
        }
        intent.putStringArrayListExtra(
                "PhotoBrowse", photoBrowseList);
        intent.putExtra("CurrentBrowse",current);
        mContext.startActivity(intent);
    }

    /** 预览选中的图片 */
    private void browseSelect(int current){
        Intent intent=new Intent(mContext, PhotoBrowseActivity.class);
        ArrayList<String> photoBrowseList=new ArrayList<>();
        for (int i=0;i<uPhotoList.size();i++){
            photoBrowseList.add(uPhotoList.get(i).getUrl());
        }
        intent.putStringArrayListExtra(
                "PhotoBrowse", photoBrowseList);
        intent.putExtra("CurrentBrowse",current);
        mContext.startActivity(intent);
    }

    /** 全选 */
    public void selectAll(){
        for (int i=0;i<photoList.size();i++){
            uPhotoList.add(photoList.get(i));
        }
        notifyDataSetChanged();
    }

    /** 取消全选 */
    public void selectNull(){
        uPhotoList.clear();
        notifyDataSetChanged();
    }
}
