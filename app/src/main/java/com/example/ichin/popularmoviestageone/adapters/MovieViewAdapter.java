package com.example.ichin.popularmoviestageone.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ichin.popularmoviestageone.DetailsActivity;
import com.example.ichin.popularmoviestageone.MainActivity;
import com.example.ichin.popularmoviestageone.R;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.listener.PopupListener;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder> {

    private static final String IMAGE_BASE_URL="http://image.tmdb.org/t/p/w185";
    private Context context;
    private List<Movies> moviesList;
    private int moviePosterLayout;
    private OnItemClickListener listener;
    private PopupListener infoListener;

    public MovieViewAdapter(List<Movies> mList, int layout , Context context, OnItemClickListener listener, PopupListener infoListener) {
        this.context = context;
        this.moviesList = mList;
        this.moviePosterLayout = layout;
        this.listener = listener;
        this.infoListener = infoListener;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(moviePosterLayout,parent,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(movieView);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        holder.bind(moviesList.get(position),listener,infoListener);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView movieTitle;
        ImageView moviePoster;
        ImageView info;
        TextView releaseDate;

        public MovieViewHolder(View itemView) {
            super(itemView);
            this.movieTitle = itemView.findViewById(R.id.tv_movieTitle);
            this.moviePoster = itemView.findViewById(R.id.iv_movieImage);
            this.info = itemView.findViewById(R.id.iv_info);
//            this.releaseDate = itemView.findViewById(R.id.tv_release);

        }

        public void bind(final Movies movie,final OnItemClickListener listener, final PopupListener popupListener ){
            movieTitle.setText(movie.getTitle());
            String imageUrl = Utils.IMAGE_BASE_URL + movie.getPosterPath();
//            Picasso.with(context).load(imageUrl).fit().centerCrop().into(moviePoster);
            int size = (int) Math.ceil(Math.sqrt(Utils.MAX_WIDTH * Utils.MAX_HEIGHT));
            Picasso.with(context).load(imageUrl).resize(size,size).into(moviePoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Yes Clicking works", Toast.LENGTH_SHORT).show();
                    listener.onItemClick(movie);
                }
            });

//            releaseDate.setText(movie.getReleaseDate());
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupListener.onInfoClick(movie);
                }
            }) ;


        }
    }

}
