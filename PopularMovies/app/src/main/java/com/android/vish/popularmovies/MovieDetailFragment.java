package com.android.vish.popularmovies;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vish.popularmovies.adapters.MovieReviewsAdapter;
import com.android.vish.popularmovies.adapters.MovieTrailersAdapter;
import com.android.vish.popularmovies.models.Movie;
import com.android.vish.popularmovies.models.MovieReview;
import com.android.vish.popularmovies.models.MovieVideos;
import com.android.vish.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment {

    public static final String MOVIE_KEY = "movieKey";

    private ImageView    mPoster;
    private TextView     mTitle;
    private TextView     mReleaseDate;
    private TextView     mAverageVote;
    private TextView     mPlot;
    private TextView     mTrailersTitle;
    private RecyclerView mTrailersView;
    private TextView     mReviewsTitle;
    private RecyclerView mReviewsView;

    private ProgressDialog    mProgressDialog;
    private Movie             mMovie;
    private List<MovieReview> mReviews;
    private List<MovieVideos> mTrailers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mPoster = view.findViewById(R.id.movie_detail_poster);
        mTitle = view.findViewById(R.id.movie_detail_title);
        mReleaseDate = view.findViewById(R.id.movie_detail_release_date);
        mAverageVote = view.findViewById(R.id.movie_detail_vote_average);
        mPlot = view.findViewById(R.id.movie_detail_plot);
        mTrailersTitle = view.findViewById(R.id.movie_detail_trailers_title);
        mTrailersView = view.findViewById(R.id.movie_detail_trailers);
        mReviewsTitle = view.findViewById(R.id.movie_detail_reviews_title);
        mReviewsView = view.findViewById(R.id.movie_detail_reviews);

        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable(MOVIE_KEY);
            Picasso.with(getActivity()).load(mMovie.getImage()).into(mPoster);
            mTitle.setText(mMovie.getTitle());
            mReleaseDate.setText(getString(R.string.release_date, mMovie.getReleaseDate()));
            mAverageVote.setText(getString(R.string.rating, mMovie.getAverageVote()));
            mPlot.setText(mMovie.getPlot());
            getActivity().setTitle(mMovie.getTitle());
            new GetMovieReviewsTask().execute(NetworkUtils.buildMovieIdUrl(mMovie.getId(), NetworkUtils.REVIEWS_ENDPOINT));
            new GetMovieTrailersTask().execute(NetworkUtils.buildMovieIdUrl(mMovie.getId(), NetworkUtils.VIDEOS_ENDPOINT));
        } else {
            getActivity().setTitle("");
        }
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.spinner).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }


    /**
     * Show the progress dialog spinner
     */
    protected void showProgressDialog() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(getLayoutInflater().inflate(R.layout.progress_spinner, null));
        }
    }

    /**
     * Dismiss the progress dialog spinner
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Perform async call to fetch movie trailers
     */
    public class GetMovieTrailersTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL movieReviewsResponseUrl = params[0];
            String movieReviewsResponse = null;
            try {
                movieReviewsResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsResponseUrl);
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
            return movieReviewsResponse;
        }

        @Override
        protected void onPostExecute(String movieReviewsResult) {
            dismissProgressDialog();
            if (!TextUtils.isEmpty(movieReviewsResult)) {
                mTrailers = new ArrayList<>(NetworkUtils.parseMovieVideosJson(movieReviewsResult));
                if (!mTrailers.isEmpty()) {
                    mTrailersTitle.setVisibility(View.VISIBLE);
                    mTrailersView.setVisibility(View.VISIBLE);
                    mTrailersView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    mTrailersView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                    ViewCompat.setNestedScrollingEnabled(mReviewsView, false);
                    mTrailersView.setAdapter(new MovieTrailersAdapter(getContext(), mTrailers));
                } else {
                    mTrailersTitle.setVisibility(View.GONE);
                    mTrailersView.setVisibility(View.GONE);
                }
            } else {
                Snackbar.make(mTitle, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Perform async call to fetch movie reviews
     */
    public class GetMovieReviewsTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL movieReviewsResponseUrl = params[0];
            String movieReviewsResponse = null;
            try {
                movieReviewsResponse = NetworkUtils.getResponseFromHttpUrl(movieReviewsResponseUrl);
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
            return movieReviewsResponse;
        }

        @Override
        protected void onPostExecute(String movieReviewsResult) {
            dismissProgressDialog();
            if (!TextUtils.isEmpty(movieReviewsResult)) {
                mReviews = new ArrayList<>(NetworkUtils.parseMovieReviewsJson(movieReviewsResult));
                if (!mReviews.isEmpty()) {
                    mReviewsTitle.setVisibility(View.VISIBLE);
                    mReviewsView.setVisibility(View.VISIBLE);
                    mReviewsView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    mReviewsView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                    ViewCompat.setNestedScrollingEnabled(mReviewsView, false);
                    mReviewsView.setAdapter(new MovieReviewsAdapter(getContext(), mReviews));
                } else {
                    mReviewsTitle.setVisibility(View.GONE);
                    mReviewsView.setVisibility(View.GONE);
                }
            } else {
                Snackbar.make(mTitle, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
