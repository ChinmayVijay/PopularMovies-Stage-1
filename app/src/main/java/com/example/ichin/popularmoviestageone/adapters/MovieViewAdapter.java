package com.example.ichin.popularmoviestageone.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ichin.popularmoviestageone.R;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder> {

    private static final String IMAGE_BASE_URL=" http://image.tmdb.org/t/p/w185";
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
        MovieViewHolder movieViewHolder = new MovieViewHolder(movieView);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        holder.bind(moviesList.get(position),listener);
//        holder.movieTitle.setText(moviesList.get(position).getTitle());
////        Picasso.with(this).load("http://cdn.journaldev.com/wp-content/uploads/2017/01/android-constraint-layout-sdk-tool-install.png").placeholder(R.drawable.placeholder).into(imageView);
//        String imageUrl = IMAGE_BASE_URL + moviesList.get(position).getPosterPath();
//        Log.d("Poster",imageUrl);
//        new DownloadImageTask(holder.moviePoster).execute(imageUrl);
////        Picasso.with(context).load(imageUrl).fit().centerCrop().into(holder.moviePoster);
////        Picasso.with(context).load(imageUrl).into(holder.moviePoster);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        public TextView movieTitle;
        public ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            this.movieTitle = itemView.findViewById(R.id.tv_movieTitle);
            this.moviePoster = itemView.findViewById(R.id.iv_movieImage);
        }

        public void bind(final Movies movie,final OnItemClickListener listener ){
            movieTitle.setText(movie.getTitle());
            String imageUrl = IMAGE_BASE_URL + movie.getPosterPath();
            new DownloadImageTask(moviePoster).execute(imageUrl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Yes Clicking works", Toast.LENGTH_SHORT).show();
                    listener.onItemClick(movie);
                }
            });
        }
    }

    private class DownloadImageTask extends AsyncTask<String,Void,Bitmap>{

        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlDisplay = strings[0];
            Bitmap moviePoster = null;

            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                moviePoster = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return moviePoster;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
