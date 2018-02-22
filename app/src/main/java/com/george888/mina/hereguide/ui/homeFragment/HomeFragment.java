package com.george888.mina.hereguide.ui.homeFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.george888.mina.hereguide.HereApp;
import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.adapters.HomeRecyclerAdapter;
import com.george888.mina.hereguide.pojo.HomeType;
import com.george888.mina.hereguide.ui.base.BaseFragment;
import com.george888.mina.hereguide.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by minageorge on 2/8/18.
 */

public class HomeFragment extends BaseFragment implements HomeFragmentMvpView, SearchView.OnQueryTextListener
        , SearchView.OnCloseListener {
    @BindView(R.id.home_recycler)
    RecyclerView recyclerView;
    private static int columns;
    private GridLayoutManager mLayoutManager = null;
    private HomeRecyclerAdapter adapter = null;
    private String currentSearchText = "";
    private static String TAG = HomeFragment.class.getSimpleName();
    private ArrayList<HomeType> places = null;
    private CommonUtils utils = new CommonUtils();



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        columns = utils.calculateNoOfColumns(getContext());
        adapter = new HomeRecyclerAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        places = getHomePlaces();
        adapter.swapData(places);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        adapter.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home_and_fav_toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(HomeFragment.this);
        searchView.setOnCloseListener(HomeFragment.this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rows:
                switchLayout();
                switchIcon(item);
                return true;
        }
        return true;
    }


    @Override
    public boolean onClose() {
        currentSearchText = "";
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        currentSearchText = newText.toLowerCase();
        ArrayList<HomeType> newList = new ArrayList<HomeType>();
        ArrayList<HomeType> oldLis = places;
        for (HomeType item : oldLis) {
            String name = item.getTitle().toLowerCase();
            if (name.contains(currentSearchText)) {
                newList.add(item);
            }
        }
        adapter.swapData(newList);
        return true;
    }


    private ArrayList<HomeType> getHomePlaces() {

        String[] placesTypes = getResources().getStringArray(R.array.places_types);
        String[] placesTitles = getResources().getStringArray(R.array.places_titles);
        ArrayList<HomeType> place = new ArrayList<>();
        for (int i = 0; i < placesTypes.length; i++) {
            place.add(new HomeType(placesTitles[i], placesTypes[i], R.drawable.restaurant));
        }
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Set<String> selected = sharedPrefs.getStringSet(getResources().getString(R.string.list_key), null);

        if (selected != null) {
            if (selected.size() != 0) {
                String[] defaults = new String[selected.size()];
                Iterator itr = selected.iterator();
                int index = 0;
                while (itr.hasNext()) {
                    defaults[index] = (String) itr.next();
                    index++;
                }

                ArrayList<HomeType> newList = new ArrayList<HomeType>();
                ArrayList<HomeType> oldLis = place;
                for (HomeType item : oldLis) {
                    for (int x = 0; x < defaults.length; x++) {
                        if (item.getType().equals(defaults[x])) {
                            newList.add(item);
                        }
                    }
                }
                place.clear();
                place = newList;
            }
        }
        return place;
    }

    private void switchIcon(MenuItem item) {
        if (mLayoutManager.getSpanCount() >= 2) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_view_headline_black_36dp));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_view_module_black_36dp));
        }
    }

    private void switchLayout() {
        int currentState = mLayoutManager.getSpanCount();
        if (currentState == 1) {
            mLayoutManager.setSpanCount(columns);
        } else {
            mLayoutManager.setSpanCount(1);
        }
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }


}
