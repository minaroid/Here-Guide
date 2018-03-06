package com.george888.mina.hereguide;

import android.app.Application;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.george888.mina.hereguide.utils.CommonUtils;
import com.george888.mina.hereguide.utils.CurrentLocation;

/**
 * Created by minageorge on 2/7/18.
 */

public class HereApp extends Application {
    private CommonUtils utils = null;
    private boolean isConnected = false;
    private int columns;
    private static String TAG = HereApp.class.getSimpleName();
    private ContentResolver contentResolver;
    private boolean NewFavoritesAdded = false;
    private String LocatinLatitude, LocatinLongitude;
    private String distanceType;
    private SharedPreferences sharedPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        utils = new CommonUtils();
        testConnection();
        setColumns(utils.calculateNoOfColumns(getApplicationContext()));
        contentResolver = getContentResolver();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        DistanceType();
    }

    public void DistanceType() {
        setDistanceType(sharedPrefs.getString(getResources().getString(R.string.pref_dis_key), null));
    }

    public String getDistanceType() {
        return distanceType;
    }

    public void setDistanceType(String distanceType) {
        this.distanceType = distanceType;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }


    public void testConnection() {
        setConnected(utils.isConnected(this));

    }

    public ContentResolver getResolver() {

        return contentResolver;
    }

    public boolean isNewFavoritesAdded() {
        return NewFavoritesAdded;
    }

    public void setNewFavoritesAdded(boolean newFavoritesAdded) {
        NewFavoritesAdded = newFavoritesAdded;
    }

    public String getLocatinLatitude() {
        return LocatinLatitude;
    }

    public void setLocatinLatitude(String locatinLatitude) {
        LocatinLatitude = locatinLatitude;
    }

    public String getLocatinLongitude() {
        return LocatinLongitude;
    }

    public void setLocatinLongitude(String locatinLongitude) {
        LocatinLongitude = locatinLongitude;
    }

}
