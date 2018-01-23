package com.zongke.hapilolauncher.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by ${xingen} on 2017/12/2.
 */

public class TestActivity extends AppCompatActivity{
    private TextView textView;
    private String keyName="keyName";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
       String content=  intent.getStringExtra(keyName);
        textView=new TextView(this);
        textView.setText(content);
        setContentView(textView);
    }
}
