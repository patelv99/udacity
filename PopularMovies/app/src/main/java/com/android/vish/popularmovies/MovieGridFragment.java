package com.android.vish.popularmovies;

import android.app.ProgressDialog;
import android.database.Cursor;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import com.android.vish.popularmovies.adapters.MovieGridAdapter;
import com.android.vish.popularmovies.data.MovieContract;
import com.android.vish.popularmovies.models.Movie;
import com.android.vish.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovieGridFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    private GridView       mMovieGrid;
    private List<Movie> mMovieList = new ArrayList<>();
    private MovieGridAdapter mMovieGridAdapter;
    private Cursor           mCursor;
    private int              mSortIndex;

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
        if (getArguments() != null) {
            mSortIndex = getArguments().getInt(MovieActivity.SORT_INDEX_KEY);
        }
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.movies));
        mMovieGridAdapter = new MovieGridAdapter(getActivity(), mMovieList);
        mMovieGrid.setAdapter(mMovieGridAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MovieActivity.sCurrentFrag = this.getClass().getSimpleName();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_sort_menu, menu);
        Spinner spinner = (Spinner) menu.findItem(R.id.spinner).getActionView();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                                                          Arrays.asList(getResources().getStringArray(R.array.movie_sort_options)));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // position is the index of the item found in movie_sort_options
                switch (position) {
                    case 0:
                        new GetMoviesTask().execute(NetworkUtils.buildGetMoviesUrl(NetworkUtils.POPULAR_ENDPOINT));
                        MovieActivity.sSortIndex = 0;
                        break;
                    case 1:
                        new GetMoviesTask().execute(NetworkUtils.buildGetMoviesUrl(NetworkUtils.TOP_RATED_ENDPOINT));
                        MovieActivity.sSortIndex = 1;
                        break;
                    case 2:
                        new GetAllFavoriteMoviesTask().execute();
                        MovieActivity.sSortIndex = 2;
                        break;
                    default:
                        new GetMoviesTask().execute(NetworkUtils.buildGetMoviesUrl(NetworkUtils.POPULAR_ENDPOINT));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(mSortIndex);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCursor != null) {
            mCursor.close();
        }
    }

    public void addMovies() {
        if (mCursor != null) {
            mMovieList.clear();
            mCursor.moveToFirst();
            while (mCursor.moveToNext()) {
                Movie movie = new Movie();
                movie.setId(mCursor.getInt(mCursor.getColumnIndex(MovieContract.FavoriteEntry._ID)));
                movie.setImage(mCursor.getString(mCursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_POSTER)));
                movie.setTitle(mCursor.getString(mCursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_TITLE)));
                movie.setReleaseDate(mCursor.getString(mCursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE)));
                movie.setAverageVote(mCursor.getInt(mCursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_RATING)));
                movie.setPlot(mCursor.getString(mCursor.getColumnIndex(MovieContract.FavoriteEntry.COLUMN_MOVIE_PLOT)));
                mMovieList.add(movie);
            }
        }
        mMovieGridAdapter.updateMovies(mMovieList);
    }

    /**
     * When a movie tile is clicked, open {@link MovieDetailFragment} with the movie info
     *
     * @param position the position of the movie in the list
     */
    public void onMovieClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(MovieActivity.MOVIE_KEY, mMovieList.get(position));
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
                Log.e(this.getClass().getSimpleName(), e.getMessage());
            }
            return moviesResponse;
        }

        @Override
        protected void onPostExecute(String popularMoviesResult) {
            dismissProgressDialog();
            if (!TextUtils.isEmpty(popularMoviesResult)) {
                mMovieList = new ArrayList<>(NetworkUtils.parseMoviesJson(popularMoviesResult));
                mMovieGridAdapter.updateMovies(mMovieList);
            } else {
                Snackbar.make(mMovieGrid, getString(R.string.network_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Perform async call to fetch all favorite movies
     */
    public class GetAllFavoriteMoviesTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor cursor;
            try {
                cursor = getActivity().getContentResolver().query(MovieContract.FavoriteEntry.CONTENT_URI,
                                                                  null, null, null, null);
            } catch (Exception exception) {
                Log.e(MovieDetailFragment.class.getSimpleName(), getString(R.string.failed_to_load_data));
                return null;
            }
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null) {
                mCursor = cursor;
                addMovies();
            }
        }
    }
}
