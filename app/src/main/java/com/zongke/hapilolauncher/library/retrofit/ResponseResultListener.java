package com.zongke.hapilolauncher.library.retrofit;

/**
 * Created by ${xingen} on 2017/11/7.
 */

public interface ResponseResultListener<T> {
    /**
     * 结果的回调
     * @param t
     */
    void success(T t);
    /**
     * 异常的回调
     * @param e
     */
    void failure(CommonException e);
}
