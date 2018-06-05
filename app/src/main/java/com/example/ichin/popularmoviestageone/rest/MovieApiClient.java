package com.example.ichin.popularmoviestageone.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApiClient {
    public static final String BASE_URL="http://api.themoviedb.org/3/";
    private static Retrofit retrofitInstance= null;

    public static Retrofit getClient(){
        if(retrofitInstance == null){
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }
}
