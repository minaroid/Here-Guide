package com.george888.mina.hereguide.ui.placeActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.george888.mina.hereguide.HereApp;
import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.adapters.PlaceToolBarPagerAdapter;
import com.george888.mina.hereguide.data.sql.FavContract;
import com.george888.mina.hereguide.pojo.PlaceInfo;
import com.george888.mina.hereguide.tasks.PlaceInfoLoader;
import com.george888.mina.hereguide.ui.base.BaseActivity;
import com.george888.mina.hereguide.ui.resultsActivity.ResultsActivity;
import com.george888.mina.hereguide.ui.resultsActivity.ResultsMvpView;

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
    @BindView(R.id.text_place_name)
    TextView placeName;
    @BindView(R.id.pager_indicator)
    CircleIndicator circleIndicator;
    @BindView(R.id.place_dis)
    TextView placeDistance;
    @BindView(R.id.place_rate)
    RatingBar placeRate;
    @BindView(R.id.place_activity)
    View view;
    private PlaceToolBarPagerAdapter adapter;
    private int loaderId = 90;
    private placePresenter presenter;
    private HereApp app = null;
    private ContentValues values = null;
    private Intent i;
    private boolean isInFavoriteList = false;
    private Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        i = getIntent();
        getSupportActionBar().setTitle("");
        placeName.setText(i.getStringExtra("name"));
        placeRate.setRating(Float.parseFloat(i.getStringExtra("rate")));
        placeDistance.setText(i.getStringExtra("dis"));
        Bundle mBundle = new Bundle();
        String lang = Locale.getDefault().getLanguage();

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + i.getStringExtra("id") + "&language=" +
                lang + "&key=" + getResources().getString(R.string.google_places_service_api_key) + "";
        mBundle.putString("url", url);
        getSupportLoaderManager().initLoader(loaderId, mBundle, mListLoaderCallbacks);

        adapter = new PlaceToolBarPagerAdapter(this);
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        app = ((HereApp) getApplicationContext());
        presenter = new placePresenter(app.getResolver());
        if (presenter.checkFavoritesList(i.getStringExtra("id"))) {
            isInFavoriteList = true;
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_place, menu);
        MenuItem menuItem = menu.findItem(R.id.place_fav);
        if (isInFavoriteList) {
            menuItem.setIcon(getResources().getDrawable(R.drawable.heart));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.place_fav:
                if (isInFavoriteList) {
                    removeFromFavorites(item);
                } else {
                    addToFavorites(item);
                }
                break;
            case R.id.place_share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
        }
        return true;
    }

    public void viewOnMap(View view) {
    }

    private void addToFavorites(MenuItem item) {
        values = new ContentValues();
        values.put(FavContract.FavListEntry.COL_PLACE_ID, i.getStringExtra("id"));
        values.put(FavContract.FavListEntry.COL_PLACE_NAME, i.getStringExtra("name"));
        values.put(FavContract.FavListEntry.COL_PLACE_RATE, i.getStringExtra("rate"));
        values.put(FavContract.FavListEntry.COL_PLACE_PHOTO, i.getStringExtra("ref"));
        if (presenter.addToFavoritesList(values)) {
            item.setIcon(getResources().getDrawable(R.drawable.heart));
            Snackbar.make(view, R.string.msg_fav_added, Snackbar.LENGTH_SHORT).show();
            isInFavoriteList = true;
            app.setNewFavoritesAdded(true);
        } else {
            Snackbar.make(view, R.string.msg_fav_error, Snackbar.LENGTH_SHORT).show();
        }
    }

    public void removeFromFavorites(final MenuItem item) {

        if (presenter.remove(i.getStringExtra("id"))) {
            snackbar = Snackbar.make(view, R.string.msg_fav_removed, Snackbar.LENGTH_SHORT);
            snackbar.setDuration(3000);
            snackbar.setAction(R.string.msg_fav_undo, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToFavorites(item);
                }
            });
            snackbar.show();
            item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_24dp));
            isInFavoriteList = false;
        } else {
            Snackbar.make(view, R.string.msg_fav_error, Snackbar.LENGTH_SHORT).show();
        }
    }


}
