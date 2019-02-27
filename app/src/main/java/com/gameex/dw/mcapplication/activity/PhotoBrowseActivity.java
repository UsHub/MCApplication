package com.gameex.dw.mcapplication.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.bm.library.PhotoView;
import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.adapterPack.photo.PhotoBrowsePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PhotoBrowseActivity extends AppCompatActivity {
    private static PhotoBrowseActivity sPhotoBrowseActivity;

    public static PhotoBrowseActivity getPhotoBrowseActivity() {
        return sPhotoBrowseActivity;
    }

    private List<String> photoBrowseList;
    private int current;
    private ViewPager mViewPager;
    private PhotoBrowsePagerAdapter mPhotoBrowsePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_browse);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
    }

    /** 初始化 */
    private void initView(){
        sPhotoBrowseActivity=this;

        photoBrowseList=new ArrayList<>();
        if (getIntent()!=null){
            Intent intent=getIntent();
            photoBrowseList=intent.getStringArrayListExtra("PhotoBrowse");
            current=intent.getIntExtra("CurrentBrowse",0);
        }
        mViewPager = findViewById(R.id.browse_view_pager);
        mPhotoBrowsePagerAdapter=new PhotoBrowsePagerAdapter(photoBrowseList,getApplicationContext());
        mViewPager.setAdapter(mPhotoBrowsePagerAdapter);
        mViewPager.setCurrentItem(current);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
