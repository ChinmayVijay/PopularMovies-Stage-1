package com.example.ichin.popularmoviestageone;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ichin.popularmoviestageone.adapters.MovieViewAdapter;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.model.MoviesResponse;
import com.example.ichin.popularmoviestageone.rest.MovieApiClient;
import com.example.ichin.popularmoviestageone.rest.MovieApiInterface;
import com.example.ichin.popularmoviestageone.utilities.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
    private static final String PARAM_SORT="popularity.desc";
    public static final String PROP_MOVIES = "Movies";
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView movieRecyclerView;
    private MovieViewAdapter movieAdapter;
    private OnItemClickListener listener;
    private FloatingActionButton fabSortOptions;
    private boolean isPopularAlready = false;
    private boolean isTopRatedAlready = false;
    private RelativeLayout layout_error;
    private RelativeLayout layout_original;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieRecyclerView = findViewById(R.id.rv_moviePosters);

        fabSortOptions = findViewById(R.id.fabSort);
        fabSortOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });

        layout_original = findViewById(R.id.rl_original);
        layout_error = findViewById(R.id.rl_error);

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(Movies movie) {
                Intent detailsIntent = new Intent(MainActivity.this,DetailsActivity.class);
                detailsIntent.putExtra(PROP_MOVIES,movie);
                startActivity(detailsIntent);
            }

            @Override
            public void onYoutubeItemClick(String key) {

            }
        };

        fetchPopularMovies();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.refresh_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.refresh_action:
                if(isTopRatedAlready) fetchTopRatedMovies();
                else if(isPopularAlready) fetchPopularMovies();
                break;
        }
        return true;
    }

    private void fetchPopularMovies() {
        layout_original.setVisibility(View.VISIBLE);
        layout_error.setVisibility(View.GONE);
        fabSortOptions.setVisibility(View.VISIBLE);
        MovieApiInterface movieApiService = getMovieApiInterface();

        Call<MoviesResponse> moviesResponseCall = movieApiService.getPopularMovies(PARAM_SORT, Utils.API_KEY);
        moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movies> allMovies = response.body().getResults();
                movieAdapter = new MovieViewAdapter(allMovies, R.layout.recyclerview_items,getApplicationContext(),listener);
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                movieRecyclerView.setHasFixedSize(true);
                movieRecyclerView.setLayoutManager(layoutManager);
                movieRecyclerView.setAdapter(movieAdapter);
                Log.d(TAG,"total movies fetched"+allMovies.size());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                layout_original.setVisibility(View.GONE);
                layout_error.setVisibility(View.VISIBLE);
            }
        });

        isPopularAlready = true;
        isTopRatedAlready = false;
    }

    private MovieApiInterface getMovieApiInterface() {
        return MovieApiClient.getClient()
                    .create(MovieApiInterface.class);
    }

    private void fetchTopRatedMovies(){
        layout_original.setVisibility(View.VISIBLE);
        layout_error.setVisibility(View.GONE);
        fabSortOptions.setVisibility(View.VISIBLE);
        MovieApiInterface movieApiService = getMovieApiInterface();

        Call<MoviesResponse> moviesResponseCall = movieApiService.getTopRatedMovies(Utils.API_KEY);
        moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                List<Movies> allMovies = response.body().getResults();
                movieAdapter = new MovieViewAdapter(allMovies, R.layout.recyclerview_items,getApplicationContext(),listener);

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
                movieRecyclerView.setHasFixedSize(true);
                movieRecyclerView.setLayoutManager(layoutManager);
                movieRecyclerView.setAdapter(movieAdapter);
                Log.d(TAG,"total movies fetched"+allMovies.size());

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                fabSortOptions.setVisibility(View.GONE);
                layout_original.setVisibility(View.GONE);
                layout_error.setVisibility(View.VISIBLE);
            }
        });
        isTopRatedAlready = true;
        isPopularAlready = false;


    }

    private void showSortDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Sort Movies By");
        dialogBuilder.setItems(getResources().getStringArray(R.array.sort_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Toast.makeText(MainActivity.this, "fetching popular movies", Toast.LENGTH_SHORT).show();
                        if(!isPopularAlready) fetchPopularMovies();

                        dialog.cancel();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "fetching top rated movies", Toast.LENGTH_SHORT).show();
                        if(!isTopRatedAlready) fetchTopRatedMovies();
                        dialog.cancel();
                        break;
                    default:
                        dialog.cancel();
                        break;
                }
            }
        });
        AlertDialog sortDialog = dialogBuilder.create();
        sortDialog.show();

    }

}
