package com.george888.mina.hereguide.ui.resultsActivity;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.george888.mina.hereguide.HereApp;
import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.adapters.ResultsRecyclerAdapter;
import com.george888.mina.hereguide.pojo.ResultsPlace;
import com.george888.mina.hereguide.tasks.TypeResultsLoader;
import com.george888.mina.hereguide.ui.base.BaseActivity;
import com.george888.mina.hereguide.ui.placeActivity.placeActivity;

import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by minageorge on 2/10/18.
 */

public class ResultsActivity extends BaseActivity implements ResultsMvpView ,
        SearchView.OnQueryTextListener
        , SearchView.OnCloseListener{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.place_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.place_progress)
    ProgressBar progressBar;
    private GridLayoutManager mLayoutManager = null;
    private ResultsRecyclerAdapter adapter = null;
    private static int loaderId = 50;
    private static String TAG = ResultsActivity.class.getSimpleName();
    private Intent intentFromHomeAct = null;
    private ActionBar actionBar = null;
    private HereApp app = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        intentFromHomeAct = getIntent();
        if (intentFromHomeAct != null) {
            actionBar.setTitle(intentFromHomeAct.getStringExtra("title"));
            mLayoutManager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new ResultsRecyclerAdapter(this, mLayoutManager);
            recyclerView.setAdapter(adapter);
            getLoaderManager().initLoader(loaderId, getUrl(intentFromHomeAct), mListLoaderCallbacks);
        }
        app = ((HereApp) getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_results_toolbar, menu);
        MenuItem menuItem = menu.findItem(R.id.results_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(ResultsActivity.this);
        searchView.setOnCloseListener(ResultsActivity.this);
        searchView.setQueryHint(intentFromHomeAct.getStringExtra("title"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bundle getUrl(Intent intent) {
        Bundle bundle = new Bundle();
        String lang = Locale.getDefault().getLanguage();
        String placesUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                intent.getStringExtra("lat") + "," + intent.getStringExtra("lon") +
                "&radius=50000&type=" + intent.getStringExtra("type")
                + "&language=" + lang +
                "&key=" + getResources().getString(R.string.google_places_service_api_key) + "";
        bundle.putString("url", placesUrl);
        return bundle;
    }

    public LoaderManager.LoaderCallbacks<List<ResultsPlace>> mListLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<List<ResultsPlace>>() {
                @Override
                public Loader<List<ResultsPlace>> onCreateLoader(int id, Bundle args) {
                    progressBar.setVisibility(View.VISIBLE);
                    return new TypeResultsLoader(ResultsActivity.this, args);
                }

                @Override
                public void onLoadFinished(Loader<List<ResultsPlace>> loader, List<ResultsPlace> data) {
                    progressBar.setVisibility(View.GONE);
                    adapter.swapData(data);
                }

                @Override
                public void onLoaderReset(Loader<List<ResultsPlace>> loader) {

                }
            };

    @Override
    public boolean onClose() {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void openPlaceActivity(String id,String name,String rate,String dis,String photoReference,String lat,String lng) {
        if(app.isConnected())
        {
            Intent i = new Intent(this,placeActivity.class);
            i.putExtra("id",id);
            i.putExtra("name",name);
            i.putExtra("rate",rate);
            i.putExtra("dis",dis);
            i.putExtra("ref", photoReference);
            i.putExtra("lat", lat);
            i.putExtra("lng", lng);
          startActivity(i);
        }else{
            Toast.makeText(this,R.string.msg_offline,Toast.LENGTH_SHORT).show();
        }
    }
}
