package com.zongke.hapilolauncher.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by ${xingen} on 2017/8/8.
 *
 * RecyclerView的分割线(线段)
 */

public class RecyclerViewItemDecoration  extends RecyclerView.ItemDecoration{
    private Context context;
    private Drawable divider;
    public RecyclerViewItemDecoration(Context context,int resourceId){
        this.context=context;
        Resources resources=context.getResources();
        this.divider=resources.getDrawable(resourceId);
    }
    /**
     *  设置 item的 偏移量
     * @param outRect 边界
     * @param view
     * @param parent
     * @param state  内部数据管理
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //画横线，就是往下偏移一个分割线的高度
        outRect.set(0,0,0,this.divider.getIntrinsicHeight());
    }
    /**
     * 将drawable文件夹下的资源绘制到divider中
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //将divider绘制进去
        int left=parent.getPaddingLeft();
        int right=parent.getWidth()-parent.getPaddingRight();
        final  int childCount=parent.getChildCount();
        for (int i=0;i<childCount;++i){
            final View child=parent.getChildAt(i);
            RecyclerView.LayoutParams  layoutParams=(RecyclerView.LayoutParams) child.getLayoutParams();
            int top=child.getBottom()+layoutParams.bottomMargin;
            int bottom=top+divider.getIntrinsicHeight();
            divider.setBounds(left,top,right,bottom);
            divider.draw(c);
        }
    }
}
