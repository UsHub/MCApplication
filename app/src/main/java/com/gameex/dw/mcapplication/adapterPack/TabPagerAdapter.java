package com.gameex.dw.mcapplication.adapterPack;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    public TabPagerAdapter(FragmentManager fm
            ,List<Fragment> fragmentList, List<String> mTitleList){
        super(fm);
        this.mFragmentList=fragmentList;
        this.mTitleList=mTitleList;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
}
