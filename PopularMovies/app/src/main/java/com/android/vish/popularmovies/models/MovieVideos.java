package com.android.vish.popularmovies.models;

import java.io.Serializable;

public class MovieVideos implements Serializable {

    private String    mId;
    private String mKey;
    private String mName;
    private String mSite;
    private String mType;

    public String getId() {
        return mId;
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

    public String getType() {
        return mType;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setSite(String site) {
        this.mSite = site;
    }

    public void setType(String type) {
        this.mType = type;
    }

}
