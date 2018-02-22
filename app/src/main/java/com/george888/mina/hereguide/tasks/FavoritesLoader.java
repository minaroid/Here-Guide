package com.george888.mina.hereguide.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.george888.mina.hereguide.data.sql.FavContract;
import com.george888.mina.hereguide.pojo.ResultsPlace;

import java.util.List;


/**
 * Created by minageorge on 2/18/18.
 */

public class FavoritesLoader extends AsyncTaskLoader<Cursor> {
    private Cursor mData = null;


    public FavoritesLoader(Context context, Bundle bundle) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }

    @Override
    public Cursor loadInBackground() {
        try {
            return getContext().getContentResolver().query(
                    FavContract.FavListEntry.CONTENT_URI
                    , null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deliverResult(Cursor data) {
        mData = data;
        super.deliverResult(data);
    }

}
