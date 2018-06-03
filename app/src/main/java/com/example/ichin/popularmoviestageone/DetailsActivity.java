package com.example.ichin.popularmoviestageone;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL=" http://image.tmdb.org/t/p/w185";
    private static final String TAG = DetailsActivity.class.getSimpleName();
    ImageView posterImage;
    TextView movieTitle;
    Movies movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        movieTitle = findViewById(R.id.tv_movie_title);
        posterImage = findViewById(R.id.iv_poster);

        movie = getIntent().getParcelableExtra(MainActivity.PROP_MOVIES);

        movieTitle.setText(movie.getTitle());

        Picasso.with(this).setLoggingEnabled(true);
        String imageUrl = IMAGE_BASE_URL + movie.getPosterPath();
        Log.d(TAG, imageUrl);
//        Uri myUri = Uri.parse(imageUrl);
//        Picasso.with(DetailsActivity.this).load(myUri).resize(300,60).into(posterImage);
        Picasso.with(DetailsActivity.this).load(imageUrl).resize(300,60).into(posterImage);

    }
}
