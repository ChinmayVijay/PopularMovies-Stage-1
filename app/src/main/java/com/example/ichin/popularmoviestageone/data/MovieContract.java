package com.example.ichin.popularmoviestageone.data;

import android.provider.BaseColumns;

public class MovieContract {

    public static final class MovieListEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";
        public static final String MOVIE_ID = "id";
        public static final String MOVIE_TITLE = "title";
        public static final String MOVIE_RELEASE_DATE = "date";
        public static final String MOVIE_DURATION = "duration";
        public static final String MOVIE_RATING = "rating";
        public static final String MOVIE_BACKDROP_PATH = "backdrop";
        public static final String MOVIE_POSTER_PATH = "poster";

    }
}
