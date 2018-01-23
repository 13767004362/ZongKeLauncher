package com.zongke.hapilolauncher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.utils.DisplayUtils;
import com.zongke.hapilolauncher.utils.Tools;

/**
 * 激活设备界面
 * Created by llj on 2017/9/16.
 */

public class ActivateDeviceActivity extends AppCompatActivity {
    private ImageView qdImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        qdImg = (ImageView) findViewById(R.id.qd_img);
        qdImg.setImageBitmap(Tools.createQRImage("教主刘", DisplayUtils.dip2px(this, 200), DisplayUtils.dip2px(this, 200)));
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ActivateDeviceActivity.class));
    }
}
