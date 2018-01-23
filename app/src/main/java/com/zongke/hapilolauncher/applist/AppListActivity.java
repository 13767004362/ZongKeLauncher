package com.zongke.hapilolauncher.applist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zongke.hapilolauncher.R;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class AppListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);

        getSupportFragmentManager().beginTransaction().add(R.id.main_content_layout, AppListFragment.newInstance(),AppListFragment.TAG).commit();

    }
    public static void openActivity(Context context){
        Intent intent=new Intent(context,AppListActivity.class);
        if (!(context instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }
}
