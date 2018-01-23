package com.zongke.hapilolauncher.homeScreen;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.db.entity.ActivityDegreeListBean;
import com.zongke.hapilolauncher.recyclerview.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xingen} on 2017/9/6.
 *
 * 个人排名的Adapter
 *
 */

public class PersonRankingAdapter extends BaseRecyclerViewAdapter<List<ActivityDegreeListBean.RecordsBean>,PersonRankingAdapter.ViewHolder> {
    private List<ActivityDegreeListBean.RecordsBean> list;
    PersonRankingAdapter(){
        this.list=new ArrayList<>();
    }
    @Override
    public void addData(List<ActivityDegreeListBean.RecordsBean> personRanking_entities) {
        this.list.addAll(personRanking_entities);
        this.notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView=getItemRootView(parent, R.layout.item_person_ranking);
        ViewHolder viewHolder=new ViewHolder(rootView);
        return viewHolder ;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActivityDegreeListBean.RecordsBean entity=list.get(position);
        switch (position){
            case 0:
                holder.ranking_iv.setImageResource(R.mipmap.launcher_home_ranking_first);
                break;
            case 1:
                holder.ranking_iv.setImageResource(R.mipmap.launcher_home_ranking_second);
                break;
            case 2:
                holder.ranking_iv.setImageResource(R.mipmap.launcher_home_ranking_third);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ranking_iv,icon_iv;
        public ViewHolder(View itemView) {
            super(itemView);
            this.ranking_iv=(ImageView) itemView.findViewById(R.id.item_ranking_iv);
            this.icon_iv=(ImageView) itemView.findViewById(R.id.item_ranking_icon_iv);
        }
    }
}
