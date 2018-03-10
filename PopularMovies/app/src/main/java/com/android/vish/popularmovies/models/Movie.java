package com.android.vish.popularmovies.models;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Movie implements Serializable {

    public static final  String           MOVIE_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500/";
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT  = new SimpleDateFormat("MMM dd, yyyy");
    private static final SimpleDateFormat MOVIE_DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private int    mId;
    private String mImage;
    private String mTitle;
    private String mReleaseDate;
    private int    mAverageVote;
    private String mPlot;
    private int    mPopularity;

    public Movie() {
        // default constructor
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getImage() {
        return mImage;
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
        try {
            return DISPLAY_DATE_FORMAT.format(MOVIE_DB_DATE_FORMAT.parse(mReleaseDate));
        } catch (ParseException e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage());
            return mReleaseDate;
        }
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
