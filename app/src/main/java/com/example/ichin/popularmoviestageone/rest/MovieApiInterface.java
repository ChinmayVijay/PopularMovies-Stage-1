package com.example.ichin.popularmoviestageone.rest;

import com.example.ichin.popularmoviestageone.model.MovieReviewsResponse;
import com.example.ichin.popularmoviestageone.model.MovieTrailerResponse;
import com.example.ichin.popularmoviestageone.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiInterface {
    @GET("discover/movie")
    Call<MoviesResponse> getPopularMovies(@Query("sort_by")String sortBy, @Query("api_key")String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key")String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerResponse> getMovieTrailers(@Path("id") long movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewsResponse> getMovieReviews(@Path("id") long movieId, @Query("api_key") String apiKey);



}
