package com.zongke.hapilolauncher.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.db.entity.FunctionList_Entity;
import com.zongke.hapilolauncher.utils.DisplayUtils;
import com.zongke.hapilolauncher.utils.TypefaceUtils;
import com.zongke.hapilolauncher.utils.ViewUtils;

import java.util.List;

import static com.zongke.hapilolauncher.utils.DisplayUtils.dip2px;


/**
 * Created by ${xingen} on 2017/6/29.
 * <p>
 * 定制一个旋转的环形菜单。
 */

public class RotatingRingMenu extends ViewGroup {
    private static final String TAG = RotatingRingMenu.class.getSimpleName();
    private int whiteColor, blueColor, redColor;
    private Context context;
    private Bitmap circle_small_bg_bitmap, circle_large_bg_bitmap;
    /**
     * ViewGroup默认宽高
     */
    private int default_with = 471;
    /**
     * 美工设计图上的
     */
    private float density = 1.5f;
    /**
     * 滚动类
     */
    private Scroller scroller = null;
    /**
     * 处理onTouch()中事件。
     */
    private GestureDetector gestureDetector;
    /**
     * 属性动画，这里处理滚动
     */
    private ValueAnimator mScrollAnimator;

    /**
     * 转动的子View
     */
    private SectorCircleItem sectorCircleView;
    /**
     * 处理，View本身的转动
     */
    private ObjectAnimator rotateAnimator;
    /**
     * 当前item的角度
     */
    private int currentItemAngle;
    private int currentItem;
    /**
     * 滚动视图的区域面积
     */
    private RectF scrollViewBounds;
    private CenterCircleItem centerCircleItem;
    /**
     * 初始的飞行速度除以的数，这里是除以4
     */
    public static final int FLING_VELOCITY_DOWNSCALE = 4;
    private int currentViewRotation;

    public RotatingRingMenu(Context context) {
        super(context);
        this.context = context;
        initConfig();
        addChildView();
    }

    public RotatingRingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initConfig();
        addChildView();
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
    }

    /**
     * 添加配置
     */
    private void initConfig() {
        //   setLayerToSW(this);
        //初始化色值
        this.whiteColor = Color.parseColor("#ffffff");
        this.blueColor = Color.parseColor("#39b9e6");
        this.redColor = Color.parseColor("#e74117");

        //创建一个Scroller，来处理fling 手势。
        this.scroller = new Scroller(context, null, true);
        //创建一个 gestrue Dector来处理onTouch()中信息。
        this.gestureDetector = new GestureDetector(RotatingRingMenu.this.getContext(), new ViewGroupGestureListener());
        this.gestureDetector.setIsLongpressEnabled(false);
        /**
         *  scroller不提供动画能力，它只提供一些值，当需要使用到的。
         *   因此，需要有一个方法来调用从开始到结束的每一帧。
         *   采用ValueAnimator对象生成每个动画的回调，而不使用动画值。
         */
        this.mScrollAnimator = ValueAnimator.ofFloat(0, 1);
        this.mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.i(TAG, " ScrollAnimator  u 更新 监听器 相应，执行更新操作  ");
                tickScrollAnimation();
            }
        });
        /**
         *  这里的属性名，对应setViewRotation()
         */
        this.rotateAnimator = ObjectAnimator.ofInt(RotatingRingMenu.this, "ViewRotation", 0);
        this.rotateAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        /**
         *         加载bitmap
         */
        this.circle_small_bg_bitmap = decodeBitmap(R.mipmap.menu_small_circle_bg);
        this.circle_large_bg_bitmap = decodeBitmap(R.mipmap.menu_large_circle_bg);
