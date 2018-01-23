package com.zongke.hapilolauncher.applist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zongke.hapilolauncher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private List<AppModel> appList;

    public AppListAdapter() {
        this.appList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View rootView = View.inflate(parent.getContext(), R.layout.applist_item_view, null);
      final   ViewHolder viewHolder = new ViewHolder(rootView);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getLayoutPosition();
                AppModel appModel = appList.get(position);
                Context context = parent.getContext();
                //打开对应的运用程序
                if (appModel != null) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(appModel.getApplicationPackageName());
                    if (intent != null) {
                        context.startActivity(intent);
                    }
                }
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageDrawable(appList.get(position).getIcon());
        holder.textView.setText(appList.get(position).getAppLabel());
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public void addData(List<AppModel> list) {
        appList.clear();
        appList.addAll(list);
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.item_iv);
            this.textView = itemView.findViewById(R.id.item_tv);
        }
    }
}
