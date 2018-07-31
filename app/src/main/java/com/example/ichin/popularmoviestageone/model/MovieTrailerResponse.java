
package com.example.ichin.popularmoviestageone.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerResponse {

    @SerializedName("id")
    private Long mId;
    @SerializedName("results")
    private List<Result> mResults;

    public Long getId() {
        return mId;
    }

    public List<Result> getResults() {
        return mResults;
    }



}
