package com.zongke.hapilolauncher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import static com.bumptech.glide.request.target.Target.SIZE_ORIGINAL;

/**
 * Created by ${xinGen} on 2017/9/5.
 * <p>
 *
 *
 *  自定义一个横向自动滚动的View，支持左右无限循环转动。
 *
 *
 */
/**/
public class HorizontalScrollBGView extends View implements ScrollResponseCompact {
    private final String tag = HorizontalScrollBGView.class.getSimpleName();
    /**
     * 每次的移动距离
     */
    private final int distance=6;
    /**
     * 创建画笔
     */
    private Paint paint;
    private DrawFilter pfdf;
    /**
     * 偏移量
     */
    private int xOffset = 2140;
    /**
     * 拼接的Bitmap
     */
    private Bitmap startStitchingBitmap,endStitchingBitmap;
    /**
     * 向着左边移动，或者向着右边移动的模式
     */
    public static final int MODE_MOVE_LEFT = 1;

    public static final int MODE_MOVE_RIGHT = 2;
    /**
     * 原始图片
     */
    private Bitmap originBitmap;
    /**
     * 原始图片的宽高
     */
    private int bitmapWidth, bitmapHeight;

    /**
     * 采用Glide加载整个图片,原始图片
     *
     * @param imageId
     */
    private int originWidth = 1635;
    private int originHeight = 540;

    //图片处于最左边，向右拉不动了。
    private int currentMode = MODE_MOVE_RIGHT;

    public HorizontalScrollBGView(Context context) {
        super(context);
        initConfig();
    }

    public HorizontalScrollBGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initConfig();
    }
    /**
     * 初始化配置
     */
    private void initConfig() {
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.pfdf = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //防锯齿
        canvas.setDrawFilter(this.pfdf);
        //先回收原本的绘制bitmap
        recycleDrawBitmap();
        if (originBitmap == null) return;
        int width = getWidth();
        int height = getHeight();
        if (width > bitmapWidth) {
            canvas.drawBitmap(originBitmap, getMatrix(), paint);
        } else {
            if (xOffset>=bitmapWidth){
                xOffset=0;
            }else if (xOffset<0){
                xOffset= bitmapWidth+xOffset;
            }
            Log.i(tag,"  背景绘制： "+currentMode+" 偏移量： "+xOffset+" 图片宽度："+bitmapWidth+" 手机宽度： "+width);
            if (currentMode==MODE_MOVE_RIGHT){//向着右边滑动
                    if (xOffset+width<=bitmapWidth){
                        this.startStitchingBitmap = createSpecifyBitmap(originBitmap, xOffset, 0, width, height > bitmapHeight ? bitmapHeight : height);
                        canvas.drawBitmap(startStitchingBitmap, getMatrix(), paint);
                    }else {//这张图片剩余部分，不够填充手机屏幕，采用拼接方式
                       this.startStitchingBitmap=createSpecifyBitmap(originBitmap,xOffset,0,bitmapWidth-xOffset, height > bitmapHeight ? bitmapHeight : height);
                        canvas.drawBitmap(startStitchingBitmap,getMatrix(),paint);
                        //拼接第二张图片
                        Rect rect=new Rect(startStitchingBitmap.getWidth(),0,width,height);
                        endStitchingBitmap=createSpecifyBitmap(originBitmap,0,0,(xOffset+width-bitmapWidth),height > bitmapHeight ? bitmapHeight : height);
                        canvas.drawBitmap(endStitchingBitmap,null,rect,paint);
                    }
            }else {//向着左边滑动
                if (bitmapWidth-xOffset>=width){
                    this.startStitchingBitmap = createSpecifyBitmap(originBitmap, xOffset, 0, width, height > bitmapHeight ? bitmapHeight : height);
                    canvas.drawBitmap(startStitchingBitmap, getMatrix(), paint);
                }else {
                    this.startStitchingBitmap=createSpecifyBitmap(originBitmap,xOffset,0,bitmapWidth-xOffset, height > bitmapHeight ? bitmapHeight : height);
                    canvas.drawBitmap(startStitchingBitmap,getMatrix(),paint);
                    //拼接第二张图片
                    Rect rect=new Rect(startStitchingBitmap.getWidth(),0,width,height);
                    endStitchingBitmap=createSpecifyBitmap(originBitmap,0,0,(xOffset+width-bitmapWidth),height > bitmapHeight ? bitmapHeight : height);
                    canvas.drawBitmap(endStitchingBitmap,null,rect,paint);
                }
            }
            //先回收原本的绘制bitmap
            recycleDrawBitmap();
        }
    }

    /**
     * 创建指定的Bitmap
     *
     * @param resourceBitmap
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    private Bitmap createSpecifyBitmap(Bitmap resourceBitmap, int x, int y, int width, int height) {
        return Bitmap.createBitmap(resourceBitmap, x, y, width, height);
    }
    /**
     * 回收原本拼接的Bitmap
     */
    private void recycleDrawBitmap() {
        if (startStitchingBitmap != null) {
            startStitchingBitmap.recycle();
            startStitchingBitmap = null;
        }
        if (endStitchingBitmap!=null){
            endStitchingBitmap.recycle();
            endStitchingBitmap=null;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (originBitmap != null && currentStyle == style_adaptive) {//高度以原始图片的高度为准。
            setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(bitmapHeight, MeasureSpec.getMode(heightMeasureSpec)));
        }
    }
    /**
     * 恢复原本配置
     */
    private void recoverConfig() {
        handler.removeMessages(110);
        if (originBitmap != null) {
            originBitmap.recycle();
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recycleDrawBitmap();
    }
    private int currentStyle = style_adaptive;
    public static final int style_adaptive = 6;
    public void setImageBitmap(int imageId) {
        loadBitmap(imageId);
    }
    private void loadBitmap(int imageId) {
        int targetWidth;
        int targetHeight;
        if (getWidth() > originWidth) {
            targetWidth = (int) (getWidth() / ((float) 720 / 1635));
            targetHeight = (int) (targetWidth * ((float) originHeight / originWidth));
        } else {
            targetWidth = SIZE_ORIGINAL;
            targetHeight = SIZE_ORIGINAL;
        }
        // Log.i(tag, " 目标图片宽高 " + targetWidth + " " + targetHeight + " 控件的宽度 " + getWidth());
        Glide.with(getContext()).load(imageId).asBitmap().into(new SimpleTarget<Bitmap>(targetWidth, targetHeight) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> transition) {
                originBitmap = resource;
                bitmapHeight = originBitmap.getHeight();
                bitmapWidth = originBitmap.getWidth();
                handler.obtainMessage(110).sendToTarget();
            }
        });
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MODE_MOVE_LEFT://左边滑动
                    currentMode=msg.what;
                    xOffset+=distance;
                    invalidate();
                    break;
                case MODE_MOVE_RIGHT://右边滑动
                    currentMode=msg.what;
                    xOffset-=distance;
                    invalidate();
                    break;
                case 110:
                    invalidate();
                    break;
            }
            return false;
        }
    });
    /**
     * 默认到了，最左端。
     */
    private boolean isScrollBorder = true;
    @Override
    public boolean isScrollBorder() {
        return isScrollBorder;
    }
    @Override
    public int getScrollMode() {
        return currentMode;
    }
    @Override
    public boolean scrollPosition(int move_mode, float rotation) {
        //移除原本的信息
        handler.removeMessages(MODE_MOVE_RIGHT);
        handler.removeMessages(MODE_MOVE_LEFT);
        //执行最新的信息
        handler.obtainMessage(move_mode).sendToTarget();
        return  isScrollBorder;
    }

}
