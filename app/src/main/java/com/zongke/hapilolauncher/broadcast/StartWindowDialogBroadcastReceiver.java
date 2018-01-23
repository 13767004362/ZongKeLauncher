package com.zongke.hapilolauncher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.window.SuspensionWindowManagerUtils;

/**
 * Created by ${xingen} on 2017/12/4.
 *
 * 开启悬浮窗口的广播
 */

public class StartWindowDialogBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String messageType=intent.getStringExtra("type");
        switch (messageType){
            case TYPE_SHOW_CONTENT:
                if (! SuspensionWindowManagerUtils.windowIsOpen()){
                    SuspensionWindowManagerUtils.createSuspensionWindow(context.getApplicationContext(), R.layout.windowdialog_test_layout);
                    if(!TextUtils.isEmpty(intent.getStringExtra("content"))){
                        SuspensionWindowManagerUtils.suspensionWindowWidget.showSpecifiedContent(intent.getStringExtra("content"));
                    }
                }
                break;
            default:

                break;
        }

    }
    public static final String TYPE_SHOW_CONTENT="showContent";
    public  void register(Context context){

        context.registerReceiver(this,new  IntentFilter("com.zongke.hapilolauncher.broadcast.StartWindowDialogBroadcastReceiver"));
    }
    public void unRegister(Context context){
        context.unregisterReceiver(this);
    }

}
