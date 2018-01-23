package com.zongke.hapilolauncher.utils;

import android.view.View;
import android.view.Window;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class SystemUIManager {
    /**
     * 隐藏NavigationBar
     */
    public static final int action_navigation_bar_hide = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

    /**
     * 全屏，会隐藏StatusBar
     */
    public static final int action_full_screen = View.SYSTEM_UI_FLAG_FULLSCREEN;
    /**
     * 系统4.1或者更高版本，可以让NavigationBar和StatusBar浮在内容上面。
     */
    public static final int action_navigation_bar_hide_float = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
    /**
     * 与SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION结合使用，确保布局稳定
     */
    public static final int action_stable = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
    /**
     * 在系统4.0，API14以上使StatusBar和NavigationBar暗淡显示。
     */
    public static final int action_dim = View.SYSTEM_UI_FLAG_LOW_PROFILE;
    /**
     * 系统4.4 出现，该flag可以让程序进入全屏。
     */
    public static final int action_immersive = View.SYSTEM_UI_FLAG_IMMERSIVE;
    /**
     * 系统4.4 出现，该flag可以让系统bar出现，但是一会儿自动消失。Sticky方式
     */
    public static final int action_immersive_sticky = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    /**
     * Sticky风格
     */
    public static void setStickyStyle(Window window) {
        int flag = action_navigation_bar_hide | action_full_screen |
                action_navigation_bar_hide_float | action_navigation_bar_hide_float | action_stable | action_immersive_sticky;
        window.getDecorView().setSystemUiVisibility(flag);
    }
    /**
     * Immersive风格
     */
    public static void setImmersiveStyle(Window window) {
        int flag = action_navigation_bar_hide | action_full_screen | action_navigation_bar_hide_float | action_navigation_bar_hide_float | action_stable | action_immersive;
        window.getDecorView().setSystemUiVisibility(flag);
    }
    /**
     * 正常风格
     */
    public static void setNormalStyle(Window window) {
        int flag = action_navigation_bar_hide | action_full_screen;
        window.getDecorView().setSystemUiVisibility(flag);
    }
    /**
     * 暗淡风格
     */
    public static void setDimStyle(Window window) {
        int flag = action_dim;
        window.getDecorView().setSystemUiVisibility(flag);
    }
    /**
     * 清空全部flag ，恢复初始状态
     */
    public static void clearStyle(Window window) {
        //传递0值清空全部flags。
        int flag = 0;
        window.getDecorView().setSystemUiVisibility(flag);
    }
}
