package com.george888.mina.hereguide.ui.homeActivity;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.george888.mina.hereguide.HereApp;
import com.george888.mina.hereguide.R;
import com.george888.mina.hereguide.ui.base.BaseActivity;
import com.george888.mina.hereguide.ui.favoritesFragment.FavoritesFragment;
import com.george888.mina.hereguide.ui.homeFragment.HomeFragment;
import com.george888.mina.hereguide.ui.resultsActivity.ResultsActivity;
import com.george888.mina.hereguide.ui.settingsFragment.SettingsFragment;
import com.george888.mina.hereguide.utils.CurrentLocation;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by minageorge on 2/7/18.
 */

public class HomeActivity extends BaseActivity implements HomeMvpView,
        NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.navigation_view)
    NavigationView DrawerNavigationView;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.empty_view)
    LinearLayout emptyView;
    private ActionBarDrawerToggle toggle = null;
    private HereApp app = null;
    private CurrentLocation currentLocation = null;
    private HomePresenter homePresenter = null;
    private Intent ResultsActivityintent = null;
    private static String TAG = HomeActivity.class.getSimpleName();
    private static int permissionId = 50;
    private FragmentManager fragmentManager = null;
    private String[] fragments;
    private boolean isHomePreferenceChaged = false;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.tab_home);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        DrawerNavigationView.setNavigationItemSelectedListener(this);
        app = ((HereApp) getApplicationContext());
        homePresenter = new HomePresenter();
        fragmentManager = getSupportFragmentManager();
        fragments = new String[]{
                FavoritesFragment.class.getSimpleName(),
                HomeFragment.class.getSimpleName(),
                SettingsFragment.class.getSimpleName()
        };

        if (app.isConnected()) {
            initBottomNavigation();
            if (checkLocationPermission()) {
                currentLocation = new CurrentLocation(this);
            }
        } else {
            noConnectionState();
        }
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void noConnectionState() {
        emptyView.setVisibility(View.VISIBLE);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home_id);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (app.isConnected()) {
                    emptyView.setVisibility(View.GONE);
                    initBottomNavigation();
                    if (checkLocationPermission()) {
                        currentLocation = new CurrentLocation(HomeActivity.this);
                    }
                }
            }
        });
    }

    private void initBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_fav_id:
                        getSupportActionBar().setTitle(R.string.tab_fav);
                        inflateFragment(0);
                        break;
                    case R.id.bottom_home_id:
                        getSupportActionBar().setTitle(R.string.tab_home);
                        inflateFragment(1);
                        break;
                    case R.id.bottom_sett_id:
                        getSupportActionBar().setTitle(R.string.tab_sett);
                        inflateFragment(2);
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.bottom_home_id);
    }

    private void inflateFragment(int index) {
        String tag;
        Fragment selectedFragment = null;
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (index) {

            case 0:
                tag = fragments[index];
                selectedFragment = fragmentManager.findFragmentByTag(tag);
                if (app.isNewFavoritesAdded()) {
                    transaction.replace(R.id.container, selectedFragment, tag)
                            .detach(selectedFragment).attach(selectedFragment)
                            .commit();
                    app.setNewFavoritesAdded(false);
                } else {

                    if (selectedFragment == null) {
                        selectedFragment = new FavoritesFragment();
                        transaction.addToBackStack(tag);
                    }
                    transaction.replace(R.id.container, selectedFragment, tag).commit();
                }
                break;

            case 1:
                tag = fragments[index];
                selectedFragment = fragmentManager.findFragmentByTag(tag);
                if (isHomePreferenceChaged) {
                    transaction.replace(R.id.container, selectedFragment, tag)
                            .detach(selectedFragment).attach(selectedFragment)
                            .commit();
                    isHomePreferenceChaged = false;
                } else {

                    if (selectedFragment == null) {
                        selectedFragment = new HomeFragment();
                        transaction.addToBackStack(tag);
                    }
                    transaction.replace(R.id.container, selectedFragment, tag).commit();
                }
                break;

            case 2:
                tag = fragments[index];
                selectedFragment = fragmentManager.findFragmentByTag(tag);
                if (selectedFragment == null) {
                    selectedFragment = new SettingsFragment();
                    transaction.addToBackStack(tag);
                }
                transaction.replace(R.id.container, selectedFragment, tag).commit();
                break;
        }
    }

    public boolean checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, permissionId);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_tab_home:
                bottomNavigationView.setSelectedItemId(R.id.bottom_home_id);
                break;
            case R.id.nav_tab_fav:
                bottomNavigationView.setSelectedItemId(R.id.bottom_fav_id);
                break;
            case R.id.nav_tab_sett:
                bottomNavigationView.setSelectedItemId(R.id.bottom_sett_id);
                break;
            case R.id.nav_rate:
                Toast.makeText(this, "Rate", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_report:
                Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permissionId) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                checkLocationPermission();
            } else {
                currentLocation = new CurrentLocation(this);
            }
        }
    }

    @Override
    public void openResultsActivity(String title, String type) {
        if (app.isConnected() && currentLocation.getLocatinLongitude() != null) {
            ResultsActivityintent = new Intent(HomeActivity.this, ResultsActivity.class);
            ResultsActivityintent.putExtra("title", title);
            ResultsActivityintent.putExtra("type", type);
            ResultsActivityintent.putExtra("lat", currentLocation.getLocatinLatitude());
            ResultsActivityintent.putExtra("lon", currentLocation.getLocatinLongitude());
            startActivity(ResultsActivityintent);
        } else {
            Toast.makeText(this, R.string.msg_offline, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            if (bottomNavigationView.getSelectedItemId() == R.id.bottom_home_id) {
//                super.finish();
//            } else {
//                bottomNavigationView.setSelectedItemId(R.id.bottom_home_id);
//            }
//        }
        Toast.makeText(this, String.valueOf(fragmentManager.getBackStackEntryCount()), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.list_key))) {
            isHomePreferenceChaged = true;
        }
        Toast.makeText(this, getString(R.string.msg_changes), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
