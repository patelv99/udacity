package com.vish.travelbook;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.vish.travelbook.database.DbHelper;
import com.vish.travelbook.database.TripContract;
import com.vish.travelbook.model.Trip;

public abstract class BaseFragment extends Fragment {

    private ProgressDialog progressDialog;
    public  Trip           trip;

    /**
     * Save the trip to the database
     */
    public void saveTripToDB(View view) {
        ContentValues values = DbHelper.createTripContentValues(trip);
        Uri uri = getActivity().getContentResolver().insert(TripContract.TripEntry.CONTENT_URI, values);
        if (uri != null) {
            Snackbar.make(view, trip.title + " was added to db", Snackbar.LENGTH_SHORT).show();
            Log.i(getClass().getSimpleName(), trip.title + " was added to db");
            trip.id = (int) ContentUris.parseId(uri);
        }
    }

    /**
     * Update the trip in the database
     */
    public void updateTripInDB(View view) {
        Uri uri = TripContract.TripEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(trip.id)).build();
        ContentValues values = DbHelper.createTripContentValues(trip);
        int result = getActivity().getContentResolver().update(uri, values, null, null);
        if (result > 0) {
            Snackbar.make(view, trip.title + " was updated in db", Snackbar.LENGTH_SHORT).show();
            Log.i(getClass().getSimpleName(), trip.title + " was updated in db");
        }
    }

    /**
     * Delete the selected trip
     */
    public void deleteTrip(View view) {
        Uri uri = TripContract.TripEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(trip.id)).build();

        int result = getActivity().getContentResolver().delete(uri, null, null);
        if (result > 0) {
            Snackbar.make(view, trip.title + "was deleted from the db", Snackbar.LENGTH_SHORT).show();
            Log.i(getClass().getSimpleName(), trip.title + " was deleted from the db");

        }
    }

    /**
     * Show the progress dialog spinner
     */
    public void showProgressDialog() {
        if (progressDialog == null || !progressDialog.isShowing()) {
            progressDialog = new ProgressDialog(getActivity());
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
    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    /**
     * Get all trips from the database
     */
    public class GetTripTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor cursor;
            try {
                Uri uri = TripContract.TripEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(trip.id)).build();
                cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            } catch (Exception exception) {
                Log.e(HomeActivity.class.getSimpleName(), "Failed to load data");
                return null;
            }
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null) {
                trip = DbHelper.cursorToTrips(cursor).get(0);
            }
        }
    }
}
