package com.example.ichin.popularmoviestageone.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favmovies.db";

    private static final int DATABSE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieListEntry.TABLE_NAME + " ("+
                        MovieContract.MovieListEntry.MOVIE_ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                        MovieContract.MovieListEntry.MOVIE_DURATION         + " INTEGER, "                              +
                        MovieContract.MovieListEntry.MOVIE_RELEASE_DATE     + " TEXT, "                                 +
                        MovieContract.MovieListEntry.MOVIE_RATING           + " REAL, "                                 +
                        MovieContract.MovieListEntry.MOVIE_BACKDROP_PATH    + " TEXT, "                                 +
                        MovieContract.MovieListEntry.MOVIE_POSTER_PATH      + " TEXT "                                  + ");";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.MovieListEntry.TABLE_NAME);
        onCreate(db);
    }
}
