package com.android.vish.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.android.vish.popularmovies.models.Movie;

public class MovieActivity extends AppCompatActivity {

    public static final String FRAGMENT_KEY   = "fragmentKey";
    public static final String SORT_INDEX_KEY = "sortIndexKey";
    public static final String MOVIE_KEY      = "movieKey";

    public static String sCurrentFrag = "";
    public static int   sSortIndex;
    public static Movie sMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (savedInstanceState != null) {
            sCurrentFrag = savedInstanceState.getString(FRAGMENT_KEY);
            sSortIndex = savedInstanceState.getInt(SORT_INDEX_KEY);
        }
        Bundle bundle = new Bundle();
        Fragment openFragment;
        if (sCurrentFrag.equals(MovieGridFragment.class.getSimpleName())) {
            bundle.putInt(SORT_INDEX_KEY, sSortIndex);
            openFragment = new MovieGridFragment();
            openFragment.setArguments(bundle);
        } else if (sCurrentFrag.equals(MovieDetailFragment.class.getSimpleName())) {
            bundle.putSerializable(MOVIE_KEY, sMovie);
            openFragment = new MovieDetailFragment();
            openFragment.setArguments(bundle);
        } else {
            openFragment = new MovieGridFragment();
        }
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_movie_fragment_container, openFragment, openFragment.getClass().getSimpleName())
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FRAGMENT_KEY, sCurrentFrag);
        outState.putInt(SORT_INDEX_KEY, sSortIndex);
        outState.putSerializable(MOVIE_KEY, sMovie);
    }
}
