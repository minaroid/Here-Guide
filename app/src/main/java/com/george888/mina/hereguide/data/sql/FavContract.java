package com.george888.mina.hereguide.data.sql;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by minageorge on 2/18/18.
 */

public class FavContract {
    public static final String PROVIDER_AUTH = "com.minageorge888.hereguide";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + PROVIDER_AUTH);
    public static final String PATH_FAV = "favorites";

    private FavContract() {

    }

    public static class FavListEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();

        public static final String TABLE_NAME = "favList";
        public static final String COL_PLACE_NAME = "placeName";
        public static final String COL_PLACE_ID = "placeID";
        public static final String COL_PLACE_PHOTO = "placePhoto";
        public static final String COL_PLACE_RATE = "placeRate";
        public static final String COL_PLACE_LAT = "lat";
        public static final String COL_PLACE_LNG = "ln";
    }
}
