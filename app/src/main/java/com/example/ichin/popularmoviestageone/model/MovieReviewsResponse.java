
package com.example.ichin.popularmoviestageone.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieReviewsResponse {

    @SerializedName("id")
    private Long mId;
    @SerializedName("page")
    private Long mPage;
    @SerializedName("results")
    private List<ReviewResult> mReviewResult;
    @SerializedName("total_pages")
    private Long mTotalPages;
    @SerializedName("total_results")
    private Long mTotalResults;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getPage() {
        return mPage;
    }

    public void setPage(Long page) {
        mPage = page;
    }

    public List<ReviewResult> getReviewResult() {
        return mReviewResult;
    }

    public void setReviewResult(List<ReviewResult> reviewResult) {
        mReviewResult = reviewResult;
    }

    public Long getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(Long totalPages) {
        mTotalPages = totalPages;
    }

    public Long getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Long totalResults) {
        mTotalResults = totalResults;
    }

}
