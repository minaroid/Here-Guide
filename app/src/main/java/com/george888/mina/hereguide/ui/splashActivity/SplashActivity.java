package com.george888.mina.hereguide.ui.splashActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.ui.base.BaseActivity;
import com.george888.mina.hereguide.ui.homeActivity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by minageorge on 2/7/18.
 */

public class SplashActivity extends BaseActivity implements SplashMvpView {

    @BindView(R.id.text_layout)
    LinearLayout linearLayout;
    @BindView(R.id.im_loading)
    ImageView imageView;
    private Animation anim, anim2;
    private SplashPresenter mSplashPresenter;
    private static final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_logo);
        anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_loading);
        linearLayout.setAnimation(anim);
        imageView.setAnimation(anim2);
        mSplashPresenter = new SplashPresenter();
        mSplashPresenter.onAttach(this);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mSplashPresenter.decideNextActivity();

            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public void openHomeActivity() {

        Intent intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
        finish();
    }


}
