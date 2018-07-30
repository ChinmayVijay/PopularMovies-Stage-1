package com.example.ichin.popularmoviestageone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ichin.popularmoviestageone.adapters.TrailerViewAdapter;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.model.MovieTrailerResponse;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.model.Result;
import com.example.ichin.popularmoviestageone.rest.MovieApiClient;
import com.example.ichin.popularmoviestageone.rest.MovieApiInterface;
import com.example.ichin.popularmoviestageone.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ichin.popularmoviestageone.utilities.Utils.API_KEY;

public class DetailsActivity extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    private ImageView posterImage;
    private TextView movieTitle;
    private TextView movieOverview;
    private TextView movieReleaseDate;
    private TextView movieRating;
    private Movies movie;
    private long movieId;
    private TrailerViewAdapter trailerViewAdapter;
    private OnItemClickListener mListener;
    private RecyclerView trailerRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        movieTitle = findViewById(R.id.tv_movie_detail_title_text);
        posterImage = findViewById(R.id.iv_movie_detail_poster);
        movieOverview = findViewById(R.id.tv_movie_detail_overview_text);
        movieReleaseDate = findViewById(R.id.tv_movie_detail_release_date_text);
        movieRating = findViewById(R.id.tv_movie_detail_user_rating_text);

        trailerRecyclerView = findViewById(R.id.rv_movieTrailers);

        movie = getIntent().getParcelableExtra(MainActivity.PROP_MOVIES);

        movieTitle.setText(movie.getTitle());

        Picasso.get().setLoggingEnabled(true);
        String imageUrl = Utils.IMAGE_BASE_URL + movie.getPosterPath();
        Log.d(TAG, imageUrl);

        int size = (int) Math.ceil(Math.sqrt(Utils.MAX_WIDTH * Utils.MAX_HEIGHT));
//        Picasso.with(DetailsActivity.this).load(imageUrl).resize(size,size).into(posterImage);
        Picasso.get().load(imageUrl).resize(size,size).into(posterImage);

        movieOverview.setText(movie.getOverview());

        movieReleaseDate.setText(movie.getReleaseDate());

        movieRating.setText(String.valueOf(movie.getVoteAverage()));
        movieRating.append("/10.0");


        getMoviesTrailer();

    }

    private void getMoviesTrailer() {
        MovieApiInterface movieApiService = MovieApiClient.getClient().create(MovieApiInterface.class);
        Call<MovieTrailerResponse> moviesTrailerResponseCall= movieApiService.getMovieTrailers(movie.getId(), API_KEY);
        moviesTrailerResponseCall.enqueue(new Callback<MovieTrailerResponse>() {
            @Override
            public void onResponse(Call<MovieTrailerResponse> call, Response<MovieTrailerResponse> response) {
                Log.d(TAG,"id is "+response.body().getResults().get(0).getType());
                movieId = response.body().getId();
                List<Result> movieTrailerResult = response.body().getResults();
                Log.d(TAG, "total trailers: " + movieTrailerResult.size());
                Log.d(TAG, "total trailers: " + "movie id: "+movieId);
                trailerViewAdapter = new TrailerViewAdapter(getApplicationContext(),movieTrailerResult,mListener,R.layout.recyclerview_trailers);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                trailerRecyclerView.setLayoutManager(layoutManager);
                trailerRecyclerView.setHasFixedSize(true);
                trailerRecyclerView.setAdapter(trailerViewAdapter);

            }

            @Override
            public void onFailure(Call<MovieTrailerResponse> call, Throwable t) {

            }
        });
    }
}