/**

        int targetWidth= DisplayUtils.dip2px(context,200);
        Log.i(TAG,"计算可知 "+targetWidth);
        this.circle_large_bg_bitmap = decodeBitmap(R.mipmap.menu_large_circle_bg,targetWidth,targetWidth);
 *
 */

    }

    /**
     * 添加子View
     */
    private void addChildView() {
        sectorCircleView = new SectorCircleItem(this.context);
        this.addView(sectorCircleView);
        sectorCircleView.rotateTo(currentViewRotation);

        centerCircleItem = new CenterCircleItem(this.context);
        this.addView(centerCircleItem);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(TAG, " 父容器 onMeasure ");
        int withSize = dip2px(context, default_with * 2 / density);
        int heightSize = withSize;
        setMeasuredDimension(measureSize(withSize, widthMeasureSpec), measureSize(heightSize, heightMeasureSpec));
    }

    /**
     * 重新计算宽高度值
     *
     * @param defaultSize
     * @param measureSpec
     * @return
     */
    private int measureSize(int defaultSize, int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {           //已知控件的大小
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            //遵循 AT_MOST,result不能大于specSize
            result = Math.min(result, specSize);
        } else {        //设置一个默认值
            result = defaultSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //获取到水平和竖直的Padding值
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingBottom() + getPaddingTop());
        //系统分配到父容器中的宽高。
        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        int centerLength = circle_small_bg_bitmap.getWidth();
        //int centerLength = DisplayUtils.dip2px(context, (CenterCircleItem.circleRadius * 2 / density) * 2);
        int left_circle = (int) (ww / 2 - centerLength / 2);
        int top_circle = (int) (hh / 2 - centerLength / 2);
        int right_circle = left_circle + centerLength;
        int bottom_circle = top_circle + centerLength;
        Log.i(TAG, " 父容器的布局改变 宽 " + ww + " 高 " + hh + " 点 " + left_circle + " " + top_circle + " " + right_circle + " " + bottom_circle);
        //layout（left,top,right,bottom）位于父容器ViewGroup的上边和左边的距离。
        centerCircleItem.layout(left_circle, top_circle, right_circle, bottom_circle);

        scrollViewBounds = new RectF(0.0f, 0.0f, ww, hh);
        //将矩形偏移到指定的（left,top）位置，且保持宽高不变。
        scrollViewBounds.offset(getPaddingLeft(), getPaddingTop());
        // 计算实际饼图的绘制位置。
        sectorCircleView.layout((int) scrollViewBounds.left, (int) scrollViewBounds.top, (int) scrollViewBounds.right, (int) scrollViewBounds.bottom);
        //设置旋转和缩放的转动点
        sectorCircleView.setPivot(scrollViewBounds.width() / 2, scrollViewBounds.height() / 2);
        sectorCircleView.layout(0, 0, w, h);
        //重新转动
        calcutionItemData();
    }

    /**
     * Fling监听，中触发动画，进行变换动作。
     */
    private void tickScrollAnimation() {
        if (!scroller.isFinished()) {
            Log.i(TAG, " scroll computeScrollOffset()  计算 ");
            //计算
            this.scroller.computeScrollOffset();
            //滚动角度
            setViewRotation(this.scroller.getCurrY());
        } else {
            Log.i(TAG, " scroll 结束,停止滚动  ");
            this.mScrollAnimator.cancel();
            onScrollFinished();
        }
    }


    /**
     * 对View设置转动角度
     *
     * @param
     */
    public void setViewRotation(int viewRotation) {
        this.currentViewRotation = (viewRotation % 360 + 360) % 360;
        this.sectorCircleView.rotateTo(currentViewRotation);
    }

    /**
     * 重写onTouchEvent()
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         *   GestureDetector拦截事件
         */
        boolean result = gestureDetector.onTouchEvent(event);
        /**
         *  result为false ,表示GestureDetector不做处理的事件
         */
        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) { //当用户滚动过程中，抬起手指停止滚动。
                Log.i(TAG, " GestureDetector 不处理 ");
                stopScrolling();
                result = true;
            }
        }
        return result;
    }

    /**
     * 停止所有的滚动
     */
    private void stopScrolling() {
        //滚动结束
        this.scroller.forceFinished(true);
        onScrollFinished();
    }

    /**
     * 是否有动画或者正在滚动
     *
     * @return
     */
    private boolean isAnimationRuning() {
        return !this.scroller.isFinished();
    }

    /**
     * 布局硬件加速
     *
     * @param v
     */
    private void setLayerToHW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    /**
     * 布局软件层加速
     *
     * @param v
     */
    private void setLayerToSW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    /**
     * 一个辅助方法： 将（x,y）滚动向量转换成旋转的标量
     *
     * @param dx 当前滚动向量的x分量
     * @param dy 当前滚动向量的y分量
     * @param x  相对于中心，当前触摸的x位置
     * @param y  相对于中心，当前触摸的y位置
     * @return 滚动角位置变化的标量
     */
    private static float vectorToScalarScroll(float dx, float dy, float x, float y) {
        float l = (float) Math.sqrt(dx * dx + dy * dy);
        float crossX = -y;
        float crossY = x;
        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);
        return l * sign;
    }

    /**
     * 手势监控，拖拽和滑动事件。
     */
    private class ViewGroupGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            //滚动
            float scrollTheta = vectorToScalarScroll(distanceX, distanceY, e2.getX() - scrollViewBounds.centerX(), e2.getY() - scrollViewBounds.centerY());

            Log.i(TAG, " onScroll状态 " + "上次的转动角度" + currentViewRotation + " 当前计算的： " + scrollTheta / FLING_VELOCITY_DOWNSCALE);

            setViewRotation(currentViewRotation - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 使用Scroller建立Fling手势
            float scrollTheta = vectorToScalarScroll(velocityX, velocityY, e2.getX() - scrollViewBounds.centerX(), e2.getY() - scrollViewBounds.centerY());
            scroller.fling(0, (int) currentViewRotation, 0, (int) scrollTheta / FLING_VELOCITY_DOWNSCALE, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            Log.i(TAG, " onFling状态  " + "上次的转动角度" + currentViewRotation + " 当前计算的： " + (scrollTheta / FLING_VELOCITY_DOWNSCALE));
            mScrollAnimator.setDuration(scroller.getDuration());
            mScrollAnimator.start();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(TAG, " onDown状态 ");
            // sectorCircleView.accelerate();
            if (isAnimationRuning()) {
                stopScrolling();
            }
            return true;
        }
    }


    private FunctionList_Entity functionList_entity;
    /**
     * 总的技能个数
     */
    private int totalSize;

    /**
     * 添加绘制需要的数据
     *
     * @param functionList_entity
     */
    public void addData(FunctionList_Entity functionList_entity) {
        Log.i(TAG, " addData " + functionList_entity);
        this.functionList_entity = functionList_entity;
        for (FunctionList_Entity.ListBean listBean : functionList_entity.getList()) {
            calculationTotalSize(listBean);
        }
        calcutionItemData();
    }

    /**
     * 叠加计算总个数
     *
     * @param listBean
     */
    public void calculationTotalSize(FunctionList_Entity.ListBean listBean) {
        totalSize += listBean.getItemList().size();
    }

    /**
     * 计算数据,当数据源发生改变的时候
     */
    public void calcutionItemData() {
        if (this.functionList_entity == null) return;
        List<FunctionList_Entity.ListBean> list = this.functionList_entity.getList();
        int currentAngle = 0;
        for (FunctionList_Entity.ListBean listBean : list) {
            //以上一个饼图块的结束角度作为起始角度
            listBean.mStartAngle = currentAngle;
            //结束的角度等于=值/总值*360
            listBean.mEndAngle = (int) ((float) currentAngle + (float) listBean.getItemList().size() * 360.0f / (float) totalSize);
            currentAngle = listBean.mEndAngle;
        }
        calcationCurrentItem();
        onScrollFinished();
    }

    /**
     * 当用户结束滚动动作时候调用
     */
    private void onScrollFinished() {
        sectorCircleView.decelerate();
    }

    /**
     * 计算当前item,
     */
    private void calcationCurrentItem() {
        Log.i(TAG, " calcationCurrentItem");
        int pointerAngle = (currentItemAngle + 360 + currentViewRotation) % 360;
        for (int i = 0; i < functionList_entity.getList().size(); i++) {
            FunctionList_Entity.ListBean listBean = functionList_entity.getList().get(i);
            if (pointerAngle >= listBean.mStartAngle && pointerAngle <= listBean.mEndAngle) {
                notifyCurrentItemChange(i);
                break;
            }
        }
    }

    /**
     * 当前item改变的时候，发生通知和更新
     *
     * @param currentItem
     */
    private void notifyCurrentItemChange(int currentItem) {
        Log.i(TAG, " notifyCurrentItemChange " + currentItem);
        this.currentItem = currentItem;
        //可以获取到当前Item的监听事件。
        invalidate();
    }

    /**
     * 扇形和圆形
     */
    private class SectorCircleItem extends View {
        private Paint paint;
        private Context context;
        private int textSize = 24;
        private float textSize_large=21.3f;
        //内圆的半径,扇形
        private int medium_circle_radius = 136;
        //环形圆圈的半径
        private int small_circle_radius = 27;
        //环形圆圈中心到视图中心的距离
        private int hypotenuse_size = 200;
        private int shallowColor, deepColor;
        private RectF mBounds;
        private int small_circle_color;
        private int shadow_color;

        public SectorCircleItem(Context context) {
            super(context);
            this.context = context;
            initConfig();
        }

        /**
         * 初始化配置
         */
        private void initConfig() {
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            shallowColor = Color.parseColor("#f1e3bf");
            deepColor = Color.parseColor("#ffc100");
            small_circle_color = Color.parseColor("#fff09e");
            shadow_color = Color.parseColor("#ebc278");
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.i(TAG, " 滚动的View在onDraw ");
            drawFixedCircle(canvas);
            drawAllGraphs(canvas);
            drawBitmap(canvas);
        }
        private void drawBitmap(Canvas canvas){
            paint.reset();
            //绘制条形的圆圈背景
            float top = (float) (getHeight() - circle_large_bg_bitmap.getHeight()) / 2;
            float left = (float) (getWidth()- circle_large_bg_bitmap.getWidth()) / 2;
            paint.setAntiAlias(true);
            Log.i(TAG," 外阴影圆圈的大小 "+circle_large_bg_bitmap.getHeight()+" "+circle_large_bg_bitmap.getWidth()+" "+top+" "+left);
            canvas.drawBitmap(circle_large_bg_bitmap, left, top, paint);
        }

        /**
         * 绘制弧线，也可以绘制扇形
         *
         * @param canvas
         */
        private void drawAllGraphs(Canvas canvas) {
            //椭圆的边界来绘制弧的形状和大小
            RectF rectF = createRectF((medium_circle_radius * 2 +12)/ density);
            //中心到环形圆圈中文字的距离
            RectF rectF2 = createRectF(193 * 2 / density);
            //中心到扇形中文字距离
            RectF rectF3 = createRectF(105 * 2 / density);
            for (int i = 0; i < functionList_entity.getList().size(); ++i) {
                FunctionList_Entity.ListBean listBean = functionList_entity.getList().get(i);
                drawRingArc(canvas, rectF, i, listBean);
                //  drawSmallShadow(canvas);
                drawRingText(canvas, rectF3, listBean);
                drawRingCircle(canvas, rectF2, listBean);
            }
        }

        //红色的外圆
        private int large_circle_radius = 165;
        //条形的外圆圈
        public static  final int large_circle_radius_2 = 146;
        //条形的内圆圈
        private int small_circle_radius_2 = 64;

        //超小圆圈的半径
        private int small_circle_radius_3 = 4;
        //中心点到超小圆圈的斜边距离
        private float small_hypotenuse_size = 154f;



        /**
         * 绘制固定样式
         */
        private void drawFixedCircle(Canvas canvas) {

            int width = getWidth();
            int height = getHeight();
            int x = width / 2;
            int y = height / 2;
            //绘制红色的外圆形
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(redColor);
            canvas.drawCircle(x, y, dip2px(context, large_circle_radius * 2 / density), paint);


           /* paint.setColor(shadow_color);
            canvas.drawCircle(x, y, DisplayUtils.dip2px(context, large_circle_radius_2 * 2 / density), paint);*/

            //绘制12个小圆圈
            float hypotenuse = dip2px(context, (small_hypotenuse_size * 2+6) / density);//斜边长
            float averageAngle = 360.0f / 18;
            int radius = dip2px(context, small_circle_radius_3 * 2 / density);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(small_circle_color);
            for (int i = 0; i < 18; ++i) {
                //计算角度
                int angle = (int) averageAngle * (i) + (int) averageAngle / 2;
                float cx = 0;
                float cy = 0;
                if (angle < 90) {
                    double dangle = Math.toRadians(angle);
                    cx = width / 2 + (float) Math.cos(dangle) * hypotenuse;
                    cy = height / 2 + (float) Math.sin(dangle) * hypotenuse;
                } else if (90 < angle && angle < 180) {
                    double dangle = Math.toRadians(angle - 90);
                    cx = width / 2 - (float) Math.sin(dangle) * hypotenuse;
                    cy = height / 2 + (float) Math.cos(dangle) * hypotenuse;
                } else if (180 < angle && angle < 270) {
                    double dangle = Math.toRadians(angle - 180);
                    cx = width / 2 - (float) Math.cos(dangle) * hypotenuse;
                    cy = height / 2 - (float) Math.sin(dangle) * hypotenuse;
                } else if (270 < angle) {
                    double dangle = Math.toRadians(angle - 270);
                    cx = width / 2 + (float) Math.sin(dangle) * hypotenuse;
                    cy = height / 2 - (float) Math.cos(dangle) * hypotenuse;
                } else if (angle == 90) {
                    cx = width / 2;
                    cy = height / 2 + hypotenuse;
                } else if (angle == 270) {
                    cx = width / 2;
                    cy = height / 2 - hypotenuse;
                }
                canvas.drawCircle(cx, cy, radius, paint);
            }
        }

        /**
         * 创建区域面积
         *
         * @param distance
         * @return
         */
        private RectF createRectF(float distance) {
            int height = getHeight();
            int width = getWidth();
            RectF rectF = new RectF();
            int radius = dip2px(context, distance);
            rectF.top = height / 2 - radius;
            rectF.right = width / 2 + radius;
            rectF.left = width / 2 - radius;
            rectF.bottom = height / 2 + radius;
            return rectF;
        }

        /**
         * 绘制圆形中的扇形块
         *
         * @param canvas
         * @param rectF
         * @param i
         * @param listBean
         */
        private void drawRingArc(Canvas canvas, RectF rectF, int i, FunctionList_Entity.ListBean listBean) {
            //从右边中间开始为零度（对应手表的3点钟），从顺时针方向旋转
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(i == 3 || i == 1 || i == 5 ? deepColor : shallowColor);
            canvas.drawArc(rectF, listBean.mStartAngle, listBean.mEndAngle - listBean.mStartAngle, true, paint);
            // canvas.drawArc(rectF, 360 - listBean.mStartAngle, listBean.mEndAngle - listBean.mStartAngle, true, paint);
        }

        /**
         * 绘制阴影条形圆圈
         *
         * @param canvas
         */
        private void drawSmallShadow(Canvas canvas) {
            paint.setColor(shadow_color);
            //绘制条形的内圆
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, dip2px(context, small_circle_radius_2 * 2 / density), paint);
        }

        /**
         * 绘制扇形中的文本
         * .
         *
         * @param canvas
         * @param rectF
         * @param listBean
         */
        private void drawRingText(Canvas canvas, RectF rectF, FunctionList_Entity.ListBean listBean) {
            //绘制扇形中的文字
            paint.setColor(whiteColor);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(TypefaceUtils.getInstance().getTypeFace(getContext(),TypefaceUtils.font_roboto_bold));
            paint.setTextSize(DisplayUtils.sp2px(context, textSize_large));
            Path path = new Path();
            path.addArc(rectF, listBean.mStartAngle, listBean.mEndAngle - listBean.mStartAngle);
            //水平偏移量
            float hOffset = 0;
            //竖直偏移量
            float vOffset = 0;
            canvas.drawTextOnPath(listBean.getName(), path, hOffset, vOffset, paint);
        }

        /**
         * 绘制 环形的圆形
         * <p>
         * 这里从手表三点线作为0起点或者360点
         *
         * @param listBean
         * @param canvas
         */
        private void drawRingCircle(Canvas canvas, RectF rectF, FunctionList_Entity.ListBean listBean) {
            int height = getHeight();
            int width = getWidth();
            int size = listBean.getItemList().size();
            float averageAngle = (float) (listBean.mEndAngle - listBean.mStartAngle) / size;
            for (int i = 0; i < listBean.getItemList().size(); ++i) {
                FunctionList_Entity.ListBean.ItemListBean itemListBean = listBean.getItemList().get(i);
                //计算角度
                int angle = (int) (listBean.mStartAngle + averageAngle * i + averageAngle / 2);
                float radius = dip2px(context, small_circle_radius * 2 / density);//圆形半径
                float hypotenuse = dip2px(context, hypotenuse_size * 2 / density);//斜边长
                float cx = 0;
                float cy = 0;
                if (angle < 90) {
                    double dangle = Math.toRadians(angle);
                    cx = width / 2 + (float) Math.cos(dangle) * hypotenuse;
                    cy = height / 2 + (float) Math.sin(dangle) * hypotenuse;
                } else if (90 < angle && angle < 180) {
                    double dangle = Math.toRadians(angle - 90);
                    cx = width / 2 - (float) Math.sin(dangle) * hypotenuse;
                    cy = height / 2 + (float) Math.cos(dangle) * hypotenuse;
                } else if (180 < angle && angle < 270) {
                    double dangle = Math.toRadians(angle - 180);
                    cx = width / 2 - (float) Math.cos(dangle) * hypotenuse;
                    cy = height / 2 - (float) Math.sin(dangle) * hypotenuse;
                } else if (270 < angle) {
                    double dangle = Math.toRadians(angle - 270);
                    cx = width / 2 + (float) Math.sin(dangle) * hypotenuse;
                    cy = height / 2 - (float) Math.cos(dangle) * hypotenuse;
                } else if (angle == 90) {
                    cx = width / 2;
                    cy = height / 2 + hypotenuse;
                } else if (angle == 270) {
                    cx = width / 2;
                    cy = height / 2 - hypotenuse;
                }
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(blueColor);
                canvas.drawCircle(cx, cy, radius, paint);

                //绘制圆形中的文字
                paint.setFakeBoldText(true);
                paint.setColor(whiteColor);
                paint.setTypeface(TypefaceUtils.getInstance().getTypeFace(getContext(),TypefaceUtils.font_standing_cool));
                int text_size = DisplayUtils.sp2px(context, textSize);
                paint.setTextSize(text_size);
                paint.setTextAlign(Paint.Align.CENTER);
                Path path = new Path();
                //水平偏移量
                float hOffset = 0;
                //竖直偏移量
                float vOffset = 0;
                //添加闭合的顺时针圆形
                path.addArc(rectF, listBean.mStartAngle + i * averageAngle, averageAngle);
                canvas.drawTextOnPath(itemListBean.getName(), path, hOffset, vOffset, paint);
                //添加白边
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(whiteColor);
                paint.setStrokeWidth(dip2px(context, 2));
                canvas.drawCircle(cx, cy, radius + 2, paint);
            }
        }

        /**
         * 设置回转角度
         *
         * @param rotation
         */
        public void rotateTo(float rotation) {
            //控件围绕pivot点旋转的角度
            setRotation(rotation);
        }

        /**
         * view进行旋转和缩放的中心点。
         *
         * @param x
         * @param y
         */
        public void setPivot(float x, float y) {
            setPivotX(x);
            setPivotY(y);
        }

        /**
         * 关闭硬件加速（hardware acceleration），释放内存。
         */
        public void decelerate() {
            setLayerToSW(this);
        }

        /**
         * 开启硬件加速
         */
        public void accelerate() {
            setLayerToHW(this);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mBounds = new RectF(0, 0, w, h);
            Log.i(TAG, "  滚动 View 布局发生改变...");
            super.onSizeChanged(w, h, oldw, oldh);
        }
    }

    /**
     * 中心圆的view
     */
    private class CenterCircleItem extends View {
        private Paint paint;
        private Context context;
        public static final int circleRadius = 57;
        private int textSize = 18;
        //默认显示的字
        private String text = "我的家庭";


        public CenterCircleItem(Context context) {
            super(context);
            this.context = context;
            initConfig();
        }

        /**
         * 初始化配置
         */
        private void initConfig() {
            this.paint = new Paint();
            this.paint.setAntiAlias(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            Log.i(TAG, "中心View  onDraw");
            //绘制图片
            drawBitmap(canvas);
            //绘制字体
            String content = functionList_entity == null || TextUtils.isEmpty(functionList_entity.getFamilyName()) ? text : functionList_entity.getFamilyName();
            drawText(content, canvas);
        }

        /**
         * 绘制bitmap
         *
         * @param canvas
         */
        void drawBitmap(Canvas canvas) {
            float top = 0;
            float left = top;
            canvas.drawBitmap(circle_small_bg_bitmap, top, left, paint);
        }

        /**
         * 绘制圆形
         *
         * @param canvas
         */
        void drawCircle(Canvas canvas) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(redColor);
            int x = getWidth() / 2;
            int y = getHeight() / 2;
            canvas.drawCircle(x, y, dip2px(context, circleRadius * 2 / density), paint);
        }

        /**
         * 绘制文字
         *
         * @param content
         * @param canvas
         */
        void drawText(String content, Canvas canvas) {
            paint.setFakeBoldText(true);
            paint.setColor(whiteColor);
            paint.setTextSize(DisplayUtils.sp2px(context, textSize));
            float x2 = (getWidth() - ViewUtils.calculationDigitalWith(paint, content)) / 2;

            float y2 = getHeight() /3  + ViewUtils.calculationDigitalHeight(paint) / 2;
            canvas.drawText(content, x2, y2, paint);
        }
    }

    private Bitmap decodeBitmap(int imageId) {
        return decodeBitmap(imageId, 0, 0);
    }

    /**
     * 根据资源，加载bitmap
     *
     * @param imageId
     * @return
     */
    private Bitmap decodeBitmap(int imageId, int targetWidth, int targetHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (targetHeight > 0 || targetHeight> 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), imageId, options);
            //计算出适屏比例
            options.inSampleSize = calculateInSize(options,
                    targetWidth, targetHeight);
            options.inJustDecodeBounds = false;
        }else {
            options.inSampleSize=1;
        }
        return BitmapFactory.decodeResource(getResources(), imageId, options);
    }

    /**
     * 采用向上取整的方式，计算压缩尺寸
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int inSampleSize = 0;
        if (reqWidth > 0) {//ImageView指定大小，按适合尺寸加载
            int heightRatio = (int) Math.ceil((options.outHeight * 1.0f) / reqHeight);
            int widthRatio = (int) Math.ceil((options.outWidth * 1.0f) / reqWidth);
            inSampleSize = Math.max(heightRatio, widthRatio);
        } else {  //ImageView没有指定大小，按图片原始大小加载
            inSampleSize = 1;
        }
        return inSampleSize;
    }

}
