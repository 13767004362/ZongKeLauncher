package com.zongke.hapilolauncher.library.okhttp;

import com.zongke.hapilolauncher.library.retrofit.HttpConstance;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class HeaderInterceptor implements Interceptor {
   public static final String appId = "appId";
 public static final String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";
    @Override
    public Response intercept(Chain chain) throws IOException {
       Request.Builder  builder = chain.request().newBuilder();
        builder.addHeader(appId, HttpConstance.APP_ID)
                .addHeader("User-Agent", "android")
                .addHeader("Content-Type", CONTENT_TYPE);
        return chain.proceed(builder.build());
    }
}
