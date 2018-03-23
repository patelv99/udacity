package com.android.vish.bakenow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RecipeStep implements Serializable {

    @SerializedName("id") private               int    mId;
    @SerializedName("shortDescription") private String mShortDescription;
    @SerializedName("description") private      String mDescription;
    @SerializedName("videoURL") private         String mVideoUrl;
    @SerializedName("thumbnailURL") private     String mThumbnailUrl;

    public int getId() {
        return mId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }
}