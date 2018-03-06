package com.george888.mina.hereguide.ui.placeActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;


import com.george888.mina.hereguide.data.sql.FavContract;
import com.george888.mina.hereguide.ui.base.BasePresenter;

/**
 * Created by minageorge on 2/19/18.
 */

public class placePresenter<V extends placeMvpView> extends BasePresenter<V>
        implements placeMvpPresenter<V> {

    private ContentResolver resolver;

    public placePresenter(ContentResolver resolver) {
        this.resolver = resolver;
    }

    public boolean addToFavoritesList(ContentValues contentValues) {
        try {
            resolver.insert(FavContract.BASE_CONTENT_URI, contentValues);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkFavoritesList(String placeId) {
        Cursor cursor = resolver.query(FavContract.FavListEntry.CONTENT_URI.buildUpon()
                        .appendPath(placeId).build(), null, null, null
                , null);
        if (cursor.getCount() == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(String placeId) {
        int x = resolver.delete(FavContract.FavListEntry.CONTENT_URI.buildUpon()
                .appendPath(placeId).build(), null, null);
        if (x != 0) {
            return true;
        } else {
            return false;

        }
    }
}
