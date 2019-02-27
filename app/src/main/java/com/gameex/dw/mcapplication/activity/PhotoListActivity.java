package com.gameex.dw.mcapplication.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.adapterPack.photo.PhotoAdapter;
import com.gameex.dw.mcapplication.adapterPack.photo.PhotoPopupAdapter;
import com.gameex.dw.mcapplication.dataPack.photo.PhotoGroup;
import com.gameex.dw.mcapplication.dataPack.photo.PhotoInfo;
import com.gameex.dw.mcapplication.otherClass.DataGroup;
import com.gameex.dw.mcapplication.otherClass.PhotoRecyclerItemDecoration;
import com.gameex.dw.mcapplication.service.PhotoLoad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhotoListActivity extends AppCompatActivity implements View.OnClickListener{
    private static int PHOTOPATH_OK=10;

    private Boolean isSelectedAll=false;
    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private String[] path;
    private List<PhotoInfo> mPhotoInfos;
    private HashMap<String,List<PhotoInfo>> mPhotoMap;
    private PopupWindow photoPopupWindow;
    private ImageButton popupControl;
    private TextView floderNameView;
    private Button surePhotoButton,selectAll,browse;
    private PhotoAdapterReceiver mPhotoAdapterReceiver;
    private LocalBroadcastManager mLocalBroadcastManager;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        intent=new Intent(PhotoListActivity.this, PhotoLoad.class);
        startService(intent);

        initView();
    }

    /** 初始化布局和数据 */
    private void initView(){
        mRecyclerView=findViewById(R.id.photo_recycler);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new PhotoRecyclerItemDecoration(12,3));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        popupControl=findViewById(R.id.popup_window_control);
        popupControl.setOnClickListener(this);
        floderNameView=findViewById(R.id.floder_name);
        surePhotoButton=findViewById(R.id.sure_photo_list_path);
        surePhotoButton.setOnClickListener(this);
        selectAll=findViewById(R.id.select_all_photo);
        selectAll.setOnClickListener(this);
        browse=findViewById(R.id.browse_photo);
        browse.setOnClickListener(this);

        mPhotoMap=new HashMap<>();
        mPhotoInfos=new ArrayList<>();

        mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);
        mPhotoAdapterReceiver=new PhotoAdapterReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("PHOTOLIST_ACTION");
        intentFilter.addAction("com.gameex.dw.mcapplication.activity.PHOTOMAP");
        mLocalBroadcastManager.registerReceiver(mPhotoAdapterReceiver,intentFilter);
    }

    /** 加载布局 */
    private void updateUi(List<PhotoInfo> photoInfos){
        mPhotoAdapter=new PhotoAdapter(photoInfos,this);
        mRecyclerView.setAdapter(mPhotoAdapter);
    }

    /** 设置popupWindow属性，添加数据 */
    private void getPhotoPopup(){
        View photoPopupView=PhotoListActivity.this.getLayoutInflater()
                .inflate(R.layout.photo_popup_window_main,null);
        RecyclerView photoPopupRecycler=photoPopupView
                .findViewById(R.id.photo_popup_recycler);
        photoPopupRecycler.setLayoutManager(new LinearLayoutManager(
                PhotoListActivity.this));
        photoPopupRecycler.addItemDecoration(new DividerItemDecoration(
                this,DividerItemDecoration.VERTICAL));
        List<PhotoGroup> photoGroups= DataGroup.subGroupOfPhoto(mPhotoMap);
        PhotoPopupAdapter photoPopupAdapter=new PhotoPopupAdapter(
                PhotoListActivity.this,photoGroups);
        photoPopupRecycler.setAdapter(photoPopupAdapter);
        photoPopupWindow=new PopupWindow(photoPopupView,600,800);
        photoPopupWindow.setAnimationStyle(R.style.AnimationPhoto);
        photoPopupWindow.setBackgroundDrawable(new ColorDrawable(
                Color.parseColor("#F8F8F8")));
        photoPopupWindow.setFocusable(true);
        photoPopupWindow.setOutsideTouchable(true);
        photoPopupWindow.update();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.popup_window_control){
            if (photoPopupWindow!=null && photoPopupWindow.isShowing()){
                photoPopupWindow.dismiss();
            }else{
                photoPopupWindow.showAsDropDown(v,-80,30);
                popupControl.setImageResource(R.drawable.arrow_drop_up);
            }
        }else if (v.getId()==R.id.sure_photo_list_path){
            mPhotoInfos=PhotoAdapter.getuPhotoList();
            if (mPhotoInfos.size()==0){
                Toast.makeText(this, "忘记选图片了！", Toast.LENGTH_SHORT).show();
            }else{
                path=new String[mPhotoInfos.size()];
                for (int i=0;i<mPhotoInfos.size();i++){
                    path[i]=mPhotoInfos.get(i).getUrl();
                }
                if (path[0]!=null){
                    Intent intent=new Intent(PhotoListActivity.this,MainActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putStringArray("PhotoPath",path);
                    intent.putExtras(bundle);
                    setResult(PHOTOPATH_OK,intent);
                    finish();
                }
            }
        }else if (v.getId()==R.id.select_all_photo){
            if (!isSelectedAll){
                isSelectedAll=true;
                mPhotoAdapter.selectAll();
                selectAll.setText("取消全选");
            }else {
                isSelectedAll=false;
                mPhotoAdapter.selectNull();
                selectAll.setText("全选");
            }
        }else if (v.getId()==R.id.browse_photo){
            List<PhotoInfo> photoInfos=PhotoAdapter.getuPhotoList();
            if (photoInfos.size()==0){
                Toast.makeText(this, "还没选图片呢", Toast.LENGTH_SHORT).show();
            }else{
                ArrayList<String> photoBrowseList=new ArrayList<>();
                Intent bIntent=new Intent(this,PhotoBrowseActivity.class);
                for (int i=0;i<photoInfos.size();i++){
                    photoBrowseList.add(photoInfos.get(i).getUrl());
                }
                bIntent.putStringArrayListExtra(
                        "PhotoBrowse", photoBrowseList);
                bIntent.putExtra("CurrentBrowse"
                        ,0);
                startActivity(bIntent);
            }
        }
    }

    private class PhotoAdapterReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("PHOTOLIST_ACTION")){
                String FloderName=intent.getExtras().getString("PhotoList");
                if (photoPopupWindow.isShowing()){
                    photoPopupWindow.dismiss();
                    popupControl.setImageResource(R.drawable.arrow_drop_down);
                    floderNameView.setText(FloderName);
                }
                updateUi(mPhotoMap.get(FloderName));
                if (isSelectedAll){
                    isSelectedAll=false;
                    selectAll.setText("全选");
                }
            }else if (intent.getAction().equals("com.gameex.dw.mcapplication.activity.PHOTOMAP")){
                mPhotoMap= (HashMap<String, List<PhotoInfo>>)
                        intent.getExtras().get("PhotoMap");
                if (mPhotoMap.size()!=0){
                    updateUi(mPhotoMap.get("全部图片"));
                    getPhotoPopup();
                }else{
                    Log.e("测试","数据传递失败！");
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalBroadcastManager.unregisterReceiver(mPhotoAdapterReceiver);
        stopService(intent);
    }
}
