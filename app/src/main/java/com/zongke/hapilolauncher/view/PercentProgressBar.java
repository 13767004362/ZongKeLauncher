package com.zongke.hapilolauncher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.zongke.hapilolauncher.utils.DisplayUtils;

/**
 * 带有百分比的ProgressBar
 * Created by llj on 2017/9/11.
 */
public class PercentProgressBar extends ProgressBar {
    private Paint mPaint;
    private Rect textRect;

    public PercentProgressBar(Context context) {
        super(context);
        init();
    }

    public PercentProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PercentProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    public PercentProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setTextSize(DisplayUtils.sp2px(getContext(),17.6f));
        textRect = new Rect();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String percent = getProgress() + "%";
        mPaint.getTextBounds(percent,0,percent.length(),textRect);
        canvas.drawText(percent, getWidth() / 2 - textRect.width()/2, getHeight()/2 + textRect.height()/2, mPaint);
    }
}
