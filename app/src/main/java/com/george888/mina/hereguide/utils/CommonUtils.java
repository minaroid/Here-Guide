package com.george888.mina.hereguide.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;


/**
 * Created by minageorge on 2/7/18.
 */

public class CommonUtils {

    public boolean isConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null) {
            return true;
        } else {
            return false;
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

}
