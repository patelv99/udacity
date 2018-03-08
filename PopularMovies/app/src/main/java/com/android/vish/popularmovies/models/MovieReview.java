package com.android.vish.popularmovies.models;

import java.io.Serializable;

public class MovieReview implements Serializable {

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    public String getId() {
        return mId;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mContent;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public void setContent(String content) {
        this.mContent = content;
    }


    public void setUrl(String url) {
        this.mUrl = url;
    }
}
