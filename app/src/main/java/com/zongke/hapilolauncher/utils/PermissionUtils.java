package com.zongke.hapilolauncher.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class PermissionUtils {

    public static final String  SET_PREFERRED_APPLICATIONS= android.Manifest.permission.SET_PREFERRED_APPLICATIONS;

    public static final int SET_PREFERRED_APPLICATIONS_CODE=1;
    /**
     * 检查指定权限是否授权
     */

    public static boolean checekPermission(Context context,  String permissionNanme){
       boolean checkResult = false;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (getCurrentAppTargetVersion(context)>=Build.VERSION_CODES.M){
                if ( context.checkSelfPermission(permissionNanme)== PackageManager.PERMISSION_GRANTED){
                    checkResult=true;
                }
            }else{
                checkResult=(PermissionChecker.checkSelfPermission(context,permissionNanme)==PackageManager.PERMISSION_GRANTED);
            }
        }else{
            checkResult=(context.getPackageManager().checkPermission(permissionNanme,context.getPackageName())==PackageManager.PERMISSION_GRANTED);
        }
        return checkResult;
    }
    /**
     * 获取当前运用程序的默认target版本。
     */
   public static int getCurrentAppTargetVersion(Context context){
        int  targetVersion=0;
        try {
            targetVersion= context.getPackageManager().getPackageInfo(context.getPackageName(),0).applicationInfo.targetSdkVersion;
        }catch (Exception e){
            e.printStackTrace();
        }
        return targetVersion;
    }



}
