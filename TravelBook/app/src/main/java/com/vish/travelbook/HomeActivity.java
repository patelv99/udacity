package com.vish.travelbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.vish.travelbook.database.TripContract.TripEntry;
import com.vish.travelbook.model.Trip;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

import static com.vish.travelbook.TripEditActivity.EDIT_KEY;
import static com.vish.travelbook.TripEditActivity.EDIT_TRIP;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView   recyclerView;
    private ProgressDialog progressDialog;

    private List<Trip>      trips;
    private Cursor          tripsCursor;
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

    @Override protected void onResume() {
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
     * Show the progress dialog spinner
     */
    protected void showProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.show();
            progressDialog.setContentView(getLayoutInflater().inflate(R.layout.progress_spinner, null));
        }
    }

    /**
     * Dismiss the progress dialog spinner
     */
    protected void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * Update the list of trips with those fetched from the database
     */
    public void updateTrips() {
        if (tripsCursor != null) {
            trips.clear();
            while (tripsCursor.moveToNext()) {
                Trip trip = new Trip();
                trip.id = tripsCursor.getInt(tripsCursor.getColumnIndex(TripEntry._ID));
                trip.title = tripsCursor.getString(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_TITLE));
                trip.startDate = new DateTime(tripsCursor.getLong(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_START)));
                trip.endDate = new DateTime(tripsCursor.getLong(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_END)));
                trip.image = tripsCursor.getString(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_IMAGE));
                trips.add(trip);
            }
        }
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
                tripsCursor = cursor;
                updateTrips();
            }
            dismissProgressDialog();
        }
    }
}
