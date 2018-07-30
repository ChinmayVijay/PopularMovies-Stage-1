package com.example.ichin.popularmoviestageone.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ichin.popularmoviestageone.R;
import com.example.ichin.popularmoviestageone.listener.OnItemClickListener;
import com.example.ichin.popularmoviestageone.model.Result;

import java.util.List;

public class TrailerViewAdapter extends RecyclerView.Adapter<TrailerViewAdapter.MovieTrailerViewHolder> {

    private Context mContext;
    private List<Result> mResults;
    private OnItemClickListener mListener;
    private int mLayout;

    public TrailerViewAdapter(Context mContext, List<Result> mResults, OnItemClickListener mListener, int mLayout) {
        this.mContext = mContext;
        this.mResults = mResults;
        this.mListener = mListener;
        this.mLayout = mLayout;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieTrailerView = LayoutInflater.from(mContext).inflate(mLayout,parent,false);
        return new MovieTrailerViewHolder(movieTrailerView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder holder, int position) {
        holder.bind(mResults.get(position),mListener);
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder{
        TextView trailerNum;
        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            this.trailerNum = itemView.findViewById(R.id.tv_trailerNum);
        }

        void bind(final Result movieResult, OnItemClickListener listener){
            Log.d("Trailers", movieResult.getName());
            trailerNum.setText(movieResult.getName());

        }
    }
}
