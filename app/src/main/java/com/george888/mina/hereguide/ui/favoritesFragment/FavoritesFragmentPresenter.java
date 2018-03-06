package com.george888.mina.hereguide.ui.favoritesFragment;


import android.content.ContentResolver;
import android.content.ContentValues;

import com.george888.mina.hereguide.data.sql.FavContract;
import com.george888.mina.hereguide.ui.base.BasePresenter;

/**
 * Created by minageorge on 2/8/18.
 */

public class FavoritesFragmentPresenter<V extends FavoritesFragmentMvpView>
        extends BasePresenter<V>
        implements FavoritesFragmentMvpPresenter<V> {
    private ContentResolver resolver;

    FavoritesFragmentPresenter(ContentResolver resolver) {
        this.resolver = resolver;
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
