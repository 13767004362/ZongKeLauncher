package com.zongke.hapilolauncher.library.retrofit;

/**
 * Created by ${xingen} on 2017/11/7.
 * 一个通用的异常
 */

public class CommonException extends Exception {
    /**
     * 请求失败的code
     */
    public static final int RESULT_FAIRL= HttpConstance.RESULT_CODE_ERROR;
    /**
     * token过期的code
     */
    public static final int RESULT_EXPIRED= HttpConstance.RESULT_CODE_EXPIRED;

    /**
     * 一些其他未知的异常的code
     */
    public static final int RESULT_UNKNOWN_ERROR=100;
    /**
     *
     * 网络异常的code
     */
    public static final int RESULT_NET_ERROR=110;
    /**
     *
     * GSON解析异常的code
     */
    public static final int RESULT_PARSE_ERROR=120;
    /**
     * 异常的状态码
     */
    private  int code;
    /**
     * 异常的文字信息
     */
    private String errorMsg;

    public CommonException(String message, int code) {
        super(message);
        this.code = code;
        this.errorMsg = message;
    }
    public int getCode() {
        return code;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 静态工厂方式创建
     * @param code
     * @param message
     * @return
     */
    public static  CommonException newInstance( int code,String message){
        return  new CommonException(message,code);
    }
}
