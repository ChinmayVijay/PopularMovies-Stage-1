package com.example.ichin.popularmoviestageone;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ichin.popularmoviestageone.adapters.CustomFavoriteMovieAdapter;
import com.example.ichin.popularmoviestageone.adapters.MovieViewAdapter;
import com.example.ichin.popularmoviestageone.data.MovieContract;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.model.MoviesResponse;
import com.example.ichin.popularmoviestageone.rest.MovieApiClient;
import com.example.ichin.popularmoviestageone.rest.MovieApiInterface;
import com.example.ichin.popularmoviestageone.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String PARAM_SORT="popularity.desc";
    public static final String PROP_MOVIES = "Movies";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int MOVIE_LOADER_ID = 0;
    private RecyclerView movieRecyclerView;
    private MovieViewAdapter movieAdapter;
    private CustomFavoriteMovieAdapter customFavoriteMovieAdapter;
    private OnItemClickListener listener;
    private FloatingActionButton fabSortOptions;
    private boolean isPopularAlready = false;
    private boolean isTopRatedAlready = false;
    private RelativeLayout layout_error;
    private RelativeLayout layout_original;
    private ArrayList<Movies> movies;
    private boolean isFavoriteMovieViewAlready = true;
    private Handler handler;
    private ContentObserver contentObserver;
    private int favMovieCountInitial;
    private TextView errorMsg;


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
        errorMsg = findViewById(R.id.tv_errorMessage);

        customFavoriteMovieAdapter = new CustomFavoriteMovieAdapter(getApplicationContext(),listener);

        listener = new OnItemClickListener() {
            @Override
            public void onItemClick(Movies movie) {
                Intent detailsIntent = new Intent(MainActivity.this,DetailsActivity.class);
                detailsIntent.putExtra(PROP_MOVIES,movie);
                startActivity(detailsIntent);
            }
        };

        if(savedInstanceState!=null){
            isFavoriteMovieViewAlready = savedInstanceState.getBoolean("favorite");
            isPopularAlready = savedInstanceState.getBoolean("popular");
            isTopRatedAlready = savedInstanceState.getBoolean("topRated");
            favMovieCountInitial = savedInstanceState.getInt("favMovCount");
        }
        if(isFavoriteMovieViewAlready){
            getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
            getFavoriteMovies();
        }

        if(isPopularAlready){
            fetchPopularMovies();
        }

        if(isTopRatedAlready){
            fetchTopRatedMovies();
        }

    }

    private void fetchFavoriteMovies() {
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
        getFavoriteMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"After click on back button");
        getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
        if(isFavoriteMovieViewAlready){
            getFavoriteMovies();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("popular",isPopularAlready);
        outState.putBoolean("favorite",isFavoriteMovieViewAlready);
        outState.putBoolean("topRated",isTopRatedAlready);
        outState.putInt("favMovCount",favMovieCountInitial);
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
        isFavoriteMovieViewAlready = false;
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
        isFavoriteMovieViewAlready = false;


    }

    private void getFavoriteMovies(){
        if(favMovieCountInitial > 0){
            Toast.makeText(MainActivity.this, "Showing Favorite Movies", Toast.LENGTH_SHORT).show();
            layout_original.setVisibility(View.VISIBLE);
            layout_error.setVisibility(View.GONE);
            fabSortOptions.setVisibility(View.VISIBLE);
            errorMsg.setVisibility(View.VISIBLE);
            customFavoriteMovieAdapter = new CustomFavoriteMovieAdapter(getApplicationContext(),listener);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
            movieRecyclerView.setHasFixedSize(true);
            movieRecyclerView.setLayoutManager(layoutManager);
            movieRecyclerView.setAdapter(customFavoriteMovieAdapter);

            isTopRatedAlready = false;
            isPopularAlready = false;
            isFavoriteMovieViewAlready = true;
        }
        else{
            layout_original.setVisibility(View.GONE);
            layout_error.setVisibility(View.VISIBLE);
            errorMsg.setVisibility(View.VISIBLE);
            errorMsg.setText(getResources().getString(R.string.no_fav_mov_found));
            isFavoriteMovieViewAlready = true;


        }


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
                    case 2:

                        //show favorite movies
                        if(!isFavoriteMovieViewAlready) fetchFavoriteMovies();
                        dialog.cancel();
                    default:
                        dialog.cancel();
                        break;
                }
            }
        });
        AlertDialog sortDialog = dialogBuilder.create();
        sortDialog.show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, @NonNull Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if(mMovieData!=null) {deliverResult(mMovieData);}
                else{ forceLoad();}
            }

            @Override
            public Cursor loadInBackground() {
                try{
                    Cursor temp =
                     getContentResolver().query(MovieContract.MovieListEntry.CONTENT_URI,
                            new String[]{
                                    MovieContract.MovieListEntry.COLUMN_MOVIE_ID,
                                    MovieContract.MovieListEntry.COLUMN_MOVIE_TITLE,
                                    MovieContract.MovieListEntry.COLUMN_MOVIE_OVERVIEW,
                                    MovieContract.MovieListEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                                    MovieContract.MovieListEntry.COLUMN_MOVIE_RELEASE_DATE,
                                    MovieContract.MovieListEntry.COLUMN_MOVIE_BACKDROP_PATH,
                                    MovieContract.MovieListEntry.COLUMN_MOVIE_POSTER_PATH
                            },
                            null,
                            null,
                            null);
                    if(temp!=null){ favMovieCountInitial = temp.getCount();}
                    return temp;
                }
                catch (Exception e){
                    Log.e(TAG, "Failed to load Data");
                    e.printStackTrace();
                    return null;
                }

            }

            @Override
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        customFavoriteMovieAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        customFavoriteMovieAdapter.swapCursor(null);
    }

}
