package com.zongke.hapilolauncher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by ${xingen} on 2017/9/6.
 * <p>
 * 定义一个描边的TextView
 * <p>
 *  思路：
 *
 * 采用描边色的文字和内部色的文字叠加，形成一个描边效果。
 */

public class StrokeTextView extends AppCompatTextView {
    private int currentMode = MODE_DEFAULT;
    public static final int MODE_STROKE = 2;
    public static final int MODE_DEFAULT = 1;

    public StrokeTextView(Context context) {
        super(context);
    }
    public StrokeTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if (currentMode == MODE_STROKE) {
            drawStrokeText(canvas);
        }
        super.onDraw(canvas);
    }
    /**
     * 绘制描边色的字体
     *
     * @param canvas
     */
    private void drawStrokeText(Canvas canvas) {
        if (this.strokePaint == null) {
            this.strokePaint = new Paint();
        }
        //拷贝原本TextView中Paint一些属性
        Paint defaultPaint=getPaint();
        this.strokePaint.set(defaultPaint);
        //根据需求，定义描边色的画笔
        this.strokePaint.setStyle(Paint.Style.STROKE);
        this.strokePaint.setColor(strokeColor);
        this.strokePaint.setStrokeWidth(strokeWidth);
        //绘制描边色的文本
        String text = getText().toString();
        canvas.drawText(text, (getWidth() - strokePaint.measureText(text)) / 2, getBaseline(), strokePaint);
    }
    private Paint strokePaint;
    private int strokeColor;
    private int strokeWidth;
    /**
     *
     *  设置描边的特征，颜色，描边的宽度
     *
     * @param mode
     * @param strokeColor
     * @param strokeWidth
     */
    public void setModeStroke(int mode,int strokeColor,int strokeWidth) {
        if (mode==MODE_DEFAULT) return;
        this.currentMode = mode;
        this.strokeColor=strokeColor;
        this.strokeWidth=strokeWidth;
    }
}
