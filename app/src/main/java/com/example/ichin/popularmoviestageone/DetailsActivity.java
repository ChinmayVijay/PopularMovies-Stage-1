package com.example.ichin.popularmoviestageone;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ichin.popularmoviestageone.adapters.MovieReviewAdapter;
import com.example.ichin.popularmoviestageone.adapters.TrailerViewAdapter;
import com.example.ichin.popularmoviestageone.data.MovieContract;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.listener.TrailerListener;
import com.example.ichin.popularmoviestageone.model.MovieReviewsResponse;
import com.example.ichin.popularmoviestageone.model.MovieTrailerResponse;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.model.Result;
import com.example.ichin.popularmoviestageone.model.ReviewResult;
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
    private TextView movieOverview;
    private TextView movieReleaseDate;
    private TextView movieRating;
    private Movies movie;
    private long movieId;
    private TrailerViewAdapter trailerViewAdapter;
    private OnItemClickListener mListener;
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private MovieReviewAdapter movieReviewAdapter;
    private FloatingActionButton fabFavMovie;
    private boolean isMovieFavorite = false;
    private TrailerListener trailerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        TextView movieTitle = findViewById(R.id.tv_movie_detail_title_text);
        ImageView posterImage = findViewById(R.id.iv_movie_detail_poster);
        movieOverview = findViewById(R.id.tv_movie_detail_overview_text);
        movieReleaseDate = findViewById(R.id.tv_movie_detail_release_date_text);
        movieRating = findViewById(R.id.tv_movie_detail_user_rating_text);
        fabFavMovie = findViewById(R.id.fab_favoriteMovie);

        trailerRecyclerView = findViewById(R.id.rv_movieTrailers);
        reviewRecyclerView = findViewById(R.id.rv_movieReviews);

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
        getMovieReviews();

        checkMovieAlreadyFavorite();

        mListener = new OnItemClickListener() {
            @Override
            public void onItemClick(Movies movie) {

            }
        };

        trailerListener = new TrailerListener() {
            @Override
            public void onYoutubeLinkClick(String key) {
                Uri uri = Uri.parse("https://www.youtube.com/watch?v=" +key);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }

            @Override
            public void onShareLinkClick(String url) {
                String youtubeUrl = "https://www.youtube.com/watch?v=" + url;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT,"Watch this link!");
                shareIntent.putExtra(Intent.EXTRA_TEXT, youtubeUrl);
                startActivity(Intent.createChooser(shareIntent,"Share Link!"));
            }
        };

        fabFavMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("FAB", "button clicked");
                Toast.makeText(DetailsActivity.this, "fav button clicked", Toast.LENGTH_SHORT).show();
                new FavoriteMovieMarkUnmarkTask().execute();
            }
        });

    }

    @NonNull
    private ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieListEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieListEntry.COLUMN_MOVIE_TITLE,movie.getTitle());
        contentValues.put(MovieContract.MovieListEntry.COLUMN_MOVIE_OVERVIEW,movie.getOverview());
        contentValues.put(MovieContract.MovieListEntry.COLUMN_MOVIE_VOTE_AVERAGE,movie.getVoteAverage());
        contentValues.put(MovieContract.MovieListEntry.COLUMN_MOVIE_RELEASE_DATE,movie.getReleaseDate());
        contentValues.put(MovieContract.MovieListEntry.COLUMN_MOVIE_BACKDROP_PATH,movie.getBackdropPath());
        contentValues.put(MovieContract.MovieListEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        return contentValues;
    }

    private void checkMovieAlreadyFavorite() {
        new FavoriteMovieCheckerTask().execute();
    }

    private void getMovieReviews() {
        MovieApiInterface movieApiService = MovieApiClient.getClient().create(MovieApiInterface.class);
        Call<MovieReviewsResponse> movieReviewsResponseCall = movieApiService.getMovieReviews(movie.getId(),API_KEY);
        movieReviewsResponseCall.enqueue(new Callback<MovieReviewsResponse>() {
            @Override
            public void onResponse(Call<MovieReviewsResponse> call, Response<MovieReviewsResponse> response) {
                movieId = response.body().getId();
                List<ReviewResult> movieReviewResult = response.body().getReviewResult();
                movieReviewAdapter = new MovieReviewAdapter(getApplicationContext(),movieReviewResult, R.layout.recyclerview_trailers,mListener);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
                reviewRecyclerView.setLayoutManager(layoutManager);
                reviewRecyclerView.setHasFixedSize(true);
                reviewRecyclerView.setAdapter(movieReviewAdapter);
            }

            @Override
            public void onFailure(Call<MovieReviewsResponse> call, Throwable t) {

            }
        });
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
                trailerViewAdapter = new TrailerViewAdapter(getApplicationContext(),movieTrailerResult,trailerListener,R.layout.recyclerview_items);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                trailerRecyclerView.setLayoutManager(layoutManager);
                trailerRecyclerView.setHasFixedSize(true);
                trailerRecyclerView.setAdapter(trailerViewAdapter);

            }

            @Override
            public void onFailure(Call<MovieTrailerResponse> call, Throwable t) {

            }
        });
    }

    private class FavoriteMovieCheckerTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... params) {
            Cursor cursor = getApplicationContext().getContentResolver()
                    .query(
                            MovieContract.MovieListEntry.CONTENT_URI,
                            new String[]{MovieContract.MovieListEntry.COLUMN_MOVIE_ID},
                            MovieContract.MovieListEntry.COLUMN_MOVIE_ID + " = ?",
                            new String[]{String.valueOf(movie.getId())},
                            null
                    );
            boolean movieExists = cursor != null && cursor.getCount() == 1;
            if(cursor!=null){
                cursor.close();
            }
            return movieExists;
        }

        @Override
        protected void onPostExecute(Boolean movieExists) {
            super.onPostExecute(movieExists);
            if(movieExists){ fabFavMovie.setImageResource(R.drawable.ic_action_name);}
            else{ fabFavMovie.setImageResource(R.drawable.ic_remove_favorite);}
            isMovieFavorite = movieExists;
        }
    }

    private class FavoriteMovieMarkUnmarkTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean isMarked;
            if(isMovieFavorite){
                isMarked = getApplicationContext().getContentResolver()
                        .delete(MovieContract.MovieListEntry.CONTENT_URI,
                                MovieContract.MovieListEntry.COLUMN_MOVIE_ID + " = ?",
                                new String[]{String.valueOf(movie.getId())}) == 1;
            }
            else{
                isMarked = getApplicationContext().getContentResolver()
            .insert(MovieContract.MovieListEntry.CONTENT_URI,getContentValues()) !=null;
            }
            return isMarked;
        }

        @Override
        protected void onPostExecute(Boolean isMarked) {
            super.onPostExecute(isMarked);

            if(!isMarked){
                Toast.makeText(DetailsActivity.this, "Could not perform the operation!", Toast.LENGTH_SHORT).show();
                return;
            }
            isMovieFavorite = !isMovieFavorite;
            if(!isMovieFavorite){
                fabFavMovie.setImageResource(R.drawable.ic_remove_favorite);
                Toast.makeText(DetailsActivity.this, "Removed movie from favorites", Toast.LENGTH_SHORT).show();
            }
            else{
                fabFavMovie.setImageResource(R.drawable.ic_action_name);
                Toast.makeText(DetailsActivity.this, "Marked movie as favorite", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
