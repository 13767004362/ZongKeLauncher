package com.zongke.hapilolauncher.applist;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import com.zongke.hapilolauncher.R;

import java.io.File;

/**
 * Created by ${xinGen} on 2018/1/17.
 *
 */
public class AppModel {
    private static final String tag = AppModel.class.getSimpleName();
    private Context context;
    private ApplicationInfo applicationInfo;
    /**
     * 程序的标签
     */
    private String appLabel;
    /**
     * 程序的icon
     */
    private Drawable icon;
    /**
     * 是否安装
     */
    private boolean mounted;
    /**
     * 程序的所在的路径
     */
    private File apkFile;
    /**
     * 程序包名
     */
    private String applicationPackageName;

    public AppModel(Context context, ApplicationInfo applicationInfo) {
        this.context = context;
        this.applicationInfo = applicationInfo;
        this.apkFile = new File(this.applicationInfo.sourceDir);
    }
    public Drawable getIcon() {
        if (icon == null) {
            if (apkFile.exists()) {
                icon = applicationInfo.loadIcon(context.getPackageManager());
                return icon;
            } else {
                mounted = false;
            }
        } else if (!mounted) {
            //先前程序未安装，现在安装了，需要重新加载icon
            if (apkFile.exists()) {
                mounted = true;
                icon = applicationInfo.loadIcon(context.getPackageManager());
                return icon;
            }
        } else {
            return icon;
        }
        return context.getResources().getDrawable(R.mipmap.ic_launcher);
    }
    public void loadLabel(Context context) {
        if (appLabel == null || !mounted) {
            //若是apk路径不存在
            if (!apkFile.exists()) {
                mounted = false;
                appLabel = applicationInfo.packageName;
            } else {
                mounted = true;
                CharSequence label = applicationInfo.loadLabel(context.getPackageManager());
                if (label != null) {
                    appLabel = label.toString();
                } else {
                    appLabel = applicationInfo.packageName;
                }
            }
        }
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isMounted() {
        return mounted;
    }

    public void setMounted(boolean mounted) {
        this.mounted = mounted;
    }

    public File getApkFile() {
        return apkFile;
    }

    public void setApkFile(File apkFile) {
        this.apkFile = apkFile;
    }

    public String getApplicationPackageName() {
        return applicationPackageName;
    }

    public void setApplicationPackageName(String applicationPackageName) {
        this.applicationPackageName = applicationPackageName;
    }
}
