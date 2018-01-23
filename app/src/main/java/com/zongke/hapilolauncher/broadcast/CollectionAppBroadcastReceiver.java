package com.zongke.hapilolauncher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zongke.hapilolauncher.service.WriteCollectionAppService;

/**
 * Created by ${xingen} on 2017/10/28.
 *
 * 监听应用商店下载的应用，后收藏的通知。
 *
 */

public class CollectionAppBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(WriteCollectionAppService.TAG,"CollectionAppBroadcastReceiver onReceive  "+intent.getStringExtra("ZK_PackageName") );
        WriteCollectionAppService.startWriteCollectionAppService(context,createBundle(intent));
    }
    private Bundle createBundle(Intent intent){
        Bundle bundle=new Bundle();
        bundle.putString(WriteCollectionAppService.TAG,intent.getStringExtra("ZK_PackageName"));
        return bundle;
    }
}
