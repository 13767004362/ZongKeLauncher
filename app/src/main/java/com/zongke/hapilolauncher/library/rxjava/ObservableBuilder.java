package com.zongke.hapilolauncher.library.rxjava;


import android.graphics.Bitmap;

import com.zongke.hapilolauncher.base.BaseApplication;
import com.zongke.hapilolauncher.library.zxing.ZXingUtils;
import com.zongke.hapilolauncher.utils.BitmapUtils;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by ${xingen} on 2017/11/15.
 */

public class ObservableBuilder {

    /**
     * 创建每隔5秒查询激活的情况，轮询方式(具备缺陷，无法手动停止)
     *
     * @return
     */
/*    public static Observable<HttpResult<DeviceMSG>> createIntervalQueryDevice() {
        return Observable.interval(3, TimeUnit.SECONDS)
                //进行网络请求
                .flatMap(size -> RetrofitProvider.getInstance().getDeviceMsg().retry())
                //直到请求成功
                .takeUntil(result -> result.getCode() == 1);
    }*/


    /**
     * 创建每隔5秒查询激活的情况，轮询方式,
     *
     * @return
     */
/*    public static Observable<HttpResult<DeviceMSG>> createIntervalQueryDeviceSafe() {
        return Observable.create(subscriber ->
                Schedulers.io().createWorker().schedulePeriodically(() -> {
                    RetrofitProvider.getInstance().getDeviceMsg().subscribe(result -> subscriber.onNext(result));
                }, 0, 3, TimeUnit.SECONDS)
        );
    }*/





    /**
     * 生成带有Logo二维码的Bitmap
     *
     * @param content
     * @return
     */
    public static Observable<Bitmap> createScanCode(final String content, final int width, final int height,final  int bitmapId) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap logoBitmap = BitmapUtils.decodeBitmapResource(BaseApplication.getInstance(), bitmapId);
                Bitmap bitmap = ZXingUtils.createQRImage(content, width, height, logoBitmap);
                subscriber.onNext(bitmap);
            }
        });
    }
}
