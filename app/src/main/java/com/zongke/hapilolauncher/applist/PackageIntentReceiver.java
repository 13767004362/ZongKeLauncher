package com.zongke.hapilolauncher.applist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class PackageIntentReceiver extends BroadcastReceiver {
    private AppsLoader loader;
    public PackageIntentReceiver(AppsLoader loader){
        this.loader=loader;
        this.register(this.loader.getContext());
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        loader.onContentChanged();
    }

    /**
     * 监听
     * @param context
     */
    private void register(Context context){
       IntentFilter  filter=new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        context.registerReceiver(this,filter);
    }
    /**
     * 取消注册监听
     */
    public void unRegister(Context context){
        context.unregisterReceiver(this);
    }
}
