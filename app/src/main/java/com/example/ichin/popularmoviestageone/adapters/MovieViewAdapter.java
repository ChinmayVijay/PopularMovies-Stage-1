package com.example.ichin.popularmoviestageone.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ichin.popularmoviestageone.R;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder> {

    private Context context;
    private List<Movies> moviesList;
    private int moviePosterLayout;
    private OnItemClickListener listener;

    public MovieViewAdapter(List<Movies> mList, int layout , Context context, OnItemClickListener listener) {
        this.context = context;
        this.moviesList = mList;
        this.moviePosterLayout = layout;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(moviePosterLayout,parent,false);
        return new MovieViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        holder.bind(moviesList.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        TextView movieTitle;
        ImageView moviePoster;

        MovieViewHolder(View itemView) {
            super(itemView);
            this.movieTitle = itemView.findViewById(R.id.tv_movieTitle);
            this.moviePoster = itemView.findViewById(R.id.iv_movieImage);

        }

        void bind(final Movies movie, final OnItemClickListener listener){
            movieTitle.setText(movie.getTitle());
            String imageUrl = Utils.IMAGE_BASE_URL + movie.getPosterPath();
            int size = (int) Math.ceil(Math.sqrt(Utils.MAX_WIDTH * Utils.MAX_HEIGHT));
            Picasso.get().load(imageUrl).resize(size,size).into(moviePoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movie);
                }
            });


        }
    }

}
