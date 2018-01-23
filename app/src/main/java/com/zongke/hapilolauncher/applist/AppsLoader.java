package com.zongke.hapilolauncher.applist;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.AsyncTaskLoader;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ${xinGen} on 2018/1/17.
 *
 *  *  用于查询加载有效的启动的运用程序，
 *  即getLaunchIntentForPackage（）返回有效的运用程序。
 */

public class AppsLoader extends AsyncTaskLoader<List<AppModel>> {
    private final AppComparator comparator;
    private List<AppModel> instanlledAppsList;
    private PackageManager packageManager;

    public AppsLoader(Context context) {
        super(context);
        this.packageManager = context.getPackageManager();
        this.comparator = new AppComparator();
        this.instanlledAppsList = new ArrayList<>();
    }

    @Override
    public List<AppModel> loadInBackground() {
        //获取系统上安装的运用程序列表
        List<ApplicationInfo> infoList = this.packageManager.getInstalledApplications(0);
        if (infoList == null) {
            infoList = new ArrayList<ApplicationInfo>();
        }
        //创建相应的运用程序且加载他们的标签
        List<AppModel> items = new ArrayList<AppModel>();
        for (int i = 0; i < infoList.size(); ++i) {
            ApplicationInfo applicationInfo = infoList.get(i);
            String packageName = applicationInfo.packageName;
            //检查相应的包名是否可以启动对应的运用程序
            if (packageManager.getLaunchIntentForPackage(packageName) != null) {
                AppModel appModel = new AppModel(getContext(), applicationInfo);
                appModel.loadLabel(getContext());
                items.add(appModel);
            }
        }
        //分类list
        Collections.sort(items, comparator);
        return items;
    }

    @Override
    public void deliverResult(List<AppModel> data) {
        if (isReset()) {
            //当Loader是停止的时候，不需要传递查询结果
            if (data != null) {
                releaseResource(data);
            }
        }
        List<AppModel> oldApps = data;
        instanlledAppsList = data;
        //当Loader已经开始，立即传递结果
        if (isStarted()) {
            super.deliverResult(data);
        }
        //当不需要使用旧数据时候，释放资源
        if (oldApps != null) {
            releaseResource(oldApps);
        }
    }

    @Override
    protected void onStartLoading() {
        //若是当前的结果是可用的，立即传递结果
        if (instanlledAppsList != null) {
            deliverResult(instanlledAppsList);
        }
        //若是，从上次时间后，数据发生改变。或者现在数据不可用，则开始加载。
        if (takeContentChanged() || instanlledAppsList == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        //当需要停止时候，取消加载
        cancelLoad();
    }

    @Override
    public void onCanceled(List<AppModel> data) {
        super.onCanceled(data);
        releaseResource(data);
    }

    @Override
    protected void onReset() {
        //先关闭先前的加载
        onStartLoading();
        //释放原本的数据
        if (instanlledAppsList != null) {
            releaseResource(instanlledAppsList);
            instanlledAppsList = null;
        }
    }

    public void releaseResource(List<AppModel> data) {

    }

    private static class AppComparator implements Comparator<AppModel> {
        private Collator collator;

        public AppComparator() {
            this.collator = Collator.getInstance();
        }

        @Override
        public int compare(AppModel appModel, AppModel appMode2) {
            return collator.compare(appModel.getAppLabel(), appMode2.getAppLabel());
        }
    }

    ;
}
