package com.george888.mina.hereguide.ui.base;

/**
 * Created by minageorge on 2/7/18.
 */

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private V mMvpView;

    public BasePresenter(){

    }

    @Override
    public void onAttach(V mvpView) {

        mMvpView = mvpView;

    }


    public V getMvpView() {
        return mMvpView;
    }
}
