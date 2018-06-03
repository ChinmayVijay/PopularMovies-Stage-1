package com.example.ichin.popularmoviestageone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ichin.popularmoviestageone.model.Movies;
import com.example.ichin.popularmoviestageone.utilities.Utils;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

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
        String imageUrl = Utils.IMAGE_BASE_URL + movie.getPosterPath();
        Log.d(TAG, imageUrl);

        int size = (int) Math.ceil(Math.sqrt(Utils.MAX_WIDTH * Utils.MAX_HEIGHT));
        Picasso.with(DetailsActivity.this).load(imageUrl).resize(size,size).into(posterImage);


    }
}
