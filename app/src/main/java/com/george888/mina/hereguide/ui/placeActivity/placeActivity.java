package com.george888.mina.hereguide.ui.placeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.adapters.PlaceToolBarPagerAdapter;
import com.george888.mina.hereguide.pojo.PlaceInfo;
import com.george888.mina.hereguide.tasks.PlaceInfoLoader;
import com.george888.mina.hereguide.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by minageorge on 2/19/18.
 */

public class placeActivity extends BaseActivity implements placeMvpView {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.pager_indicator)
    CircleIndicator circleIndicator;
    private PlaceToolBarPagerAdapter adapter;
    private int loaderId = 90;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        getSupportActionBar().setTitle(i.getStringExtra("name"));


        Bundle mBundle = new Bundle();
        String langu = Locale.getDefault().getLanguage();

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + i.getStringExtra("id") + "&language=" +
                langu + "&key=" + getResources().getString(R.string.google_places_service_api_key) + "";
        mBundle.putString("url", url);
        getSupportLoaderManager().initLoader(loaderId, mBundle, mListLoaderCallbacks);

        adapter = new PlaceToolBarPagerAdapter(this);
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
    }

    public LoaderManager.LoaderCallbacks<List<PlaceInfo>> mListLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<List<PlaceInfo>>() {
                @Override
                public Loader<List<PlaceInfo>> onCreateLoader(int id, Bundle args) {
                    return new PlaceInfoLoader(getApplicationContext(), args);
                }

                @Override
                public void onLoadFinished(Loader<List<PlaceInfo>> loader, List<PlaceInfo> data) {
                    setPagerData(data);
                }

                @Override
                public void onLoaderReset(Loader<List<PlaceInfo>> loader) {

                }
            };

    private void setPagerData(List<PlaceInfo> data) {
        ArrayList<String> photosList = data.get(0).getPhotos();
        adapter = new PlaceToolBarPagerAdapter(this);
        viewPager.setAdapter(adapter);
        if (photosList != null) {
            adapter.swapData(photosList);
            circleIndicator.setViewPager(viewPager);
        }

    }
}
