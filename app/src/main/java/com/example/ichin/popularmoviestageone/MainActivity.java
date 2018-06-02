package com.example.ichin.popularmoviestageone;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.ichin.popularmoviestageone.adapters.MovieViewAdapter;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.model.MoviesResponse;
import com.example.ichin.popularmoviestageone.rest.MovieApiClient;
import com.example.ichin.popularmoviestageone.rest.MovieApiInterface;
import com.example.ichin.popularmoviestageone.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClickListener{
    RecyclerView movieRecyclerView;

    MovieViewAdapter movieAdapter;

    private static final String API_KEY = "5af869fcf0fa026d1847f32746a1f5c2";
    private static final String PARAM_SORT="popularity.desc";
    private static final String TAG = MainActivity.class.getSimpleName();
    OnItemClickListener listener;
    public static final String PROP_MOVIES = "Movies";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieRecyclerView = findViewById(R.id.rv_moviePosters);
//        movieAdapter = new MovieViewAdapter(,R.layout.recyclerview_items,this);
//
//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
//        movieRecyclerView.setLayoutManager(layoutManager);
//        movieRecyclerView.setAdapter(movieAdapter);

//        new GetMoviesTask().execute(NetworkUtils.buildUrl());
        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(Movies movie) {
                Toast.makeText(MainActivity.this, "you have chosen "+movie.getTitle(), Toast.LENGTH_SHORT).show();
                Intent detailsIntent = new Intent(MainActivity.this,DetailsActivity.class);
                detailsIntent.putExtra(PROP_MOVIES,movie);
                startActivity(detailsIntent);
            }
        };
        MovieApiInterface movieApiService = MovieApiClient.getClient()
                .create(MovieApiInterface.class);

        Call<MoviesResponse> moviesResponseCall = movieApiService.getPopularMovies(PARAM_SORT,API_KEY);
        moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movies> allMovies = response.body().getResults();
                movieAdapter = new MovieViewAdapter(allMovies,R.layout.recyclerview_items,getApplicationContext(),listener);

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                movieRecyclerView.setHasFixedSize(true);
                movieRecyclerView.setLayoutManager(layoutManager);
                movieRecyclerView.setAdapter(movieAdapter);
                Log.d(TAG,"total movies fetched"+allMovies.size());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

            }
        });

    }

    @Override
    public void onItemClick(Movies movie) {
    }

    //        @Override
    //        protected String doInBackground(URL... urls) {
    //            URL movieUrl = urls[0];
    //            String movieResultsJson= null;
    //            try {
    //                movieResultsJson = NetworkUtils.getResponseFromURL(movieUrl);
    //
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //            }
    //            return movieResultsJson;
    //        }
    //
    //        @Override
    //        protected void onPreExecute() {
    //            super.onPreExecute();
    //        }
    //
    //        @Override
    //        protected void onPostExecute(String movieResultsJson) {
//    public class GetMoviesTask extends AsyncTask<URL,Void,String>{
//            super.onPostExecute(movieResultsJson);
//            Log.d("response url", movieResultsJson);
//        }
//    }
}
