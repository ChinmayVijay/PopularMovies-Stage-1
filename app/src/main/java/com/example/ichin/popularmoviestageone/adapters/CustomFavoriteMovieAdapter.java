package com.example.ichin.popularmoviestageone.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ichin.popularmoviestageone.R;
import com.example.ichin.popularmoviestageone.data.MovieContract;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.utilities.Utils;
import com.squareup.picasso.Picasso;

public class CustomFavoriteMovieAdapter extends RecyclerView.Adapter<CustomFavoriteMovieAdapter.MovieFavoriteViewHolder> {

    private Cursor cursor;
    private Context mContext;
    private OnItemClickListener mListener;

    public CustomFavoriteMovieAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public MovieFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_items,parent,false);
        return new MovieFavoriteViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieFavoriteViewHolder holder, int position) {

        cursor.moveToPosition(position);
        Movies favMovie = new Movies();
        favMovie.setId(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieListEntry.COLUMN_MOVIE_ID)));
        favMovie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieListEntry.COLUMN_MOVIE_TITLE)));
        favMovie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieListEntry.COLUMN_MOVIE_OVERVIEW)));
        favMovie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieListEntry.COLUMN_MOVIE_VOTE_AVERAGE)));
        favMovie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieListEntry.COLUMN_MOVIE_RELEASE_DATE)));
        favMovie.setBackdropPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieListEntry.COLUMN_MOVIE_BACKDROP_PATH)));
        favMovie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieListEntry.COLUMN_MOVIE_POSTER_PATH)));
        holder.bind(favMovie,mListener);

    }

    @Override
    public int getItemCount() {
        if(cursor == null){
            return 0;
        }
        return cursor.getCount();
    }

    public class MovieFavoriteViewHolder extends RecyclerView.ViewHolder{
        TextView movieTitle;
        ImageView moviePoster;


        MovieFavoriteViewHolder(View itemView) {
            super(itemView);
            this.movieTitle = itemView.findViewById(R.id.tv_movieTitle);
            this.moviePoster = itemView.findViewById(R.id.iv_movieImage);


        }

        void bind(final Movies movie, final OnItemClickListener listener){

            movieTitle.setText(movie.getTitle());
            String imageUrl = Utils.IMAGE_BASE_URL + movie.getPosterPath();
            int size = (int) Math.ceil(Math.sqrt(Utils.MAX_WIDTH * Utils.MAX_HEIGHT));
            Picasso.get().load(imageUrl).resize(size,size).into(moviePoster);
            moviePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });



        }
    }

    public Cursor swapCursor(Cursor c){
        if(cursor == c){
            return null;
        }
        Cursor temp = cursor;
        this.cursor = c;
        if(c != null){
            this.notifyDataSetChanged();
        }
        return temp;
    }

}
