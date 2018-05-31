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

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder> {

    Context context;

    public MovieViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.recyclerview_items,parent,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(movieView);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.movieTitle.setText("Infinity Wars");
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public TextView movieTitle;
        public ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            this.movieTitle = itemView.findViewById(R.id.tv_movieTitle);
            this.moviePoster = itemView.findViewById(R.id.iv_movieImage);
        }
    }
}
