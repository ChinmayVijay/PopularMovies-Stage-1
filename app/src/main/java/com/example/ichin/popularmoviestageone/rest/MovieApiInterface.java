package com.example.ichin.popularmoviestageone.rest;

import com.example.ichin.popularmoviestageone.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApiInterface {
    @GET("discover/movie")
    Call<MoviesResponse> getPopularMovies(@Query("sort_by")String sortBy, @Query("api_key")String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key")String apiKey);

}
