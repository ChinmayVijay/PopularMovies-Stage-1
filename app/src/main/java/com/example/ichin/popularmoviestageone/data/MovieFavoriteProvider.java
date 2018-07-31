package com.example.ichin.popularmoviestageone.data;

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

import static com.example.ichin.popularmoviestageone.data.MovieContract.MovieListEntry.CONTENT_ITEM_TYPE;
import static com.example.ichin.popularmoviestageone.data.MovieContract.MovieListEntry.CONTENT_TYPE;
import static com.example.ichin.popularmoviestageone.data.MovieContract.MovieListEntry.CONTENT_URI;
import static com.example.ichin.popularmoviestageone.data.MovieContract.MovieListEntry.TABLE_NAME;
import static com.example.ichin.popularmoviestageone.data.MovieContract.PATH_MOVIES;

public class MovieFavoriteProvider extends ContentProvider {
    private static final int MOVIE =1000;
    private static final int MOVIE_WITH_ID = 1001;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MovieDbHelper dbHelper;
    @Override
    public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch(match){
            case MOVIE:
                cursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_WITH_ID:
                cursor = sqLiteDatabase.query(TABLE_NAME,
                        projection,
                        MovieContract.MovieListEntry.COLUMN_MOVIE_ID + "= ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
        if(getContext()!=null){
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match){
            case MOVIE:
                return CONTENT_TYPE;
            case MOVIE_WITH_ID :
                return CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch(match){
            case MOVIE:
                long id = sqLiteDatabase.insert(TABLE_NAME,null,values);
                if(id == -1){
                    sqLiteDatabase.close();
                    throw new SQLException("Failed to insert favorite movie into : " + uri);
                }
                Uri returnUri = ContentUris.withAppendedId(CONTENT_URI,id);
                if(getContext()!=null){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                sqLiteDatabase.close();
                return returnUri;

                default:
                    sqLiteDatabase.close();
                    throw new UnsupportedOperationException("Unknown Uri : " +uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int movieDeleted;
        switch(match){
            case MOVIE:
                movieDeleted = sqLiteDatabase.delete(TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri : " + uri);

        }
        if(movieDeleted > 0){
            if(getContext()!=null){
                getContext().getContentResolver().notifyChange(uri,null);
            }
        }
        sqLiteDatabase.close();
        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private static UriMatcher buildUriMatcher(){
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY,PATH_MOVIES,MOVIE);
        uriMatcher.addURI(MovieContract.AUTHORITY,PATH_MOVIES+"/#",MOVIE_WITH_ID);

        return uriMatcher;
    }
}
