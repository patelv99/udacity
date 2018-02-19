package com.android.vish.popularmovies;

import java.io.Serializable;

public class Movie implements Serializable {

    private static final String MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500/";

    private int    mId;
    private String mImage;
    private String mTitle;
    private String mReleaseDate;
    private int    mAverageVote;
    private String mPlot;
    private int    mPopularity;

    public Movie() {

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getImage() {
        return MOVIE_IMAGE_BASE_URL + mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public int getAverageVote() {
        return mAverageVote;
    }

    public void setAverageVote(int averageVote) {
        this.mAverageVote = averageVote;
    }

    public String getPlot() {
        return mPlot;
    }

    public void setPlot(String plot) {
        this.mPlot = plot;
    }

    public int getPopularity() {
        return mPopularity;
    }

    public void setPopularity(int popularity) {
        this.mPopularity = popularity;
    }
}
