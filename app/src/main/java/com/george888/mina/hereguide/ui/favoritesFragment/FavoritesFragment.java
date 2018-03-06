package com.george888.mina.hereguide.ui.favoritesFragment;


import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.adapters.FavoritesAdapter;
import com.george888.mina.hereguide.data.sql.FavContract;
import com.george888.mina.hereguide.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by minageorge on 2/8/18.
 */

public class FavoritesFragment extends BaseFragment implements FavoritesFragmentMvpView, LoaderManager.LoaderCallbacks<Cursor> {
    @BindView(R.id.home_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.place_progress)
    ProgressBar progressBar;
    private GridLayoutManager mLayoutManager = null;
    private FavoritesAdapter favoritesAdapter = null;
    private static final int loaderID = 500;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorits, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        favoritesAdapter = new FavoritesAdapter(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(favoritesAdapter);
        getLoaderManager().initLoader(loaderID, null, this);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_results_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.rows:
//                switchLayout();
//                switchIcon(item);
//                return true;
//        }
        return true;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        String[] projection = {FavContract.FavListEntry.COL_PLACE_ID, FavContract.FavListEntry.COL_PLACE_NAME,
                FavContract.FavListEntry.COL_PLACE_RATE, FavContract.FavListEntry.COL_PLACE_PHOTO};
        System.out.println(new CursorLoader(getActivity(),FavContract.FavListEntry.CONTENT_URI,projection,null,null,null));
        return new CursorLoader(getActivity(),FavContract.FavListEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        progressBar.setVisibility(View.GONE);

        favoritesAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void restartLoader() {
        getLoaderManager().restartLoader(loaderID, null, this);

    }
}
