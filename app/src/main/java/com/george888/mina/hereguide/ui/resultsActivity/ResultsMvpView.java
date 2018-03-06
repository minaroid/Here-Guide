package com.george888.mina.hereguide.ui.resultsActivity;

import com.george888.mina.hereguide.ui.base.MvpView;

/**
 * Created by minageorge on 2/10/18.
 */

public interface ResultsMvpView extends MvpView {

    void openPlaceActivity(String id,String name,String rate,double dis,String photoReference);

}
