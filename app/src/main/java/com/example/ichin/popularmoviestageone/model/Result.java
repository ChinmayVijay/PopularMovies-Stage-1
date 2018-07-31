
package com.example.ichin.popularmoviestageone.model;

import com.google.gson.annotations.SerializedName;


public class Result {

    @SerializedName("id")
    private String mId;
    @SerializedName("iso_3166_1")
    private String mIso31661;
    @SerializedName("iso_639_1")
    private String mIso6391;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("site")
    private String mSite;
    @SerializedName("size")
    private Long mSize;
    @SerializedName("type")
    private String mType;

    public String getId() {
        return mId;
    }

    public String getIso31661() {
        return mIso31661;
    }

    public String getIso6391() {
        return mIso6391;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    public Long getSize() {
        return mSize;
    }

    public String getType() {
        return mType;
    }

}
