package com.zongke.hapilolauncher.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ${xingen} on 2017/8/8.
 * 重写RecyclerView，定义子类
 *
 * 1. 去掉滚动的bar
 * 2. 去掉滚动到边界有阴影
 */

public class BaseRecyclerView  extends RecyclerView {
    public BaseRecyclerView(Context context) {
        super(context);
        initConfig();
    }
    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initConfig();
    }
    private void initConfig() {
         //设置滚动边界无阴影
       this.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //设置无scrollbar
        // this.setScrollbarFadingEnabled(false);
          this.setVerticalScrollBarEnabled(false);
         this.setHorizontalScrollBarEnabled(false);
    }
}
