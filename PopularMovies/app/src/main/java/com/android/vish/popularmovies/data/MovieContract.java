package com.android.vish.popularmovies.data;

import android.provider.BaseColumns;

public class MovieContract {

    public static final class FavoriteEntry implements BaseColumns {

        public static final String TABLE_NAME                = "favoriteMovies";
        public static final String COLUMN_MOVIE_TITLE        = "title";
        public static final String COLUMN_MOVIE_POSTER       = "poster";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_RATING       = "rating";
        public static final String COLUMN_MOVIE_PLOT         = "plot";
    }


}
