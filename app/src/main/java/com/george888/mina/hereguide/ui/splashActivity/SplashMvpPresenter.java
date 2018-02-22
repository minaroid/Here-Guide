package com.george888.mina.hereguide.ui.splashActivity;

import com.george888.mina.hereguide.ui.base.MvpPresenter;

/**
 * Created by minageorge on 2/7/18.
 */

public interface SplashMvpPresenter <V extends SplashMvpView> extends MvpPresenter<V>  {

    void decideNextActivity();
}
