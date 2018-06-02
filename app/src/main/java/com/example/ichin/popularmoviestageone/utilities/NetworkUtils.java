package com.example.ichin.popularmoviestageone.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=5af869fcf0fa026d1847f32746a1f5c2
    private static final String TMDB_BASE_QUERY="http://api.themoviedb.org/3/discover/movie";
    private static final String QUERY_SORT_BY = "sort_by";
    private static final String PARAM_SORT="popularity.desc";
    private static final String QUERY_API_KEY = "api_key";
    private static final String API_KEY = "5af869fcf0fa026d1847f32746a1f5c2";

    public static URL buildUrl(){
        Uri builtUri = Uri.parse(TMDB_BASE_QUERY).buildUpon()
                .appendQueryParameter(QUERY_SORT_BY,PARAM_SORT)
                .appendQueryParameter(QUERY_API_KEY,API_KEY)
                .build();
        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("url", url.toString());
        return url;
    }

    public static String getResponseFromURL(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner sc = new Scanner(in);
            sc.useDelimiter("\\A");
            boolean hasInput = sc.hasNext();
            if(hasInput){
                return sc.next();
            }
            else{
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }


    }
}
