package com.example.ichin.popularmoviestageone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ichin.popularmoviestageone.model.Movies;
import com.squareup.picasso.Picasso;

public class MoviePopupWindow extends AppCompatActivity {

    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private static final String IMAGE_BASE_URL="http://image.tmdb.org/t/p/w185";
    private Movies movie;
    private TextView movieInfo;
    private TextView popTitle;
    private ImageView popupPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);

        setLayoutMetrics();
        popupPoster = findViewById(R.id.popupPoster);

        movieInfo = findViewById(R.id.tv_movieInfo);
        popTitle= findViewById(R.id.tv_pop_title);
        movie = getIntent().getParcelableExtra(MainActivity.PROP_MOVIES);

        movieInfo.setText(movie.getOverview());
        popTitle.setText(movie.getTitle());

        int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
        String imageUrl = IMAGE_BASE_URL + movie.getPosterPath();
        Picasso.get().load(imageUrl).resize(size,size).into(popupPoster);



    }

    private void setLayoutMetrics() {
        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.7), (int)(height*0.7));
    }
}
