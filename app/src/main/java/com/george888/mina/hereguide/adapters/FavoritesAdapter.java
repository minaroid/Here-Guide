package com.george888.mina.hereguide.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * Created by minageorge on 2/18/18.
 */

public class FavoritesAdapter extends CursorAdapter {

    public FavoritesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//        return LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);

        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
