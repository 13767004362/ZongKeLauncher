package com.zongke.hapilolauncher.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.zongke.hapilolauncher.utils.Tools;


/**
 * 倒计时控件
 * Created by lilijun on 2016/9/8.
 */
public class CountClock extends AppCompatTextView {
    /**
     * 倒计时消息
     */
    private final int COUNT_DOWN_MSG = 1;
    /**
     * 改变样式闪烁消息
     */
    private final int CHANGE_TYPE = 2;
    /**
     * 倒计时总次数
     */
    private int totalCount = 30;
    /**
     * 默认的显示文本
     */
    private String defaultText;

    private OnCountDownDoneListener listener;

    boolean change = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == COUNT_DOWN_MSG) {
                if (--totalCount < 0) {
                    // 倒计时完成
                    stop();
                    isDoneMark=true;
                    if (listener != null) {
                        listener.onDone();
                    }
                } else {
                    // 倒计时未完成
                    setText(Tools.formatTime(totalCount));
                    sendEmptyMessageDelayed(COUNT_DOWN_MSG, 1000);
                    if(totalCount == 10){
                        handler.sendEmptyMessageDelayed(CHANGE_TYPE, 400);
                    }
                }
            } else if (msg.what == CHANGE_TYPE) {
                if (change) {
                    change = false;
                    //这个是透明，=看不到文字
                    setTextColor(Color.TRANSPARENT);
                } else {
                    change = true;
                    setTextColor(Color.RED);
                }
                if(totalCount > 0){
                    handler.sendEmptyMessageDelayed(CHANGE_TYPE, 400);
                }else {
                    change = true;
                    setTextColor(Color.RED);
                }
            }
        }
    };

    public CountClock(Context context) {
        super(context);
        setEnabled(false);
    }

    public CountClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEnabled(false);
    }

    public CountClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEnabled(false);
    }

//    public CountDownTextView2(Context context, AttributeSet attrs, int defStyleAttr, int
//            defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        setEnabled(false);
//    }


    /**
     * 设置监听倒计时完成广播
     *
     * @param listener
     */
    public void setOnDoneListener(OnCountDownDoneListener listener) {
        this.listener = listener;
    }
    private boolean isDoneMark;
    /**
     * 开始倒计时
     *
     * @param totalCount 倒计时的总秒数
     */
    public void start(int totalCount) {
        this.isDoneMark=false;
        setVisibility(VISIBLE);
        this.totalCount = totalCount;
        this.defaultText = getText().toString();
        setText(Tools.formatTime(totalCount));
        setTextColor(Color.parseColor("#ffffff"));
        handler.sendEmptyMessageDelayed(COUNT_DOWN_MSG, 1000);
    }

    public void stop() {
//        setVisibility(GONE);
        handler.removeMessages(COUNT_DOWN_MSG);
        handler.removeMessages(CHANGE_TYPE);
    }

    public void reset() {
        this.totalCount = 0;
        setText("00:00");
        setTextColor(Color.parseColor("#ffffff"));
        handler.removeMessages(COUNT_DOWN_MSG);
    }

    /**
     * 倒计时完成接口
     */
    public interface OnCountDownDoneListener {
        void onDone();
    }

    public boolean isDoneMark() {
        return isDoneMark;
    }
}
