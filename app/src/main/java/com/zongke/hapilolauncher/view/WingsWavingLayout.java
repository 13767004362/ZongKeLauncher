package com.zongke.hapilolauncher.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.zongke.hapilolauncher.R;
import com.zongke.hapilolauncher.utils.DisplayUtils;
import com.zongke.hapilolauncher.utils.ViewUtils;

/**
 * Created by ${xingen} on 2017/9/6.
 * <p>
 * 愿望控件：
 * <p>
 * 翅膀挥动的布局。
 */

public class WingsWavingLayout extends FrameLayout {

    /**
     * 大样式和小样式
     */
    public static final int MODE_LARGE = 1;
    public static final int MODE_SMALL = 2;
    private int currentMode;
    private ImageView[] imageViews;
    private int[] imageViewIds;
    /**
     * 水平或者竖直方向的间距
     */
    private int margin_level = 7;
    private float margin_vertical = 5;
    /**
     * Bitmap的集合
     */
    private SparseArray<Bitmap> bitmapSparseArray;

    public WingsWavingLayout(Context context) {
        super(context);
    }

    public WingsWavingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initConfig();
    }

    private void initConfig() {
        this.imageViews = new ImageView[3];
        this.imageViewIds = new int[3];
        this.bitmapSparseArray = new SparseArray<>();
    }

    /**
     * 设置显示的样式
     *
     * @param mode
     */
    public void setCurrentMode(int mode) {
        this.currentMode = mode;
        this.removeAllViews();
        this.addChildView();
    }

    /**
     * 大视图
     */
    private float middle_view_large_width = 64.7f;
    private float middle_view_large_height = 56.7f;
    private float left_or_right_view_large_width_height = 36;
    /**
     * 小视图
     */
    private float middle_view_small_width = 52.7f;
    private float middle_view_small_height = 46f;
    private float left_or_right_view_small_width_height = 29;

    private void addChildView() {
        switch (currentMode) {
            case MODE_LARGE:
                createLeftView(DisplayUtils.dip2px(getContext().getApplicationContext(), left_or_right_view_large_width_height), DisplayUtils.dip2px(getContext().getApplicationContext(), left_or_right_view_large_width_height));
                createRightView(DisplayUtils.dip2px(getContext().getApplicationContext(), left_or_right_view_large_width_height), DisplayUtils.dip2px(getContext().getApplicationContext(), left_or_right_view_large_width_height));
                createMiddleView(DisplayUtils.dip2px(getContext().getApplicationContext(), middle_view_large_width), DisplayUtils.dip2px(getContext().getApplicationContext(), middle_view_large_height));
                break;
            case MODE_SMALL:
                createLeftView(DisplayUtils.dip2px(getContext(), left_or_right_view_small_width_height), DisplayUtils.dip2px(getContext(), left_or_right_view_small_width_height));
                createRightView(DisplayUtils.dip2px(getContext(), left_or_right_view_small_width_height), DisplayUtils.dip2px(getContext(), left_or_right_view_small_width_height));
                createMiddleView(DisplayUtils.dip2px(getContext(), middle_view_small_width), DisplayUtils.dip2px(getContext(), middle_view_small_height));
                break;
        }
    }

    private void loadBitmap(final int viewIds, int imageId, int width, int height) {
        loadPropertionBitmap(imageId).into(new SimpleTarget<Bitmap>(width, height) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> transition) {
                bitmapSparseArray.put(viewIds, resource);
                if (bitmapSparseArray.size() == 3) {
                    bindImageView();
                  startAnimator();
                }
            }
        });
    }

    /**
     * 开始动画
     */
    private void startAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createTranslationAnimatorSet(imageViews[0], 20, 35,0,-40)
                , createTranslationYAnimator(imageViews[1],- DisplayUtils.dip2px(getContext(), 5f))
                , createTranslationAnimatorSet(imageViews[2], -20, 35,0,40));
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.start();
    }

    private AnimatorSet createTranslationAnimatorSet(ImageView imageView, int translationX, int translationY,float startAngle,float endAngle) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createTranslationXAnimator(imageView, translationX)
                , createTranslationYAnimator(imageView, translationY)
                ,createRotationAnimator(imageView,startAngle,endAngle));
        return animatorSet;
    }
     private Animator createRotationAnimator(ImageView imageView,float startAngle,float endAngle){
         ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation",startAngle,endAngle,startAngle );
         //设置动画次数：无限重复
         animator.setRepeatCount(ValueAnimator.INFINITE);
         return  animator;
     }
    private Animator createTranslationXAnimator(ImageView imageView, int translationX) {
        ObjectAnimator translation_x = ObjectAnimator.ofFloat(imageView, "TranslationX", 0, translationX, 0);
        translation_x.setRepeatCount(ValueAnimator.INFINITE);
        return translation_x;
    }

    private Animator createTranslationYAnimator(ImageView imageView, int translationY) {
        ObjectAnimator translation_y = ObjectAnimator.ofFloat(imageView, "TranslationY", 0, translationY, 0);
        translation_y.setRepeatCount(ValueAnimator.INFINITE);
        return translation_y;
    }

    /**
     * 添加Bitmap到ImageView
     */
    private void bindImageView() {
        for (int i = 0; i < bitmapSparseArray.size(); ++i) {
            ImageView imageView = imageViews[i];
            Bitmap bitmap = bitmapSparseArray.get(imageViewIds[i]);
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * 根据指定大小，加载合适比率的图片
     *
     * @param imageId
     */
    private BitmapRequestBuilder<Integer, Bitmap> loadPropertionBitmap(int imageId) {
        return Glide.with(getContext()).load(imageId).asBitmap().placeholder(imageId).error(imageId);
    }

    /**
     * 创建左边翅膀的控件
     *
     * @param width
     * @param height
     * @return
     */
    private void createLeftView(int width, int height) {
        ImageView imageView = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams = new LayoutParams(width, height);
        imageView.setLayoutParams(layoutParams);
        imageViewIds[0] = ViewUtils.getViewId();
        imageView.setId(imageViewIds[0]);
        imageViews[0] = imageView;
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(imageViews[0]);
        loadBitmap(imageViewIds[0],R.mipmap.launcher_home_wings_left, width, height);
    }

    /**
     * 添加中间愿望的控件
     *
     * @param width
     * @param height
     */
    private void createMiddleView(int width, int height) {
        ImageView imageView = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams2 = new LayoutParams(width, height, Gravity.CENTER_HORIZONTAL);
        layoutParams2.topMargin = DisplayUtils.dip2px(getContext().getApplicationContext(), margin_vertical);
        imageViewIds[1] = ViewUtils.getViewId();
        imageView.setLayoutParams(layoutParams2);
        imageView.setId(imageViewIds[1]);
        imageViews[1] = imageView;
        //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        this.addView(imageViews[1]);
        loadBitmap(imageViewIds[1], R.mipmap.launcher_home_sings_middle, width, height);
    }

    /**
     * 创建左边翅膀的控件
     *
     * @param width
     * @param height
     * @return
     */
    private void createRightView(int width, int height) {
        ImageView imageView = new ImageView(getContext());
        FrameLayout.LayoutParams layoutParams = new LayoutParams(width, height);
        if (currentMode == MODE_LARGE) {
            layoutParams.leftMargin = DisplayUtils.dip2px(getContext().getApplicationContext(), left_or_right_view_large_width_height + middle_view_large_width) - DisplayUtils.dip2px(getContext().getApplicationContext(), margin_level) * 2;
        } else {
            layoutParams.leftMargin = DisplayUtils.dip2px(getContext().getApplicationContext(), left_or_right_view_small_width_height + middle_view_small_width) - DisplayUtils.dip2px(getContext().getApplicationContext(), margin_level) * 2;
        }
        imageView.setLayoutParams(layoutParams);
        imageViewIds[2] = ViewUtils.getViewId();
      //  imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setId(imageViewIds[2]);
        imageViews[2] = imageView;
        this.addView(imageViews[2]);
        loadBitmap(imageViewIds[2], R.mipmap.launcher_home_wings_right, width, height);
    }


}
