package com.zongke.hapilolauncher.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ${xingen} on 2017/8/25.
 * <p>
 * 自适应Item高度的LinearLayoutManager
 */
public class AdaptiveLinearLayoutManager extends LinearLayoutManager {
    public AdaptiveLinearLayoutManager(Context context) {
        super(context);
    }

    /**
     * 重写onMeasure():
     * <p>
     * <p>
     * 1. 先获取到RecyclerView.Recycler对象，RecyclerView.Recycler负责管理Item的重新利用.
     * 2. measureChild()获取到子类View的高度，在通过setMeasuredDimension（）设置RecyclerView的高度
     *
     * @param recycler
     * @param state
     * @param widthSpec
     * @param heightSpec
     */
    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        if (recycler.getScrapList().size() > 0) {
            View view = recycler.getViewForPosition(0);
            if (view != null) {
                measureChild(view, widthSpec, heightSpec);
                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
                int measuredHeight = view.getMeasuredHeight();
                setMeasuredDimension(measuredWidth, measuredHeight);
            }
        } else {
            super.onMeasure(recycler, state, widthSpec, heightSpec);
        }
    }
}
