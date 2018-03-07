package com.george888.mina.hereguide.ui.homeActivity;

import com.george888.mina.hereguide.ui.base.MvpView;

/**
 * Created by minageorge on 2/7/18.
 */

public interface HomeMvpView extends MvpView {

    void openResultsActivity(String title,String type);

    void openPlaceActivity(String id , String name ,String rate,String dis);

}
