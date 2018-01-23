package com.zongke.hapilolauncher.window;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class SuspensionWindowManagerUtils {
    /**
     * 申请权限的状态code
     */
    public static final int request_code_window = 1;
    public static WindowManager windowManager;
    public static WindowManager.LayoutParams layoutParams;
    public static SuspensionWindowLayout suspensionWindowWidget;

    /**
     * 开启悬浮弹窗
     */
    public static void openSuspensionWindow(Activity context) {
        if (checkPermission(context)) {
            if (!windowIsOpen()) {
                SuspensionWindowManagerUtils.createSuspensionWindow(context.getApplicationContext());
            }
        } else {
            requestPermission(context);
        }
    }

    /**
     * 开启权限管理界面，授权。
     */
    public static void requestPermission(Activity context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"));
        context.startActivityForResult(intent, request_code_window);
    }

    /**
     * 悬浮窗口是否已经打开
     */
    public static boolean windowIsOpen() {
        return suspensionWindowWidget != null ? true : false;
    }

    /**
     * 当目标版本大于23时候，检查权限
     */

    public static boolean checkPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 23)
            return Settings.canDrawOverlays(context);
        else
            return true;
    }

    public static void createSuspensionWindow(Context context) {
        createSuspensionWindow(context, 0);
    }
    /**
     * 创建悬浮窗口
     */
 public static  void createSuspensionWindow( Context context,int layoutId) {
            suspensionWindowWidget= new SuspensionWindowLayout(context,layoutId);
        getWindowManager(context).addView(suspensionWindowWidget, getWidgetLayoutParams());
    }
    /**
     * 移除悬浮窗口
     */
    public static void removeSuspensionWindow(Context context) {
        if (suspensionWindowWidget != null) {
            getWindowManager(context).removeView(suspensionWindowWidget);
            suspensionWindowWidget = null;
        }
    }

    /**
     * 获取悬浮窗口的布局参数
     */
    public static WindowManager.LayoutParams getWidgetLayoutParams() {
        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            layoutParams.format = PixelFormat.RGBA_8888;
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
            layoutParams.x = windowManager.getDefaultDisplay().getWidth();
            layoutParams.y = 0;
            layoutParams.width = SuspensionWindowLayout.widget_width;
            layoutParams.height = SuspensionWindowLayout.widget_height;
        }
        return layoutParams;
    }

    /**
     * 获取窗口管理器
     */
    public static WindowManager getWindowManager(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }
}
