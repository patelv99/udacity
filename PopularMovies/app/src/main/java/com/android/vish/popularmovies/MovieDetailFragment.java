package com.android.vish.popularmovies;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vish.popularmovies.adapters.MovieReviewsAdapter;
import com.android.vish.popularmovies.adapters.MovieTrailersAdapter;
import com.android.vish.popularmovies.data.MovieContract;
import com.android.vish.popularmovies.models.Movie;
import com.android.vish.popularmovies.models.MovieReview;
import com.android.vish.popularmovies.models.MovieVideo;
import com.android.vish.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieDetailFragment extends Fragment implements MovieTrailersAdapter.TrailerClickListener {

    public static final String MOVIE_KEY          = "movieKey";
    public static final String YOUTUBE_PACKAGE_ID = "vnd.youtube:";
    public static final String BASE_YOUTUBE_LINK  = "http://www.youtube.com/watch?v=";

    private ImageView    mPoster;
    private TextView     mTitle;
    private TextView     mReleaseDate;
    private TextView     mAverageVote;
    private TextView     mPlot;
    private TextView     mFavorite;
    private TextView     mTrailersTitle;
    private RecyclerView mTrailersView;
    private TextView     mReviewsTitle;
    private RecyclerView mReviewsView;

    private ProgressDialog       mProgressDialog;
    private MovieTrailersAdapter mMovieTrailersAdapter;
    private Movie                mMovie;
    private List<MovieVideo> mTrailers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mPoster = view.findViewById(R.id.movie_detail_poster);
        mTitle = view.findViewById(R.id.movie_detail_title);
        mReleaseDate = view.findViewById(R.id.movie_detail_release_date);
        mAverageVote = view.findViewById(R.id.movie_detail_vote_average);
        mPlot = view.findViewById(R.id.movie_detail_plot);
        mFavorite = view.findViewById(R.id.movie_detail_favorite_text);
        mTrailersTitle = view.findViewById(R.id.movie_detail_trailers_title);
        mTrailersView = view.findViewById(R.id.movie_detail_trailers);
        mReviewsTitle = view.findViewById(R.id.movie_detail_reviews_title);
        mReviewsView = view.findViewById(R.id.movie_detail_reviews);
        mMovieTrailersAdapter = new MovieTrailersAdapter(getContext(), mTrailers, this);
        mTrailersView.setAdapter(mMovieTrailersAdapter);

        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable(MOVIE_KEY);
            Picasso.with(getActivity()).load((Movie.MOVIE_IMAGE_BASE_URL + mMovie.getImage())).into(mPoster);
            mTitle.setText(mMovie.getTitle());
            mReleaseDate.setText(getString(R.string.release_date, mMovie.getReleaseDate()));
            mAverageVote.setText(getString(R.string.rating, mMovie.getAverageVote()));
            mPlot.setText(mMovie.getPlot());
            getActivity().setTitle(mMovie.getTitle());
            new GetMovieReviewsTask().execute(NetworkUtils.buildMovieIdUrl(mMovie.getId(), NetworkUtils.REVIEWS_ENDPOINT));
            new GetMovieTrailersTask().execute(NetworkUtils.buildMovieIdUrl(mMovie.getId(), NetworkUtils.VIDEOS_ENDPOINT));
            new GetFavoriteMovieTask().execute(MovieContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(mMovie.getId())).build());
        } else {
            getActivity().setTitle("");
        }

        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                if (view.isSelected()) {
                    saveFavoriteMovie();
                } else {
                    removeFavoriteMovie();
                }
            }
        });

        return view;
    }

    /**
     * Open the YouTube app to play the selected trailer video
     * If YouTube not found, play in a Web View
     *
     * @param trailerIndex position of the {@link MovieVideo} in the list
     */
    @Override
    public void onTrailerClick(int trailerIndex) {
        MovieVideo trailer = mTrailers.get(trailerIndex);
        try {
            Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_PACKAGE_ID + trailer.getKey()));
            getActivity().startActivity(youTubeIntent);
        } catch (ActivityNotFoundException exception) {
            Intent webViewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_YOUTUBE_LINK + trailer.getKey()));
            getActivity().startActivity(webViewIntent);
        }
    }

    /**
     * Save the {@link Movie} in the local database
     */
    public void saveFavoriteMovie() {
        ContentValues values = new ContentValues();
        values.put(MovieContract.FavoriteEntry._ID, mMovie.getId());
        values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE, mMovie.getTitle());
        values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER, mMovie.getImage());
        values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.getReleaseDate());
        values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_RATING, mMovie.getAverageVote());
        values.put(MovieContract.FavoriteEntry.COLUMN_MOVIE_PLOT, mMovie.getPlot());
        Uri uri = getActivity().getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, values);
        if (uri != null) {
            Snackbar.make(mTitle, getString(R.string.added_to_favorites, mMovie.getTitle()), Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Remove the {@link Movie} from the local database
     */
    public void removeFavoriteMovie() {
        Uri uri = MovieContract.FavoriteEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(mMovie.getId())).build();
        int moviesDeleted = getActivity().getContentResolver().delete(uri, null, null);
        if (moviesDeleted > -1) {
            Snackbar.make(mTitle, getString(R.string.removed_from_favorites, mMovie.getTitle()), Snackbar.LENGTH_SHORT).show();
        }
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
                    mMovieTrailersAdapter.updateTrailersList(mTrailers);
                    mTrailersView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    mTrailersView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                    ViewCompat.setNestedScrollingEnabled(mTrailersView, false);
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
                List<MovieReview> reviews = new ArrayList<>(NetworkUtils.parseMovieReviewsJson(movieReviewsResult));
                if (!reviews.isEmpty()) {
                    mReviewsTitle.setVisibility(View.VISIBLE);
                    mReviewsView.setVisibility(View.VISIBLE);
                    mReviewsView.setAdapter(new MovieReviewsAdapter(getContext(), reviews));
                    mReviewsView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    mReviewsView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
                    ViewCompat.setNestedScrollingEnabled(mReviewsView, false);
                } else {
                    mReviewsTitle.setVisibility(View.GONE);
                    mReviewsView.setVisibility(View.GONE);
                }
            } else {
                Snackbar.make(mTitle, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Perform async call to fetch if movie was favorited
     */
    public class GetFavoriteMovieTask extends AsyncTask<Uri, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Uri... uris) {
            Cursor cursor;
            try {
                cursor = getActivity().getContentResolver().query(uris[0], null, null, null, null);
            } catch (Exception exception) {
                Log.e(MovieDetailFragment.class.getSimpleName(), "Failed to load data");
                return null;
            }
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null) {
                cursor.moveToFirst();
                try {
                    if (cursor.getInt(cursor.getColumnIndex(MovieContract.FavoriteEntry._ID)) == mMovie.getId()) {
                        mFavorite.setSelected(true);
                    }
                } catch (Exception exception) {
                    Log.e(MovieDetailFragment.class.getSimpleName(), "Movie not found in database");
                }
            }
        }
    }
}
