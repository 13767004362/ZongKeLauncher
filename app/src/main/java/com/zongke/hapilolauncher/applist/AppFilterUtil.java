package com.zongke.hapilolauncher.applist;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.util.List;

/**
 * Created by ${xingen} on 2017/10/28.
 */

public class AppFilterUtil {
    /**
     *  筛选出指定包的icon对应的bitmap
     * @param context
     * @param packageName
     * @return
     */
    public static  Bitmap  filterApp(Context context ,String packageName){
        Bitmap bitmap=null;
        List<ApplicationInfo> infoList= context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo applicationInfo:infoList){
            if (applicationInfo.packageName.equals(packageName)){

                BitmapDrawable drawable= (BitmapDrawable) applicationInfo.loadIcon(context.getPackageManager());

                bitmap=drawable.getBitmap();
                break;
            }else{
                continue;
            }
        }
        return bitmap;
    }

}
