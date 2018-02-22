package com.george888.mina.hereguide.ui.splashActivity;

import com.george888.mina.hereguide.ui.base.BasePresenter;

/**
 * Created by minageorge on 2/7/18.
 */

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    @Override
    public void decideNextActivity() {
      getMvpView().openHomeActivity();
    }
}
