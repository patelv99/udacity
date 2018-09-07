package com.vish.travelbook;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.vish.travelbook.database.TripContract.TripEntry;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class TripInfoFragment extends Fragment {

    public static final String MODIFY_TRIP = "modify_trip";

    private ProgressDialog    progressDialog;
    private TextView          screenTitle;
    private TextInputEditText tripTitleEditText;
    private EditText          tripStartEditText;
    private EditText          tripEndEditText;
    private Button            tripSaveButton;

    private Trip    trip;
    private boolean modifying = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_info, container, false);

        screenTitle = view.findViewById(R.id.trip_info_screen);
        tripTitleEditText = view.findViewById(R.id.trip_info_title);
        tripStartEditText = view.findViewById(R.id.trip_info_start);
        tripEndEditText = view.findViewById(R.id.trip_info_end);
        tripSaveButton = view.findViewById(R.id.trip_info_save_button);
        tripSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View view) {
                onSaveClick();
            }
        });

        if (getArguments() != null && getArguments().getSerializable(MODIFY_TRIP) != null) {
            trip = (Trip) getArguments().getSerializable(MODIFY_TRIP);
            modifyTrip();
            setHasOptionsMenu(true);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (modifying) {
            inflater.inflate(R.menu.menu_trip, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteTrip();
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Populate trip data for modification
     */
    private void modifyTrip() {
        modifying = true;
        tripTitleEditText.setText(trip.title);
        tripStartEditText.setText(DateTimeUtils.getNumericalShortDate(trip.startDate));
        tripEndEditText.setText(DateTimeUtils.getNumericalShortDate(trip.endDate));
    }

    /**
     * Validate that the trip title is not null or empty and the start date is before the end date
     *
     * @return boolean
     */
    private boolean validateTripInfo() {
        if (trip == null) {
            trip = new Trip();
        }
        trip.title = tripTitleEditText.getText().toString();
        trip.startDate = DateTimeUtils.NUMERICAL_DATE_SHORT_FORMAT.parseDateTime(tripStartEditText.getText().toString());
        trip.endDate = DateTimeUtils.NUMERICAL_DATE_SHORT_FORMAT.parseDateTime(tripEndEditText.getText().toString());

        return !TextUtils.isEmpty(trip.title) && trip.startDate.isBefore(trip.endDate);
    }

    /**
     * Validate trip info is complete and save to DB
     * After Trip is saved, proceed to trip details
     */
    private void onSaveClick() {
        showProgressDialog();
        if (validateTripInfo()) {
            if (modifying) {
                updateTripInDB();
            } else {
                saveTripToDB();
            }
            dismissProgressDialog();

            Intent intent = new Intent(getActivity(), TripDetailActivity.class);
            intent.putExtra(TRIP_KEY, trip);
            getActivity().startActivity(intent);
            getActivity().finish();
        } else {
            dismissProgressDialog();
            Snackbar.make(screenTitle, R.string.invalid_trip_info, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Save the trip to the database
     */
    private void saveTripToDB() {
        ContentValues values = new ContentValues();
        values.put(TripEntry.COLUMN_TRIP_TITLE, trip.title);
        values.put(TripEntry.COLUMN_TRIP_START, trip.startDate.getMillis());
        values.put(TripEntry.COLUMN_TRIP_END, trip.endDate.getMillis());
        values.put(TripEntry.COLUMN_TRIP_IMAGE, trip.getImage());
        Uri uri = getActivity().getContentResolver().insert(TripEntry.CONTENT_URI, values);
        if (uri != null) {
            Snackbar.make(screenTitle, trip.title + " was added to db", Snackbar.LENGTH_SHORT).show();
            Log.i(getClass().getSimpleName(), trip.title + " was added to db");
        }
    }

    /**
     * Update the trip in the database
     */
    private void updateTripInDB() {
        Uri uri = TripEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(trip.id)).build();

        ContentValues values = new ContentValues();
        values.put(TripEntry.COLUMN_TRIP_TITLE, trip.title);
        values.put(TripEntry.COLUMN_TRIP_START, trip.startDate.getMillis());
        values.put(TripEntry.COLUMN_TRIP_END, trip.endDate.getMillis());
        values.put(TripEntry.COLUMN_TRIP_IMAGE, trip.getImage());
        int result = getActivity().getContentResolver().update(uri, values, null, null);
        if (result > 0) {
            Snackbar.make(screenTitle, trip.title + " was updated in db", Snackbar.LENGTH_SHORT).show();
            Log.i(getClass().getSimpleName(), trip.title + " was updated in db");
        }
    }

    /**
     * Delete the selected trip
     */
    private void deleteTrip() {
        Uri uri = TripEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(trip.id)).build();

        int result = getActivity().getContentResolver().delete(uri, null, null);
        if (result > 0) {
            Snackbar.make(screenTitle, trip.title + "was deleted from the db", Snackbar.LENGTH_SHORT).show();
            Log.i(getClass().getSimpleName(), trip.title + " was deleted from the db");

        }
    }

    /**
     * Show the progress dialog spinner
     */
    private void showProgressDialog() {
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
    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
