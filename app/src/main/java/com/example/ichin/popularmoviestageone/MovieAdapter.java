package com.example.ichin.popularmoviestageone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.example.ichin.popularmoviestageone.model.Movies;

public class MovieAdapter extends ArrayAdapter<Movies> {

    public MovieAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
