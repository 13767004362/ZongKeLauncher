package com.zongke.hapilolauncher.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zongke.hapilolauncher.R;


/**
 * Created by ${tanlei} on 2017/9/13.
 * 打开宝箱的dialog
 */

public class OpenBoxDialog extends Dialog {
    /**
     * 背景图片
     */
    private ImageView ivOpenBoxBg1, ivOpenBoxBg2, ivOpenBoxBg5;
    /**
     * 宝箱图片
     */
    private ImageView ivBox;
    /**
     * 奖励背景
     */
    private LinearLayout llLayout;
    private Context context;

    public OpenBoxDialog(@NonNull Context context) {
        this(context, 0);
    }

    public OpenBoxDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, R.style.DialogTheme_no_systembar);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.open_box_dialog, null);
        setContentView(view);
        ivOpenBoxBg1 = (ImageView) view.findViewById(R.id.open_box_bg1);
        ivOpenBoxBg2 = (ImageView) view.findViewById(R.id.open_box_bg2);
        ivBox = (ImageView) view.findViewById(R.id.iv_box);
        llLayout = (LinearLayout) view.findViewById(R.id.ll);
        ivOpenBoxBg5 = (ImageView) view.findViewById(R.id.open_box_bg5);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideView();
        startBgAnimation();
    }

    /**
     * 开始动画
     */
    private void startBgAnimation() {
        AnimatorSet set = new AnimatorSet();
        AnimatorSet bg1Set = scaleAnimatorView(ivOpenBoxBg1);
        AnimatorSet boxSet = scaleAnimatorView(ivBox);
        //设置每个缩放动画的延迟执行的毫秒值差，来实现先后播放的视觉效果
        boxSet.setStartDelay(250);
        AnimatorSet bg2Set = scaleAnimatorView(ivOpenBoxBg2);
        bg2Set.setDuration(1500);
        bg2Set.setStartDelay(400);
        //宝箱左右摇晃动画
        ObjectAnimator rotationBoxAnimator = ObjectAnimator.ofFloat(ivBox, "rotation", 0f, -10f, 0f, 10f, 0f);
        rotationBoxAnimator.setStartDelay(750);
        rotationBoxAnimator.setDuration(400);
        //给摇晃动画设置监听器，使用AnimatorListenerAdapter就可以不需要实现所有的抽象方法，这里我只需要重写动画结束后的监听
        rotationBoxAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //摇晃结束后执行打开宝箱的桢动画
                ivBox.setImageResource(R.drawable.open_box);
                AnimationDrawable frameAnimation = (AnimationDrawable) ivBox.getDrawable();
                frameAnimation.start();

                int duration = 0;
                for (int i = 0; i < frameAnimation.getNumberOfFrames(); i++) {
                    duration += frameAnimation.getDuration(i);
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        //幀动画执行完成后
                        AnimatorSet set = new AnimatorSet();
                        AnimatorSet set2 = new AnimatorSet();
                        AnimatorSet set3 = new AnimatorSet();
                        //箱子中的白色光圈的缩放动画
                        ObjectAnimator bg1ScaleX = ObjectAnimator.ofFloat(ivOpenBoxBg5, "scaleX", 0f, 1f);
                        ObjectAnimator bg1ScaleY = ObjectAnimator.ofFloat(ivOpenBoxBg5, "scaleY", 0f, 1f);
                        bg1ScaleX.setDuration(150);
                        bg1ScaleY.setDuration(150);
                        set2.playTogether(bg1ScaleX, bg1ScaleY);
                        //箱子中的白色光圈消失的透明度动画
                        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(ivOpenBoxBg5, "alpha", 1f, 0f);
                        //加速执行的加值器
                        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                        //奖励布局的缩放动画
                        ObjectAnimator bg3ScaleX = ObjectAnimator.ofFloat(llLayout, "scaleX", 0f, 1f);
                        ObjectAnimator bg3ScaleY = ObjectAnimator.ofFloat(llLayout, "scaleY", 0f, 1f);
                        //奖励动画的平移
                        ObjectAnimator translationXAnimator = ObjectAnimator.ofFloat(llLayout, "translationY", 0f, 130f);
                        set3.playTogether(alphaAnimator, bg3ScaleX, bg3ScaleY, translationXAnimator);
                        set.playSequentially();
                        set3.setDuration(200);
                        set3.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                //奖励动画的平移结束后，得到动态布局中子View的个数，全部播放缩放动画
                                for (int i = 0; i < llLayout.getChildCount(); i++) {
                                    AnimatorSet animatorSet = scaleAnimatorView(llLayout.getChildAt(i));
                                    animatorSet.start();
                                }
                            }
                        });
                        set.playSequentially(set2, set3);
                        set.start();
                    }
                }, duration);

            }
        });
        set.playTogether(bg1Set, boxSet, bg2Set, rotationBoxAnimator);
        set.start();
    }

    /**
     * 缩放动画
     *
     * @param v
     * @return 返回一个动画集合
     */
    private AnimatorSet scaleAnimatorView(View v) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator bg1ScaleX = ObjectAnimator.ofFloat(v, "scaleX", 0f, 1.2f, 1f);
        ObjectAnimator bg1ScaleY = ObjectAnimator.ofFloat(v, "scaleY", 0f, 1.2f, 1f);
        //如果是背景太阳光圈的话，需要缩放加旋转动画
        if (v.getId() == R.id.open_box_bg2) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", 0f, 360f);
            //匀速播放加值器，不然会出现旋转一圈后卡顿一会的效果
            animator.setInterpolator(new LinearInterpolator());
            //无限循环
            animator.setRepeatCount(-1);
            //一起播放
            set.playTogether(bg1ScaleX, bg1ScaleY, animator);
        } else {
            set.playTogether(bg1ScaleX, bg1ScaleY);
        }
        //动画集合播放事件
        set.setDuration(500);
        return set;
    }

    /**
     * 因为宝箱中的奖励数量和种类是不可控的，所以需要动态的添加View
     *
     * @param res
     */
    public void addView(int res) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(res);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.weight = 1f;
        imageView.setLayoutParams(params);
        scaleView(imageView);
        llLayout.addView(imageView);
    }

    /**
     * 把View影藏掉
     */
    private void hideView() {
        scaleView(ivBox);
        scaleView(ivOpenBoxBg1);
        scaleView(ivOpenBoxBg2);
        scaleView(llLayout);
        scaleView(ivOpenBoxBg5);
    }

    private void scaleView(View view) {
        view.setScaleX(0f);
        view.setScaleY(0f);
    }
}
