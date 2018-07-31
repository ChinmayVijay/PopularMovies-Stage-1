package com.example.ichin.popularmoviestageone.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.example.ichin.popularmoviestageone";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ AUTHORITY);

    public static final String PATH_MOVIES= "movies";


    public static final class MovieListEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                '/' + AUTHORITY + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                '/' + AUTHORITY + PATH_MOVIES;
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "date";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop";
        public static final String COLUMN_MOVIE_POSTER_PATH = "poster";

    }
}
