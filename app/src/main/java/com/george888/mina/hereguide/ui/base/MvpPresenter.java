package com.george888.mina.hereguide.ui.base;

/**
 * Created by minageorge on 2/7/18.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

}