package com.zongke.hapilolauncher.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ${xingen} on 2017/8/8.
 *
 *
 * 一个基本的RecyclerView的Adapter父类
 *
 * 抽出一些共同行为：
 *
 *   1. 一个添加数据的方法,
 *   2. Item的点击事件。
 */

public abstract class BaseRecyclerViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    public  abstract  void addData(T t);

    private RecyclerViewItemClickListener itemClickListener;

    public RecyclerViewItemClickListener getItemClickListener() {
        return itemClickListener;
    }
    public void setItemClickListener(RecyclerViewItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 设置一个获取Item的方法，
     * 这里设置不绑定父容器，为了实现item填充屏幕。
     * @param parent
     * @param layoutId
     * @return
     */
    protected View getItemRootView(ViewGroup parent, int layoutId){
        View rootView=LayoutInflater .from(parent.getContext()).inflate(layoutId,parent,false);
        return rootView ;
    }

    /**
     * 设置Item的点击实现。
     *
     * 注意点：
     *  ViewHolder对象在没有加载Adapter前，getLayoutPosition()返回的是-1。
     * @param viewHolder
     *
     */
    protected void setItemClick(final RecyclerView.ViewHolder viewHolder){
        if (viewHolder.itemView!=null&&getItemClickListener()!=null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getItemClickListener().onItemClick( viewHolder.itemView, viewHolder.getLayoutPosition());
                }
            });
        }
    }
}
