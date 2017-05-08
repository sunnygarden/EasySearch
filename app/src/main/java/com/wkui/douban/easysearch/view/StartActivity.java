package com.wkui.douban.easysearch.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.wkui.douban.easysearch.R;
import com.wkui.douban.easysearch.base.BaseActivity;

/**
 * Created by wkui on 2017/3/7.
 */

public class StartActivity extends BaseActivity {
    private static final int ANIMATION_TIME = 2000;
    private static final float SCALE_END = 2F;
    private ImageView startImg;

    @Override
    protected void initView() {
        if ( !isTaskRoot() ) {
            finish();
            return;
        }
        setContentView(R.layout.activity_start);
        startImg = (ImageView) findViewById(R.id.start_iv);
        Glide.with(this).load(R.drawable.start).asGif().into(startImg);
        applyKitKatTranslucency(Color.WHITE);
        StatusBarUtil.setTransparentForImageView(this,startImg);
        startAnim();
    }

    @Override
    protected void handleThemeChange() {

    }

    private void startAnim()
    {
       //ObjectAnimator animator = ObjectAnimator.ofFloat(startImg, "alpha", 1.0f, 0.3f, 1.0F);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(startImg, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(startImg, "scaleY", 1f, SCALE_END);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                StartActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }



}
