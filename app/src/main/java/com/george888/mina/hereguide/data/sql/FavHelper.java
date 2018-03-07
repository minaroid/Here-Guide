package com.george888.mina.hereguide.data.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by minageorge on 2/18/18.
 */

public class FavHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "favList.db";
    private static final int DATABASE_VERSION = 3;

    public FavHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                FavContract.FavListEntry.TABLE_NAME + " ( " +
                FavContract.FavListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavContract.FavListEntry.COL_PLACE_NAME + " TEXT NOT NULL, " +
                FavContract.FavListEntry.COL_PLACE_PHOTO + " TEXT NOT NULL, " +
                FavContract.FavListEntry.COL_PLACE_ID + " TEXT NOT NULL, " +
                FavContract.FavListEntry.COL_PLACE_RATE + " TEXT NOT NULL, " +
                FavContract.FavListEntry.COL_PLACE_LAT + " TEXT NOT NULL, " +
                FavContract.FavListEntry.COL_PLACE_LNG + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavContract.FavListEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
