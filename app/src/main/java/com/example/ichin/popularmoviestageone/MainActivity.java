package com.example.ichin.popularmoviestageone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ichin.popularmoviestageone.adapters.MovieViewAdapter;

public class MainActivity extends AppCompatActivity {
    RecyclerView movieRecyclerView;
    MovieViewAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieRecyclerView = findViewById(R.id.rv_moviePosters);
        movieAdapter = new MovieViewAdapter(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        movieRecyclerView.setLayoutManager(layoutManager);
        movieRecyclerView.setAdapter(movieAdapter);


    }
}
