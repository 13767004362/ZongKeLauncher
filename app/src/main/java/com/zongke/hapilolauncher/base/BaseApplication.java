package com.zongke.hapilolauncher.base;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class BaseApplication extends MultiDexApplication {
    private static  BaseApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        initMultiDex();
    }
    /**
     * 突破MultiDex的限制
     */
    private void initMultiDex() {
        MultiDex.install(this);
    }
    public static BaseApplication getInstance() {
        return instance;
    }
}
