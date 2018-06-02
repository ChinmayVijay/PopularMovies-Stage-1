package com.example.ichin.popularmoviestageone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.ichin.popularmoviestageone.model.Movies;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private static final String IMAGE_BASE_URL=" http://image.tmdb.org/t/p/w185";
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
//        Picasso.with(this).load(imageUrl).resize(300,60).into(posterImage);
        Glide.with(this).load(imageUrl).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(posterImage);

    }
}
