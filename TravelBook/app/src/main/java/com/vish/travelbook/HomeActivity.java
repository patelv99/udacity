package com.vish.travelbook;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.vish.travelbook.database.DbHelper;
import com.vish.travelbook.database.TripContract.TripEntry;
import com.vish.travelbook.model.Trip;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static com.vish.travelbook.TripEditActivity.EDIT_KEY;
import static com.vish.travelbook.TripEditActivity.EDIT_TRIP;
import static com.vish.travelbook.database.TripContentProvider.TRIP_LOADER_ID;

public class HomeActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;

    private List<Trip> trips;
    private TripCardAdapter tripCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFABClick();
            }
        });

        recyclerView = findViewById(R.id.trip_recycler_view);

        tripCardAdapter = new TripCardAdapter(this);
        recyclerView.setAdapter(tripCardAdapter);

        int columnCount = getResources().getInteger(R.integer.trip_list_columns);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        trips = new ArrayList<>();
        showProgressDialog();
        getLoaderManager().initLoader(TRIP_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Open TripEditActivity when the FAB is clicked
     */
    private void onFABClick() {
        Intent intent = new Intent(this, TripEditActivity.class);
        intent.putExtra(EDIT_KEY, EDIT_TRIP);
        startActivity(intent);
    }

    /**
     * Update the list of trips with those fetched from the database
     */
    public void updateTrips(Cursor cursor) {
        trips = new ArrayList<>(DbHelper.cursorToTrips(cursor));
        tripCardAdapter.updateTrips(this, trips);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader loader = new CursorLoader(this, TripEntry.CONTENT_URI, null, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            updateTrips(data);
        }
        dismissProgressDialog();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
