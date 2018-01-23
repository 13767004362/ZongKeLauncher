package com.zongke.hapilolauncher.library.rxjava;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ${xingen} on 2017/11/6.
 *
 * 一个工具类，处理Subscribe订阅Observable。
 */

public class SubscribeUtils {

    /**
     * 一个通用的观察者发出对象，订阅者订阅对象的方法。
     *
     * @param o
     * @param s
     * @param <T>
     */
    public static  <T> Subscription  toSubscribe(Observable<T> o, Subscriber<T> s) {
       return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 构建一个Observable.Transformer对象
     * @return
     */
    public static Observable.Transformer createTransformer(){
        return  new Observable.Transformer() {
            @Override
            public Object call(Object o) {
              return((Observable)o).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

}
