package com.zongke.hapilolauncher.library.retrofit;


import android.content.Context;

import com.zongke.hapilolauncher.base.BaseApplication;
import com.zongke.hapilolauncher.db.entity.ActivityDegreeListBean;
import com.zongke.hapilolauncher.db.entity.DeviceMSG;
import com.zongke.hapilolauncher.db.entity.FunctionList_Entity;
import com.zongke.hapilolauncher.library.okhttp.OkHttpProvider;
import com.zongke.hapilolauncher.library.rxjava.SubscribeUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ${xingen} on 2017/7/10.
 * Retrofit的操作类
 */

public class RetrofitProvider {
    private final String BASE_URL;
    private final Retrofit retrofit;
    private static RetrofitProvider instance;
    private HapiloService haplioService;
    private Context appContext;
    private RetrofitProvider(){
        OkHttpClient okHttpClient = OkHttpProvider.createOkHttpClient();
        BASE_URL=HttpConstance.BASE_URL;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                //传输层
                .client(okHttpClient)
                //GSON解析
                .addConverterFactory(GsonConverterFactory.create())
                //Rxjava适配器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
       this.haplioService=this.retrofit.create(HapiloService.class);
        this.appContext= BaseApplication.getInstance();
    }

    /**
     * 单例
     * @return
     */
    public static  synchronized  RetrofitProvider getInstance(){
        if (instance==null) {
            instance=new RetrofitProvider();
        }
        return instance;
    }
    public Subscription getHomeData(Subscriber<FunctionList_Entity> subscriber){
        //存放get请求的参数
        Map<String,String> map=new HashMap<>();
        //拼接的地址
        String url="get_main_config_info";
      return   this.haplioService.getHomeData(url,map).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }



    public Observable<HttpResult<DeviceMSG>> getDeviceMsg(Map<String,Object> params){

      return  this.haplioService.getDeviceMsg(params);
    }

      public Subscription  getActivityDreeList(Map<String,Object> params,ResponseResultListener<ActivityDegreeListBean> responseResultListener){
          return this.haplioService.getActivityDegreeList(params).compose(SubscribeUtils.createTransformer()).subscribe(toSSubscriber(responseResultListener));
      }
      private <T> Subscriber toSSubscriber(ResponseResultListener<T> responseResultListener){
          return  new BaseSubscriber<T>(appContext,responseResultListener);
      }


}
