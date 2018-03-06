package com.george888.mina.hereguide.data.sql;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by minageorge on 2/18/18.
 */

public class FavContentProvider extends ContentProvider {

    private FavHelper favHelper = null;
    private static final int FAVORITES = 100;
    private static final int FAVORITES_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();


    @Override
    public boolean onCreate() {
        favHelper = new FavHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        int match = sUriMatcher.match(uri);
        Cursor mCursor;
        final SQLiteDatabase database = favHelper.getReadableDatabase();

        switch (match) {
            case FAVORITES:
                mCursor = database.query(FavContract.FavListEntry.TABLE_NAME
                        , strings, s, strings1, null, null, s1);
                break;
            case FAVORITES_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = FavContract.FavListEntry.COL_PLACE_ID+"=?";
                String mSelectionArgs[] = new String[]{id};

                mCursor = database.query(FavContract.FavListEntry.TABLE_NAME
                        , strings, mSelection, mSelectionArgs, null, null, s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }

        mCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase database = favHelper.getWritableDatabase();
        Long rowId = database.insert(FavContract.FavListEntry.TABLE_NAME, null, contentValues);
        if (rowId > 0) {
            Uri uri1 = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(uri1, null);
            return uri1;
        }
        throw new SQLException("failed to insert record" + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int match = sUriMatcher.match(uri);
        final SQLiteDatabase database = favHelper.getWritableDatabase();
        int deleted = 0;
        switch (match) {
            case FAVORITES:
                deleted = database.delete(FavContract.FavListEntry.TABLE_NAME, null, null);
                break;
            case FAVORITES_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection =  FavContract.FavListEntry.COL_PLACE_ID+"=?";
                String mSelectionArgs[] = new String[]{id};
                deleted = database.delete(FavContract.FavListEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        if (deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int match = sUriMatcher.match(uri);
        final SQLiteDatabase database = favHelper.getWritableDatabase();
        int updated = 0;
        switch (match) {
            case FAVORITES:
                updated = database.update(FavContract.FavListEntry.TABLE_NAME,contentValues, null, null);
                break;
            case FAVORITES_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = "_id=?";
                String mSelectionArgs[] = new String[]{id};
                updated = database.update(FavContract.FavListEntry.TABLE_NAME,contentValues, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);
        }
        if (updated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }

    public static UriMatcher buildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavContract.PROVIDER_AUTH, FavContract.PATH_FAV, FAVORITES);
        matcher.addURI(FavContract.PROVIDER_AUTH, FavContract.PATH_FAV + "/*", FAVORITES_ID);

        return matcher;
    }
}
