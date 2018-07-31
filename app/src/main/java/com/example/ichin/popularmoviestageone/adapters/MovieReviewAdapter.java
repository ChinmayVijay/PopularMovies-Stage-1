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
import com.example.ichin.popularmoviestageone.model.ReviewResult;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewHolder> {
    private Context mContext;
    private List<ReviewResult> mReviewResults;
    private int mLayout;
    private OnItemClickListener mListener;

    public MovieReviewAdapter(Context mContext, List<ReviewResult> mReviewResults, int mLayout, OnItemClickListener mListener) {
        this.mContext = mContext;
        this.mReviewResults = mReviewResults;
        this.mLayout = mLayout;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MovieReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieReviewView = LayoutInflater.from(mContext).inflate(mLayout,parent,false);
        return new MovieReviewHolder(movieReviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewHolder holder, int position) {
        Log.d("Reviews"," "+ mReviewResults.size());
        holder.bind(mReviewResults.get(position));
    }

    @Override
    public int getItemCount() {
        return mReviewResults.size();
    }

    class MovieReviewHolder extends RecyclerView.ViewHolder{

        TextView reviewAuthor;
        TextView reviewContent;
        TextView reviewUrl;

        MovieReviewHolder(View itemView){
            super(itemView);
            this.reviewAuthor = itemView.findViewById(R.id.tv_movie_author_text);
            this.reviewContent = itemView.findViewById(R.id.tv_movie_content_text);
            this.reviewUrl = itemView.findViewById(R.id.tv_movie_url_text);
        }

        void bind(ReviewResult reviewResult){
            this.reviewAuthor.setText(reviewResult.getAuthor());
            this.reviewContent.setText(reviewResult.getContent());
            this.reviewUrl.setText(reviewResult.getUrl());
        }

    }
}
