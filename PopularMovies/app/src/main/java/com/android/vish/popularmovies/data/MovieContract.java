package com.android.vish.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY        = "com.android.vish.popularmovies";
    public static final Uri    BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES      = "favoriteMovies";


    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME                = "favoriteMovies";
        public static final String COLUMN_MOVIE_TITLE        = "title";
        public static final String COLUMN_MOVIE_POSTER       = "poster";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_RATING       = "rating";
        public static final String COLUMN_MOVIE_PLOT         = "plot";
    }


}
