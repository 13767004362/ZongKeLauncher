package com.zongke.hapilolauncher.library.retrofit;

/**
 * Created by ${xingen} on 2017/11/15.
 */

public class HttpResult <T>{
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private T data;
    private int code;
    private String msg;
}
