package com.gameex.dw.mcapplication.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.adapterPack.TabPagerAdapter;
import com.gameex.dw.mcapplication.dataPack.SerMap;
import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;
import com.gameex.dw.mcapplication.otherClass.TabLayoutFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongListActivity extends AppCompatActivity
implements ViewPager.OnPageChangeListener{
    private static LinearLayout sLinearLayout;
    private static HashMap<String,List<MusicInfo>> mMusicMap;

    public static HashMap<String, List<MusicInfo>> getmMusicMap() {
        return mMusicMap;
    }

    public static LinearLayout getLinearLayout() {
        return sLinearLayout;
    }

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitleList;
    private List<Fragment> mViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sLinearLayout=findViewById(R.id.id_for_floder_music_popup);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        SerMap serMap= (SerMap) bundle.get("SERMAP");
        mMusicMap=new HashMap<>();
        mMusicMap=serMap.getMusicMap();

        initViewPager();

    }

    /** 初始化TabLayout和ViewPager */
    private void initViewPager(){
        mTabLayout=findViewById(R.id.tab_layout);
        mViewPager=findViewById(R.id.view_pager);
        mViewList=new ArrayList<>();
        for (int i=0;i<2;i++){
            TabLayoutFragment tabLayoutFragment=new TabLayoutFragment();
            Bundle bundle=new Bundle();
            bundle.putString("flag",String.valueOf(i));
            tabLayoutFragment.setArguments(bundle);
            mViewList.add(tabLayoutFragment);
        }

        mTitleList=new ArrayList<>();
        mTitleList.add("音乐");
        mTitleList.add("文件夹");

        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)),true);
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
//
        TabPagerAdapter tabPagerAdapter=new TabPagerAdapter(getSupportFragmentManager()
                , mViewList,mTitleList);
        mViewPager.setAdapter(tabPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(this);
        mTabLayout.setupWithViewPager(mViewPager);//将tabLayout和viewPager关联
        mTabLayout.setTabsFromPagerAdapter(tabPagerAdapter);//给tabs设置适配器
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
