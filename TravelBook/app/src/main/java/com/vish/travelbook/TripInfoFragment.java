package com.vish.travelbook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;
import com.vish.travelbook.utils.ImageUtils;

import org.joda.time.DateTime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.app.Activity.RESULT_OK;
import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;
import static com.vish.travelbook.utils.ImageUtils.SELECT_PICTURE_REQUEST_CODE;

public class TripInfoFragment extends BaseFragment {

    private TextView screenTitle;
    private TextInputEditText tripTitleEditText;
    private TextInputEditText tripStartEditText;
    private TextInputEditText tripEndEditText;
    private ImageView tripImageButton;
    private ImageView tripImage;
    private Button tripSaveButton;

    private boolean modifying = false;
    private DateTime tripStartDate = DateTime.now();
    private DateTime tripEndDate = DateTime.now();
    private String tripImageString = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_info, container, false);

        screenTitle = view.findViewById(R.id.trip_info_screen);
        tripTitleEditText = view.findViewById(R.id.trip_info_title);
        tripStartEditText = view.findViewById(R.id.trip_info_start);
        tripEndEditText = view.findViewById(R.id.trip_info_end);
        tripImage = view.findViewById(R.id.trip_info_image);
        tripImageButton = view.findViewById(R.id.trip_info_image_button);
        tripImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
        tripSaveButton = view.findViewById(R.id.trip_info_save_button);
        tripSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onSaveClick();
            }
        });

        if (getArguments() != null && getArguments().getSerializable(TRIP_KEY) != null) {
            trip = (Trip) getArguments().getSerializable(TRIP_KEY);
            modifyTrip();
            setHasOptionsMenu(true);
        }

        tripStartEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                setStartDate();
            }
        });
        tripEndEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                setEndDate();
            }
        });

        return view;
    }

    /**
     * Set start date for the trip
     */
    private void setStartDate() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
                DateTime startDate = new DateTime().withDate(year, month + 1, day);
                startDate.withTimeAtStartOfDay();
                tripStartEditText.setText(DateTimeUtils.getNumericalDate(startDate));
            }
        };
        showDatePicker(getActivity(), tripStartDate, onDateSetListener);
    }

    /**
     * Set end date for the trip
     */
    private void setEndDate() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
                DateTime endDate = new DateTime().withDate(year, month + 1, day);
                endDate.withTimeAtStartOfDay();
                tripEndEditText.setText(DateTimeUtils.getNumericalDate(endDate));
            }
        };
        showDatePicker(getActivity(), tripEndDate, onDateSetListener);
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
                showProgressDialog();
                deleteTrip(screenTitle);
                dismissProgressDialog();
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE_REQUEST_CODE) {
                Uri imageUri = data.getData();
                if (imageUri != null) {
                    tripImageString = ImageUtils.encodeImageToBase64(imageUri, getActivity());
                    tripImage.setImageBitmap(ImageUtils.base64ToBitmap(tripImageString));
                }
            }
        }
    }

    /**
     * Populate trip data for modification
     */
    private void modifyTrip() {
        modifying = true;
        tripTitleEditText.setText(trip.title);
        tripStartEditText.setText(DateTimeUtils.getNumericalShortDate(trip.startDate));
        tripEndEditText.setText(DateTimeUtils.getNumericalShortDate(trip.endDate));
        if (!trip.image.isEmpty()) {
            tripImageString = trip.image;
            tripImage.setImageBitmap(ImageUtils.base64ToBitmap(tripImageString));
        }
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
        trip.startDate = DateTimeUtils.parseNumericalShortDate(tripStartEditText.getText().toString());
        trip.endDate = DateTimeUtils.parseNumericalShortDate(tripEndEditText.getText().toString());
        trip.image = tripImageString;

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
                updateTripInDB(screenTitle);
            } else {
                saveTripToDB(screenTitle);
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

}
