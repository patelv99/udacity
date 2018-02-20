package com.android.vish.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.vish.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {

    public static final String MOVIE_KEY = "movieKey";

    private ImageView mPoster;
    private TextView  mTitle;
    private TextView  mReleaseDate;
    private TextView  mAverageVote;
    private TextView  mPlot;
    private Movie     mMovie;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mPoster = view.findViewById(R.id.movie_detail_poster);
        mTitle = view.findViewById(R.id.movie_detail_title);
        mReleaseDate = view.findViewById(R.id.movie_detail_release_date);
        mAverageVote = view.findViewById(R.id.movie_detail_vote_average);
        mPlot = view.findViewById(R.id.movie_detail_plot);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable(MOVIE_KEY);
            Picasso.with(getActivity()).load(mMovie.getImage()).into(mPoster);
            mTitle.setText(mMovie.getTitle());
            mReleaseDate.setText(getString(R.string.release_date, mMovie.getReleaseDate()));
            mAverageVote.setText(getString(R.string.rating, mMovie.getAverageVote()));
            mPlot.setText(mMovie.getPlot());
            getActivity().setTitle(mMovie.getTitle());
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
}
