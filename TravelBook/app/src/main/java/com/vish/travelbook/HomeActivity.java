package com.vish.travelbook;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.vish.travelbook.database.DbHelper;
import com.vish.travelbook.database.TripContract.TripEntry;
import com.vish.travelbook.model.Trip;
import java.util.ArrayList;
import java.util.List;

import static com.vish.travelbook.TripEditActivity.EDIT_KEY;
import static com.vish.travelbook.TripEditActivity.EDIT_TRIP;

public class HomeActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private List<Trip>      trips;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAllTripsTask().execute();
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

    /**
     * Get all trips from the database
     */
    public class GetAllTripsTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor cursor;
            try {
                cursor = getContentResolver().query(TripEntry.CONTENT_URI,
                                                    null, null, null, null);
            } catch (Exception exception) {
                Log.e(HomeActivity.class.getSimpleName(), "Failed to load data");
                return null;
            }
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null) {
                updateTrips(cursor);
            }
            dismissProgressDialog();
        }
    }
}
