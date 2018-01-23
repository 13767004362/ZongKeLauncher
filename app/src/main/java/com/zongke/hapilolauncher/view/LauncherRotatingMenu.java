package com.zongke.hapilolauncher.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.zongke.hapilolauncher.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.zongke.hapilolauncher.view.HorizontalScrollBGView.MODE_MOVE_RIGHT;

/**
 * Created by ${xinGen} on 2017/9/4.
 * <p>
 * Launcher主界面的旋转菜单
 */

public class LauncherRotatingMenu extends ViewGroup {
    private final String tag = LauncherRotatingMenu.class.getSimpleName();
    private Context context;
    private CenterCircleItem centerCircleItem;
    private SectorCircleItem sectorCircleView;
    private ScrollResponseCompact scrollResponseCompact;
    private Bitmap center_circle_bitmap, sector_circle_bitmap;
    private LauncherRotatingMenuCallBack callBack;

    public void setCallBack(LauncherRotatingMenuCallBack callBack) {
        this.callBack = callBack;
    }

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
     * 初始的飞行速度除以的数，这里是除以4
     */
    public static final int FLING_VELOCITY_DOWNSCALE = 4;
    /**
     * 滚动视图的区域面积
     */
    private RectF scrollViewBounds;
    private List<TestEntity> total_child_menu;

    public LauncherRotatingMenu(Context context) {
        super(context);
        this.context = context;
        initConfig();
    }

