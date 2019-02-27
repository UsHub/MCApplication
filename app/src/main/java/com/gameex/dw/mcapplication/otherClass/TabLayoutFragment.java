package com.gameex.dw.mcapplication.otherClass;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gameex.dw.mcapplication.R;
import com.gameex.dw.mcapplication.activity.SongListActivity;
import com.gameex.dw.mcapplication.adapterPack.music.MusicFloderAdapter;
import com.gameex.dw.mcapplication.adapterPack.music.SongsAdapter;
import com.gameex.dw.mcapplication.dataPack.music.MusicFloderGroup;
import com.gameex.dw.mcapplication.dataPack.music.MusicInfo;

import java.util.List;

public class TabLayoutFragment extends Fragment {

        private static final String ARG_PARAM1="flag";
        private static final String ARG_PARAM2="param2";

        private String param1;
        private String param2;
        private List<MusicInfo> mMusicInfos;
        private List<MusicFloderGroup> mMusicFloderGroups;

        private View mView;
        private SongsAdapter mSongsAdapter;
        private MusicFloderAdapter mMusicFloderAdapter;

        private RecyclerView mRecyclerView;

        public TabLayoutFragment(){}

        public static TabLayoutFragment newInstance(String param1,String param2){
            TabLayoutFragment tabLayoutFragment=new TabLayoutFragment();
            Bundle args=new Bundle();
            args.putString(ARG_PARAM1,param1);
            args.putString(ARG_PARAM2,param2);
            tabLayoutFragment.setArguments(args);
            return tabLayoutFragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments()!=null){
                param1=getArguments().getString(ARG_PARAM1);
                param2=getArguments().getString(ARG_PARAM2);
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater
                , @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
            mView=inflater.inflate(R.layout.fragment_tab_layout,container,false);
            return mView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            initView();
            initData();
        }

        private void initView(){
            mRecyclerView= mView.findViewById(R.id.song_recycler);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(
                    getActivity(),LinearLayoutManager.VERTICAL,false));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(
                    getActivity(),DividerItemDecoration.VERTICAL));
            switch (param1){
                case "0":
                    mMusicInfos= SongListActivity.getmMusicMap().get("全部歌曲");
                    mSongsAdapter=new SongsAdapter(mMusicInfos,getActivity());
                    mRecyclerView.setAdapter(mSongsAdapter);
                    break;
                case "1":
                    mMusicFloderGroups=DataGroup.subGroupOfMusic(SongListActivity.getmMusicMap());
                    mMusicFloderAdapter=new MusicFloderAdapter(getActivity(),mMusicFloderGroups);
                    mRecyclerView.setAdapter(mMusicFloderAdapter);
                    break;
            }
        }

        private void initData(){}

}
