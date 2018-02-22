package com.george888.mina.hereguide.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by minageorge on 2/7/18.
 */

public class CurrentLocation implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient = null;
    private String LocationLatitude = null;
    private String LocationLongitude = null;
    private String TAG = CurrentLocation.class.getSimpleName();

    public CurrentLocation(Context c) {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(c)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        try {
            //noinspection MissingPermission
            Location userCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            setLocatinLatitude(String.valueOf(userCurrentLocation.getLatitude()));
            setLocatinLongitude(String.valueOf(userCurrentLocation.getLongitude()));
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        googleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public String getLocatinLatitude() {
        return LocationLatitude;
    }

    public void setLocatinLatitude(String locatinLatitude) {
        LocationLatitude = locatinLatitude;
    }

    public String getLocatinLongitude() {
        return LocationLongitude;
    }

    public void setLocatinLongitude(String locatinLongitude) {
        LocationLongitude = locatinLongitude;
    }

    public void connect(){
        googleApiClient.connect();
    }
}