    public LauncherRotatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initConfig();
    }

    private int currentViewRotation;
    private HomeItem homeItem;

    /**
     * 添加子View
     */
    private void addChildView() {
        this.removeAllViews();
        if (sector_circle_bitmap != null && center_circle_bitmap != null) {
            Log.i(tag, " 添加子LauncherRotatingMenu添加子类View ");
            sectorCircleView = new SectorCircleItem(this.context);
            this.addView(sectorCircleView);
            sectorCircleView.rotateTo(currentViewRotation);
            centerCircleItem = new CenterCircleItem(this.context);
            this.addView(centerCircleItem);
            homeItem = new HomeItem(this.context);
            this.addView(homeItem);
            this.homeItem.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = getContext();
                    Intent intent = new Intent();
                    intent.setClassName("com.zhongke.content", "com.zhongke.content.activity.MineHomeActivity");
                    if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                        try {
                            context.startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(context, "未安装程序", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }


    /**
     * 加载图片
     */
    private void initConfig() {
        setWillNotDraw(false);
        decodeBitmap();
        loadProperBitmap(R.mipmap.luancher_main_menu_bg_top).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                //   Log.i(tag, " Glide 中心圆圈背景 " + resource);
                center_circle_bitmap = bitmap;
                if (center_circle_bitmap != null && sector_circle_bitmap != null) {
                    handler.obtainMessage(1).sendToTarget();
                }
            }
        });
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        final int width = (int) ((displayMetrics.widthPixels * 0.8367 - displayMetrics.widthPixels * 0.046) * SectorCircleItem.proportion_second_image_width);
        final int height = (int) (((displayMetrics.heightPixels - displayMetrics.heightPixels * 0.286)) * SectorCircleItem.proportion_second_image_height);
        loadProperBitmap(R.mipmap.luancher_main_menu_bg_second).into(new SimpleTarget<Bitmap>(width, height) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //  Log.i(tag, " Glide 旋转圆形背景  " + " 计算出来目标的大小" + width + " " + height + "  加载出来的Bitmap " + resource.getHeight() + " " + resource.getWidth());
                sector_circle_bitmap = resource;
                if (center_circle_bitmap != null && sector_circle_bitmap != null) {
                    handler.obtainMessage(1).sendToTarget();
                }
            }
        });

        //创建一个Scroller，来处理fling 手势。
        this.scroller = new Scroller(context, null, true);
        //创建一个 gesture Detector来处理onTouch()中信息。
        this.gestureDetector = new GestureDetector(LauncherRotatingMenu.this.getContext(), new ViewGroupGestureListener());
        this.gestureDetector.setIsLongpressEnabled(false);

        /**
         *  scroller不提供动画能力，它只提供一些值，当需要使用到的。
         *   因此，需要有一个方法来调用从开始到结束的每一帧。
         *   采用ValueAnimator对象生成每个动画的回调，而不使用动画值。
         */
        this.mScrollAnimator = ValueAnimator.ofFloat(0, 1);
        this.mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Log.i(TAG, " ScrollAnimator  u 更新 监听器 相应，执行更新操作  ");
                tickScrollAnimation();
            }
        });
    }

    /**
     * Fling监听，中触发动画，进行变换动作。
     */
    private void tickScrollAnimation() {
        if (!scroller.isFinished()) {
            scrollResponseCompact.scrollPosition(flingDirection, 0);
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

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    addChildView();
                    break;
                case 2:
                    if (sectorCircleView != null) {
                        sectorCircleView.invalidate();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 对View设置转动角度
     *
     * @param
     */
    public void setViewRotation(int viewRotation) {
        this.currentViewRotation = (viewRotation % 360 + 360) % 360;
        Log.i(tag, " 菜单控件 滚动角度 " + currentViewRotation);
        this.sectorCircleView.rotateTo(currentViewRotation);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        /**
         *   GestureDetector拦截事件
         */
        boolean result = gestureDetector.onTouchEvent(event);
        /**
         *  result为false ,表示GestureDetector不做处理的事件
         */
        if (!result) {
            //当用户滚动过程中，抬起手指停止滚动。
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.i(TAG, " GestureDetector 不处理 ");
                stopScrolling();
                result = true;
            }
        }
        Log.i(TAG, " onTouchEvent 处理 result " + result);
        return super.dispatchTouchEvent(event);
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
     * 检查方向
     *
     * @param e1
     * @param e2
     * @return
     */
    private int checkDirection(MotionEvent e1, MotionEvent e2, float velocityX, float minMoveDistance, float minVelocity) {
        if (e2.getX() - e1.getX() > minMoveDistance && Math.abs(velocityX) > minVelocity) {//手势右边滑动
            return HorizontalScrollBGView.MODE_MOVE_LEFT;//背景反向<-
        } else {//手势向着左边滑动<-
            return MODE_MOVE_RIGHT;//背景方向->
        }
    }

    /**
     * 检查方向
     *
     * @param e1
     * @param e2
     * @return
     */
    private int checkDirection(MotionEvent e1, MotionEvent e2, float minMoveDistance) {
        //手势右边滑动
        if (e2.getX() - e1.getX() > minMoveDistance) {
            //背景反向<-
            return HorizontalScrollBGView.MODE_MOVE_LEFT;
        }//手势向着左边滑动<-
        else {
            //背景方向->
            return MODE_MOVE_RIGHT;
        }
    }

    private int flingDirection;

    /**
     * 手势监控，拖拽和滑动事件。
     */
    private class ViewGroupGestureListener extends GestureDetector.SimpleOnGestureListener {
        //最小滑动距离
        float min_move_distance = 10;
        //最小滑动速度
        float min_Velocity = 0;

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int moveDirection = checkDirection(e1, e2, min_move_distance);
            //滚动
            float scrollTheta = vectorToScalarScroll(distanceX, distanceY, e2.getX() - scrollViewBounds.centerX(), e2.getY() - scrollViewBounds.centerY());
            int viewRotation = currentViewRotation - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE;
            scrollResponseCompact.scrollPosition(moveDirection, 0);
            setViewRotation(viewRotation);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            flingDirection = checkDirection(e1, e2, velocityX, min_move_distance, min_Velocity);

            // 使用Scroller建立Fling手势
            float scrollTheta = vectorToScalarScroll(velocityX, velocityY, e2.getX() - scrollViewBounds.centerX(), e2.getY() - scrollViewBounds.centerY());
            scroller.fling(0, (int) currentViewRotation, 0, (int) scrollTheta / FLING_VELOCITY_DOWNSCALE, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            //Log.i(tag, " onFling状态  " + "上次的转动角度" + currentViewRotation + " 当前计算的： " + (scrollTheta / FLING_VELOCITY_DOWNSCALE));
            mScrollAnimator.setDuration(scroller.getDuration());
            mScrollAnimator.start();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i(TAG, " onDown状态 ");
            if (isAnimationRunning()) {
                stopScrolling();
            }
            return true;
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
     * 当用户结束滚动动作时候调用
     */
    private void onScrollFinished() {
        // sectorCircleView.decelerate();
    }

    /**
     * 是否有动画或者正在滚动
     *
     * @return
     */
    private boolean isAnimationRunning() {
        return !this.scroller.isFinished();
    }

    /**
     * 根据指定大小，加载合适比率的图片
     *
     * @param imageId
     */
    private BitmapTypeRequest<Integer> loadProperBitmap(int imageId) {
        return Glide.with(context).load(imageId).asBitmap();
    }

    private RectF centerViewBuounds;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //     Log.i(tag, "ViewGroup onLayout" + " 边距 " + l + " " + r + " " + t + " " + b + " 中心控件 " + centerCircleItem + "  旋转控件: " + sectorCircleView + " 中心控件背景 : " + center_circle_bitmap + " 旋转控件背景： " + sector_circle_bitmap);
        //获取到水平和竖直的Padding值
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingBottom() + getPaddingTop());
        //系统分配到父容器中的宽高。
        float ww = (float) (r - l) - xpad;
        float hh = (float) (b - t) - ypad;
        if (center_circle_bitmap != null && sector_circle_bitmap != null) {
            if (sectorCircleView != null && scrollViewBounds == null) {
                scrollViewBounds = new RectF(0.0f, 0.0f, ww, hh * 2);
                //将矩形偏移到指定的（left,top）位置，且保持宽高不变。
                scrollViewBounds.offset(getPaddingLeft(), getPaddingTop());
                // 计算实际饼图的绘制位置。
                sectorCircleView.layout((int) scrollViewBounds.left, (int) scrollViewBounds.top, (int) scrollViewBounds.right, (int) scrollViewBounds.bottom);
                //设置旋转和缩放的转动点
                //   Log.i(tag, " 旋转控件的中心点 " + scrollViewBounds.centerX() + " " + scrollViewBounds.centerY());
                sectorCircleView.setPivot(scrollViewBounds.centerX(), scrollViewBounds.centerY());
                sectorCircleView.layout(0, 0, (int) ww, (int) hh * 2);
            }
            if (centerCircleItem != null && centerViewBuounds == null) {
                centerViewBuounds = new RectF();
                centerViewBuounds.left = scrollViewBounds.centerX() - center_circle_bitmap.getWidth() / 2;
                centerViewBuounds.top = scrollViewBounds.centerY() - center_circle_bitmap.getHeight() / 2;
                centerViewBuounds.right = centerViewBuounds.left + center_circle_bitmap.getWidth();
                centerViewBuounds.bottom = centerViewBuounds.top + center_circle_bitmap.getHeight();
                centerCircleItem.layout((int) centerViewBuounds.left, (int) centerViewBuounds.top, (int) centerViewBuounds.right, (int) centerViewBuounds.bottom);
            }
            if (bitmap != null && homeItem != null) {
                float left_home = scrollViewBounds.centerX() - bitmap.getWidth() / 2;
                float right_home = left_home + bitmap.getWidth();
                float top_home = scrollViewBounds.centerY() - bitmap.getHeight();
                float bottom_home = top_home + bitmap.getHeight();
                homeItem.layout((int) left_home, (int) top_home, (int) right_home, (int) bottom_home);
            }
        }
    }

    /**
     * 添加数据
     *
     * @param testEntityList
     */
    public void addData(List<LauncherRotatingMenu.TestEntity> testEntityList) {
        if (total_child_menu != null) {
            total_child_menu.clear();
            total_child_menu = null;
        }
        this.total_child_menu = testEntityList;
        this.isLoadingBitmap = false;
        //      Log.i(tag, "addData 数据库中查询到的数据源 ");
        loadAllBitmap();
    }

    private boolean isLoadingBitmap;
    private int total_add = 0;

    private void loadAllBitmap() {
        if (!isLoadingBitmap) {
            this.isLoadingBitmap = true;
            // Log.i(TAG," 开始加载图片： loadAllBitmap ");
            total_add = 0;
            for (int i = 0; i < total_child_menu.size(); ++i) {
                TestEntity entity = total_child_menu.get(i);
                // Log.i(tag, " 开始加载，图片位置 ："+i+" 否为空 " + entity.bitmap + " " + entity.name + " " + entity.iconId);
                if (entity.bitmap == null) {
                    loadProperBitmap(entity.iconId).into(createTarget(entity));
                }
            }
        }
    }

    private SimpleTarget<Bitmap> createTarget(final TestEntity entity) {
        int bitmapWidth = sector_circle_bitmap != null ? (int) (sector_circle_bitmap.getWidth() * SectorCircleItem.proportion_circle_image_width) : Target.SIZE_ORIGINAL;
        final int bitmapHeight = sector_circle_bitmap != null ? (int) (sector_circle_bitmap.getHeight() * SectorCircleItem.proportion_circle_image_height) : Target.SIZE_ORIGINAL;
        return new SimpleTarget<Bitmap>(bitmapWidth, bitmapHeight) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                entity.bitmap = resource;
                ++total_add;
                //   Log.i(tag, "加载好的Bitmap个数 ：" + total_add + " 控件对象是否为空 " + sectorCircleView);
                if (total_add == localLoadTotalSize) {
                    isLoadingBitmap = false;
                    //   Log.i(tag, "加载Bitmap完成"+" 控件对象是否为空 " + sectorCircleView );
                    handler.obtainMessage(2).sendToTarget();
                }
            }
        };
    }


    /**
     * 旋转的圆形菜单View
     */
    public class SectorCircleItem extends View {
        private final String tag = SectorCircleItem.class.getSimpleName();
        /**
         * 底图的宽高比率
         */
        public static final double proportion_second_image_width = (double) 827 / 1012;
        public static final double proportion_second_image_height = (double) 415 / 514;
        private DrawFilter pfdf;
        /**
         * 圆圈的大小
         */
        public static final double proportion_circle_image_width = (double) 164 / 827;
        public static final double proportion_circle_image_height = (double) 172 / 415;
        private Paint paint;


        public SectorCircleItem(Context context) {
            super(context);
            initConfig();
        }

        /**
         * 初始化配置
         */
        private void initConfig() {
            this.paint = new Paint();
            //消除锯齿
            this.paint.setAntiAlias(true);
            this.pfdf = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.setDrawFilter(pfdf);
            Log.i(tag, "SectorCircleItem ondraw " + total_add + " " + localLoadTotalSize);
            drawBGBitmap(canvas);
            drawChildMenu(canvas);
        }

        /**
         * 绘制子类Menu
         *
         * @param canvas
         */
        private void drawChildMenu(Canvas canvas) {
            if (total_child_menu == null || total_child_menu.size() == 0 || total_add != localLoadTotalSize) {
                return;
            }
            //  Log.i(tag, "SectorCircleItem 控件正在绘制转动的小菜单 ");
            float averageAngle = 360f / total_child_menu.size();
            int height = getHeight();
            int width = getWidth();
            for (int i = 0; i < total_child_menu.size(); i++) {
                Bitmap bitmap = total_child_menu.get(i).bitmap;
                //  Log.i(tag, " 绘制的Bitmap为 "+bitmap + "位置" + i + " 迭代计算的是 " + total_add + " 本地图片个数 " + localLoadTotalSize);
                if (bitmap == null) {
                    continue;
                }
                int bitmap_margin = bitmap.getHeight() / 2;
                int hypotenuse = sector_circle_bitmap.getWidth() / 2;
                int top = 0;
                int left = 0;
                float angle = averageAngle * (i + 1);
                if (angle < 90) {
                    double dangle = Math.toRadians(angle);
                    left = width / 2 + (int) (Math.cos(dangle) * hypotenuse) - bitmap_margin;
                    top = height / 2 + (int) (Math.sin(dangle) * hypotenuse) - bitmap_margin;
                } else if (90 < angle && angle < 180) {
                    double dangle = Math.toRadians(angle - 90);
                    left = width / 2 - (int) (Math.sin(dangle) * hypotenuse) - bitmap_margin;
                    top = height / 2 + (int) (Math.cos(dangle) * hypotenuse) - bitmap_margin;
                } else if (180 < angle && angle < 270) {
                    double dangle = Math.toRadians(angle - 180);
                    left = width / 2 - (int) (Math.cos(dangle) * hypotenuse) - bitmap_margin;
                    top = height / 2 - (int) (Math.sin(dangle) * hypotenuse) - bitmap_margin;
                } else if (270 < angle) {
                    double dangle = Math.toRadians(angle - 270);
                    left = width / 2 + (int) (Math.sin(dangle) * hypotenuse) - bitmap_margin;
                    top = height / 2 - (int) (Math.cos(dangle) * hypotenuse) - bitmap_margin;
                } else if (angle == 90) {
                    left = width / 2 - bitmap_margin;
                    top = height / 2 + hypotenuse - bitmap_margin;
                } else if (angle == 270) {
                    left = width / 2 - bitmap_margin;
                    top = height / 2 - hypotenuse - bitmap_margin;
                } else if (angle == 180) {
                    left = width / 2 - hypotenuse - bitmap_margin;
                    top = height / 2 - bitmap_margin;
                } else if (angle == 360) {
                    left = width / 2 + hypotenuse - bitmap_margin;
                    top = height / 2 - bitmap_margin;
                }
                total_child_menu.get(i).rect = createChildRect(top, left, bitmap);
                canvas.drawBitmap(bitmap, createMatrix(left, top, angle + 90, bitmap), paint);

            }
        }

        private Rect createChildRect(int top, int left, Bitmap bitmap) {
            Rect rect = new Rect();
            rect.top = top;
            rect.left = left;
            rect.right = rect.left + bitmap.getWidth();
            rect.bottom = rect.top + bitmap.getHeight();
            return rect;
        }

        private Matrix createMatrix(int left, int top, float angle, Bitmap bitmap) {
            Matrix matrix = new Matrix();
            matrix.setTranslate(left, top);

            // Log.i(tag, "createMatrix   转动角度 ：" + angle);
            // 根据原图的中心位置旋转
            matrix.preRotate(angle, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            return matrix;
        }


        /**
         * 绘制背景图片
         *
         * @param canvas
         */
        private void drawBGBitmap(Canvas canvas) {
            //Log.i(tag, " 旋转圆绘制底图 " + sector_circle_bitmap);
            if (sector_circle_bitmap == null) {
                return;
            }
            int bitmap_width = sector_circle_bitmap.getWidth();
            int bitmap_height = sector_circle_bitmap.getHeight();
            float top = scrollViewBounds.centerY() - bitmap_height / 2;
            float left = scrollViewBounds.centerX() - bitmap_width / 2;
            canvas.drawBitmap(sector_circle_bitmap, left, top, paint);
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

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            // Log.i(tag," 滚动view的onTouchEvent" );
            handleClickEvent(event);
            return true;
        }

        private int down_x, down_y, move_x, move_y;

        /**
         * 处理点击事件:Down->UP,不存在Move
         * <p>
         * getRawX() / getRawX():相对于屏幕的坐标绝对值
         * <p>
         * getX() /getY():相对于消费该事件视图左上点坐标绝对值。
         *
         * @param event
         */
        private void handleClickEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:       //没有移动，down->up，点击事件
                    down_x = (int) event.getRawX();
                    down_y = (int) event.getRawX();
                    move_x = down_x;
                    move_y = down_y;
                    //  Log.i(tag," 点击测试 Down动作："+move_x+" "+move_y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    move_x = (int) event.getRawX();
                    move_y = (int) event.getRawX();
                    //  Log.i(tag," 点击测试 Move动作："+move_x+" "+move_y);
                    break;
                case MotionEvent.ACTION_UP:
                    // Log.i(tag," 点击测试 UP动作："+move_x+" "+move_y);
                    //没有触发move事件，坐标不改变，是点击事件。
                    if (move_x == down_x && move_y == down_y) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        for (int i = 0; i < total_child_menu.size(); ++i) {
                            if (total_child_menu.get(i).rect.contains(x, y)) {
                                TestEntity testEntity = total_child_menu.get(i);
                                if (TextUtils.isEmpty(testEntity.packageName)) {
//                                    ToastUtils.showToast(getContext().getApplicationContext(), "未安装" + testEntity.name + "程序");
                                    callBack.showDialog();
                                } else {
                                    Intent intent;
                                    if (TextUtils.isEmpty(testEntity.activityName)) {
                                        intent = getContext().getPackageManager().getLaunchIntentForPackage(testEntity.packageName);
                                    } else {
                                        intent = new Intent();
                                        intent.setClassName(testEntity.packageName, testEntity.activityName);
                                    }
                                    if (intent != null && intent.resolveActivity(getContext().getPackageManager()) != null) {
                                        try {
                                            getContext().startActivity(intent);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            Toast.makeText(getContext(), "未安装此应用", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                break;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private Bitmap bitmap;

    private Bitmap decodeBitmap() {
        bitmap = bitmap == null ? BitmapFactory.decodeResource(getResources(), R.mipmap.luancher_main_menu_bg_top_home, new BitmapFactory.Options()) : bitmap;
        return bitmap;
    }

    /**
     * 我的家的View
     */
    private class HomeItem extends View {
        private Paint paint;

        public HomeItem(Context context) {
            super(context);
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
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }
    }

    /**
     * 中心圆的view
     */
    private class CenterCircleItem extends View {
        private Paint paint;

        public CenterCircleItem(Context context) {
            super(context);
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
            //绘制图片
            drawBitmap(canvas);
        }

        /**
         * 绘制bitmap
         *
         * @param canvas
         */
        void drawBitmap(Canvas canvas) {
            if (center_circle_bitmap == null) {
                return;
            }
            float top = 0;
            float left = top;
            canvas.drawBitmap(center_circle_bitmap, top, left, paint);
        }
    }

    private int localLoadTotalSize;

    public synchronized void setLocalBitmapSize(int size) {
        this.localLoadTotalSize = size;
    }

    public static List<TestEntity> createTestData(int position) {
        List<TestEntity> list = new ArrayList<>();
        for (int i = position; i <= 16; ++i) {
            TestEntity entity = new TestEntity();
            switch (i) {
                case 1:
                    entity.name = "电影";
                    entity.iconId = R.mipmap.launcher_main_menu_movie;
                    break;
                case 2:
                    entity.name = "音乐";
                    entity.iconId = R.mipmap.launcher_main_menu_music;
                    break;
                case 3:
                    entity.name = "游戏";
                    entity.iconId = R.mipmap.launcher_main_menu_game;
                    break;
                case 4:
                    entity.name = "主题";
                    entity.iconId = R.mipmap.launcher_main_menu_theme;
                    break;
                case 5:
                    entity.name = "备忘";
                    entity.iconId = R.mipmap.launcher_main_menu_memo;
                    break;
                case 6:
                    entity.name = "天气";
                    entity.iconId = R.mipmap.launcher_main_menu_weather;
                    break;
                case 7:
                    entity.name = "课表";
                    entity.iconId = R.mipmap.launcher_main_menu_curriculum;
                    break;
                case 8://8~16开始展现在上面。
                    entity.name = "发现";
                    entity.iconId = R.mipmap.launcher_main_menu_search;
                    break;
                case 9:
                    entity.name = "好友";
                    entity.iconId = R.mipmap.launcher_main_menu_friend;
                    entity.packageName = "com.zhongke.chat";
                    break;
                case 10:
                    entity.name = "直播";
                    entity.iconId = R.mipmap.launcher_main_menu_live;
                    entity.packageName = "com.zhongke.content";
                    entity.activityName = getActivityNames(entity.packageName, "LiveLobbyActivity");
                    break;
                case 11:
                    entity.name = "PK";
                    entity.iconId = R.mipmap.launcher_main_menu_pk;
                    entity.packageName = "com.zhongke.content";
                    entity.activityName = getActivityNames(entity.packageName, "AnswerMainActivity");
                    break;
                case 12:
                    entity.name = "愿望";
                    entity.iconId = R.mipmap.launcher_main_menu_desire;
                    entity.packageName = "com.zhongke.content";
                    entity.activityName = getActivityNames(entity.packageName, "DesireActivity");
                    break;
                case 13:
                    entity.name = "心声";
                    entity.iconId = R.mipmap.launcher_main_menu_voice;
                    break;
                case 14:
                    entity.name = "应用";
                    entity.iconId = R.mipmap.launcher_main_menu_application;
                    entity.packageName = "com.zhongke.market";
                    break;
                case 15:
                    entity.name = "活动";
                    entity.iconId = R.mipmap.launcher_main_menu_activity;
                    entity.packageName = "com.zhongke.content";
                    entity.activityName = getActivityNames(entity.packageName, "ActivityListActivity");
                    break;
                case 16:
                    entity.name = "设置";
                    entity.iconId = R.mipmap.launcher_main_menu_setting;
                    break;
                default:

                    break;
            }
            list.add(entity);
        }
        return list;
    }

    private static String getActivityNames(String packageName, String activity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(packageName);
        stringBuilder.append(".activity.");
        stringBuilder.append(activity);
        return stringBuilder.toString();
    }

    /**
     * 测试实体
     */
    public static class TestEntity {
        public String imageUrl;
        /**
         * 对应的icon
         */
        public int iconId;
        /**
         * 对应的程序包名
         */
        public String packageName;
        public String activityName;
        public String name;
        /**
         * 对应的Bitmap
         */
        public Bitmap bitmap;
        /**
         * 对应的绘制区域
         */
        public Rect rect;
    }

    public void setScrollResponseCompact(ScrollResponseCompact scrollResponseCompact) {
        this.scrollResponseCompact = scrollResponseCompact;
    }

    public interface LauncherRotatingMenuCallBack {
        void showDialog();
    }
}
