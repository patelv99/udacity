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
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Spinner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieGridFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    private GridView       mMovieGrid;
    private List<Movie> mMovieList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        mMovieGrid = view.findViewById(R.id.movie_grid_gridview);
        mMovieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onMovieClick(position);
            }
        });
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.movies));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Spinner spinner = (Spinner) menu.findItem(R.id.spinner).getActionView();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position is the index of the item found in movie_sort_options
                switch (position) {
                    case 0:
                        new GetMoviesTask().execute(NetworkUtils.buildUrl(NetworkUtils.POPULAR_ENDPOINT));
                        break;
                    case 1:
                        new GetMoviesTask().execute(NetworkUtils.buildUrl(NetworkUtils.TOP_RATED_ENDPOINT));
                        break;
                    default:
                        new GetMoviesTask().execute(NetworkUtils.buildUrl(NetworkUtils.POPULAR_ENDPOINT));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
    }

    /**
     * When a movie tile is clicked, open {@link MovieDetailFragment} with the movie info
     *
     * @param position the position of the movie in the list
     */
    public void onMovieClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieDetailFragment.MOVIE_KEY, mMovieList.get(position));
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_movie_fragment_container, movieDetailFragment, MovieDetailFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();

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
     * Perform async call to fetch all popular movies
     */
    public class GetMoviesTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL moviesResponseUrl = params[0];
            String moviesResponse = null;
            try {
                moviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesResponseUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return moviesResponse;
        }

        @Override
        protected void onPostExecute(String popularMoviesResult) {
            dismissProgressDialog();
            if (!TextUtils.isEmpty(popularMoviesResult)) {
                mMovieList = new ArrayList<>(NetworkUtils.parseMoviesJson(popularMoviesResult));
                mMovieGrid.setAdapter(new MovieGridAdapter(getActivity(), mMovieList));
            } else {
                Snackbar.make(mMovieGrid, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
