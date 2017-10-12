package com.bignerdranch.android.xundian.comm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by Administrator on 2017/10/12.
 */

public class CommActivity extends AppCompatActivity {

    /**
     * 向下动画实现方法
     * @param v
     * @param start
     * @param end
     * @return
     */
    public ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }

    /**
     * 组件动画隐藏
     * @param view
     */
    public void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    /**
     * 组件动画显示
     * @param v
     * @param height
     */
    public void animateOpen(View v,int height) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,
                height);
        animator.start();

    }

    public void animationIvOpen() {
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
    }

    public void animationIvClose() {
        RotateAnimation animation = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);

    }
}
