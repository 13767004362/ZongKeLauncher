package com.zongke.hapilolauncher.library.okhttp;

import com.zongke.hapilolauncher.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class OkHttpProvider {
    public static OkHttpClient createOkHttpClient(){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        //拦截器
        builder.addInterceptor(new HeaderInterceptor());
       HttpLoggingInterceptor loggingInterceptor =new  HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            //打印一次请求的全部信息
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
            return builder.build();
    }
}
