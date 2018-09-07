package com.vish.travelbook.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class TripContract {

    public static final String AUTHORITY        = "com.vish.travelbook";
    public static final Uri    BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_TRIPS       = "trips";

    public static final class TripEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRIPS).build();

        public static final String TABLE_NAME        = "trips";
        public static final String COLUMN_TRIP_TITLE = "title";
        public static final String COLUMN_TRIP_START = "startTime";
        public static final String COLUMN_TRIP_END   = "endTime";
        public static final String COLUMN_TRIP_IMAGE = "image";
    }

}
