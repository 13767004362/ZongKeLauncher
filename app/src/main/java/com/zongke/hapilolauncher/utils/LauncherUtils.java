package com.zongke.hapilolauncher.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class LauncherUtils {
    public static final String tag = LauncherUtils.class.getSimpleName();
    public static LauncherUtils instance;
    static {
        instance = new LauncherUtils();
    }
    private LauncherUtils() {

    }
    /**
     * 设置系统默认的Launcher
     */
    public void  setDefaultLauncher(Context context, String mainActivityName) {
        clearDefaultLauncherApps(context);
        List<ResolveInfo>  allLauncherList = getAllLauncherApps(context);
        Log.i(tag, " 设备上安装的Launcher个数 : ${allLauncherList.size}  ");
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        intentFilter.addCategory(Intent.CATEGORY_HOME);
        ComponentName launcher =new  ComponentName(context.getPackageName(), mainActivityName);
        ComponentName[] componentNameSet = new ComponentName[ allLauncherList.size()];
        int defaultMatchLauncher = 0;
        for (int i=0;i< allLauncherList.size();++i) {
            ResolveInfo resolveInfo = allLauncherList.get(i);
            componentNameSet[i] = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
          StringBuffer  stringBuffer = createMatchName(launcher);
           // Log.i(tag, " 索引是$i  将要设置成默认的Launcher:${stringBuffer.toString()}    信息名：${resolveInfo.activityInfo.name} ")
            if (stringBuffer.toString().equals(resolveInfo.activityInfo.name)) {
                defaultMatchLauncher = resolveInfo.match;
            }
        }
        try {
            context.getPackageManager().addPreferredActivity(intentFilter, defaultMatchLauncher, componentNameSet, launcher);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 创建匹配的名字
     */
   public StringBuffer createMatchName(ComponentName launcher) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(launcher.getPackageName());
        stringBuffer.append(".");
        stringBuffer.append(launcher.getClassName());
        return stringBuffer;
    }
    /**
     * 清除当前默认的Launcher
     */
    public void clearDefaultLauncherApps(Context context) {
      PackageManager  packageManager = context.getPackageManager();
        List<IntentFilter> intentList = new ArrayList< >();
       List<ComponentName> componetNameList =new  ArrayList < ComponentName > ();
        //查询到首先的Activity
        packageManager.getPreferredActivities(intentList, componetNameList, null);
        try {
            for (int i=0;i<intentList.size();++i) {
                IntentFilter intentFilter = intentList.get(i);
                //筛选除首先的Launcher中主界面
                if (intentFilter.hasAction(Intent.ACTION_MAIN) && intentFilter.hasCategory(Intent.CATEGORY_HOME)) {
                    Log.i(tag, " 清空的Launcher包名: ${componetNameList[i].packageName}");
                    packageManager.clearPackagePreferredActivities(componetNameList.get(i).getPackageName());
                }
            }
        } catch ( Exception  e){
            e.printStackTrace();
        }
    }
    /**
     * 获取到Android系统硬件或者手机上安装的全部的Launcher
     */
    public List<ResolveInfo> getAllLauncherApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        return packageManager.queryIntentActivities(intent, 0);
    }
}
