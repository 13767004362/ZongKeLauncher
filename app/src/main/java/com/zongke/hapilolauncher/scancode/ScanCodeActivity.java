package com.zongke.hapilolauncher.scancode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zhongke.account.db.Constance;
import com.zhongke.account.model.AccountInfo;
import com.zhongke.account.util.Util;
import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.base.BaseApplication;
import com.zongke.hapilolauncher.db.entity.DeviceMSG;
import com.zongke.hapilolauncher.homeScreen.MainActivity;
import com.zongke.hapilolauncher.library.retrofit.HttpConstance;
import com.zongke.hapilolauncher.library.retrofit.HttpResult;
import com.zongke.hapilolauncher.library.retrofit.RetrofitProvider;
import com.zongke.hapilolauncher.library.rxjava.ObservableBuilder;
import com.zongke.hapilolauncher.utils.ConversionObjectUtils;
import com.zongke.hapilolauncher.utils.DisplayUtils;
import com.zongke.hapilolauncher.utils.SystemUIManager;
import com.zongke.hapilolauncher.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by ${xingen} on 2017/11/15.
 * 生成二维码，且激活扫描界面
 */

public class ScanCodeActivity extends RxAppCompatActivity {

    private final String TAG = ScanCodeActivity.class.getSimpleName();
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        createScanCode();
        initView();
        startQueryDevice();
        //   startEngineService();
    }

    /**
     * 开启远程的引擎服务
     */
    private void startEngineService() {
        Intent intent = new Intent("com.zk.server.AllService");
        intent.setPackage("com.zk.server");
        sendBroadcast(intent);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        this.imageView = (ImageView) findViewById(R.id.qd_img);
    }




  /*  public static final String DEVICE_CODE = "A6754123D";*/
    /**
     * 采用随机数的设备号
     */
    public String DEVICE_CODE = UUID.randomUUID().toString();

    /**
     * 生成二维码扫描的内容
     *
     * @return
     */
    public String getScanContent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(HttpConstance.BASE_URL);
        stringBuilder.append("/user/get_user_info_by_devicecode?");
        stringBuilder.append("deviceCode=");
        stringBuilder.append(DEVICE_CODE);
        return stringBuilder.toString();
    }

    /**
     * 生成二维码，且加载到对应的图片中
     */
    private void createScanCode() {
        String url = getScanContent();
        int width = DisplayUtils.dip2px(this.getApplicationContext(), 300);
        int height = width;

        ObservableBuilder.createScanCode(url, width, height, R.mipmap.ic_launcher)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object bitmap) {
                        imageView.setImageBitmap((Bitmap) bitmap);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtils.showToast(getApplicationContext(), "生成二维码错误");
                    }
                });
    }

    /**
     * 开始查询设备信息,轮询方式
     */

    private void startQueryDevice() {

        executeNetWork();
    }

    private Map<String, Object> params;

    private Map<String, Object> createParams() {
        if (params == null) {
            params = new HashMap<>();
            params.put("deviceCode", DEVICE_CODE);
        }
        return params;
    }

    private void executeNetWork() {
        Observable.interval(0, 3, TimeUnit.SECONDS)
                //进行网络请求
                .flatMap(new Func1<Long, Observable<HttpResult<DeviceMSG>>>() {
                    @Override
                    public Observable<HttpResult<DeviceMSG>> call(Long aLong) {
                        return RetrofitProvider.getInstance().getDeviceMsg(createParams()).retry();
                    }
                })
                //直到请求成功
                .takeUntil(new Func1<HttpResult<DeviceMSG>, Boolean>() {
                    @Override
                    public Boolean call(HttpResult<DeviceMSG> result) {
                        return result.getCode() == 1;
                    }
                })
                .map(new Func1<HttpResult<DeviceMSG>, Boolean>() {
                    @Override
                    public Boolean call(HttpResult<DeviceMSG> result) {
                        if (result.getCode() == 1) {
                            AccountInfo accountInfo = ConversionObjectUtils.conversionDevice(result.getData());
                            Log.i("llj", "设备被激活，token------>>>" + accountInfo.getToken());
                            Log.i("llj", "设备被激活，name------>>>" + accountInfo.name);
                            Log.i("llj", "设备被激活，nickName------>>>" + accountInfo.nickName);
                            BaseApplication.getInstance().getContentResolver().insert(Constance.ACCOUNT_URI, Util.getContentValues(accountInfo));
                        }
                        return result.getCode() == 1 ? true : false;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "查询到过程中发生异常");
                    }
                    @Override
                    public void onNext(Object o) {
                        boolean result = (Boolean) o;
                        if (result) {
                            Log.i(TAG, "查询到激活设备");
                            MainActivity.openActivity(ScanCodeActivity.this);
                            finish();
                        } else {
                            Log.i(TAG, "查询到未激活设备");
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 开启
     *
     * @param context
     */
    public static void openActivity(Context context) {
        context.startActivity(new Intent(context, ScanCodeActivity.class));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        SystemUIManager.setStickyStyle(getWindow());
        super.onWindowFocusChanged(hasFocus);
    }
}
