package com.zongke.hapilolauncher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Karma on 2017/9/7.
 * 类描述：
 */

public class SeekBarView extends View {
    private static final String TAG = "SeekBarView";
    private Paint mPaint;
    private int progress;

    public SeekBarView(Context context) {
        super(context);
        initPaint();
    }

    public SeekBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
            height = heightSize;
            setMeasuredDimension(width, height);

        } else if (widthMode == MeasureSpec.AT_MOST) {

        } else if (widthMode == MeasureSpec.UNSPECIFIED) {

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.parseColor("#27404d"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        RectF rectF = new RectF(10, 2, 120, 30);
        canvas.drawRoundRect(rectF, 15, 15, mPaint);

        mPaint.setColor(Color.parseColor("#83c967"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
        RectF rectF1 = new RectF(10, 2, progress * 1f + 20f, 30);
        canvas.drawRoundRect(rectF1, 15, 15, mPaint);

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(30);
        mPaint.setTextSize(20f);
        String text = progress + "%";
        canvas.drawText(text, progress * 0.5f, 22f, mPaint);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
    }


    public void setProgress(int progress) {
        this.progress = progress;
        if (progress == 100) {

        }
        invalidate();
    }

}
