package com.zongke.hapilolauncher.library.retrofit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by ${xingen} on 2017/11/6.
 * <p>
 * 定义一个基本操作的Subscriber
 */

public class BaseSubscriber<T> extends Subscriber<HttpResult<T>> {
    private Context context;
    private ResponseResultListener<T> responseResultListener;
    public static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public BaseSubscriber(Context context, ResponseResultListener<T> resultListener) {
        this.context = context;

        this.responseResultListener = resultListener;
    }

    public boolean isCancelListener() {
        return getResultListener() == null ? true : false;
    }

    private ResponseResultListener<T> getResultListener() {
        return responseResultListener;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable error) {
        final Throwable e = error;
        postRunnable(new Runnable() {
                         @Override
                         public void run() {
                             if (isCancelListener()) {
                                 getResultListener().failure(handleError(e));
                             }
                         }
                     }
        );
    }

    /**
     * @param runnable
     */
    private void postRunnable(Runnable runnable) {
        mainHandler.post(runnable);
    }

    @Override
    public void onNext(HttpResult<T> r) {
        final HttpResult<T> result = r;
        switch (result.getCode()) {
            //请求成功
            case HttpConstance.RESULT_CODE_SUCCESS:
                postRunnable(new Runnable() {
                                 @Override
                                 public void run() {
                                     if (isCancelListener()) {
                                         T t = result.getData();
                                         getResultListener().success(t);
                                     }
                                 }
                             }
                );
                break;
            //请求失败
            case HttpConstance.RESULT_CODE_ERROR:
                postRunnable(new Runnable() {
                                 @Override
                                 public void run() {
                                     if (isCancelListener()) {
                                         getResultListener().failure(CommonException.newInstance(CommonException.RESULT_FAIRL, result.getMsg()));
                                     }

                                 }
                             }
                );
                break;
            //token过期
            case HttpConstance.RESULT_CODE_EXPIRED:
                postRunnable(new Runnable() {
                    @Override
                    public void run() {

                        if (isCancelListener()) {
                            getResultListener().failure(CommonException.newInstance(CommonException.RESULT_EXPIRED, "token过期"));

                        }

                    }
                });
                break;
            //默认的状态，处理特殊情况
            default:
                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        if (isCancelListener()) {
                            getResultListener().failure(CommonException.newInstance(result.getCode(), result.getMsg()));
                        }
                    }
                });
        }
    }


    /**
     * 处理常见的异常
     *
     * @param e
     * @return
     */
    protected CommonException handleError(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
            return CommonException.newInstance(CommonException.RESULT_NET_ERROR, "网络异常");
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
            return CommonException.newInstance(CommonException.RESULT_NET_ERROR, "网络异常");
        } else if (e instanceof UnknownHostException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
            return CommonException.newInstance(CommonException.RESULT_NET_ERROR, "网络异常");
        } else if (e instanceof SocketException) {
            Toast.makeText(context, "error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return CommonException.newInstance(CommonException.RESULT_NET_ERROR, "网络异常");
        } else if (e instanceof JsonSyntaxException) {
            return CommonException.newInstance(CommonException.RESULT_PARSE_ERROR, " Json解析: " + e.getMessage());
        } else {
            return CommonException.newInstance(CommonException.RESULT_UNKNOWN_ERROR, e.getMessage());
        }
    }
}
