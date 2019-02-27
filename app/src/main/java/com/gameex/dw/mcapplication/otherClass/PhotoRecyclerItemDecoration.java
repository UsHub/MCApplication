package com.gameex.dw.mcapplication.otherClass;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PhotoRecyclerItemDecoration extends RecyclerView.ItemDecoration {
    private int itemSpace;
    private int itemNum;

    /**
     * @param itemSpace item间隔
     * @param itemNum 每行item数量
     */
    public PhotoRecyclerItemDecoration(int itemSpace,int itemNum){
        this.itemSpace=itemSpace;
        this.itemNum=itemNum;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent
            , RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom=itemSpace;
        if (parent.getChildLayoutPosition(view)%itemNum == 0){
            outRect.left=0;
        }else {
            outRect.left=itemSpace;
        }
    }
}
