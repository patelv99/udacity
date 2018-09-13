package com.vish.travelbook;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.vish.travelbook.model.ItineraryEvent;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;
import org.joda.time.DateTime;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class ItineraryEventDetailFragment extends BaseFragment {

    private TextView          title;
    private TextInputEditText eventTitleEditText;
    private TextInputEditText startDateEditText;
    private TextInputEditText endDateEditText;
    private TextInputEditText startTimeEditText;
    private TextInputEditText endTimeEditText;
    private Button            saveButton;

    private String   eventTitle;
    private DateTime startDateTime = DateTime.now();
    private DateTime endDateTime   = DateTime.now();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_itinerary_event, container, false);
        title = view.findViewById(R.id.add_itinerary_event_title);
        eventTitleEditText = view.findViewById(R.id.itinerary_event_title);
        startDateEditText = view.findViewById(R.id.itinerary_event_start_date);
        startDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                setStartDate();
            }
        });
        endDateEditText = view.findViewById(R.id.itinerary_event_end_date);
        endDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                setEndDate();
            }
        });
        startTimeEditText = view.findViewById(R.id.itinerary_event_start_time);
        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                setStartTime();
            }
        });
        endTimeEditText = view.findViewById(R.id.itinerary_event_end_time);
        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                setEndTime();
            }
        });
        saveButton = view.findViewById(R.id.itinerary_event_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                onSaveClick();
            }
        });

        if (getArguments() != null && getArguments().getSerializable(TRIP_KEY) != null) {
            trip = (Trip) getArguments().getSerializable(TRIP_KEY);
        }

        return view;
    }

    /**
     * Set start date for the event
     */
    private void setStartDate() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
                startDateTime = new DateTime().withDate(year, month + 1, day);
                startDateEditText.setText(DateTimeUtils.getNumericalDate(startDateTime));
            }
        };
        showDatePicker(getActivity(), startDateTime, onDateSetListener);
    }

    /**
     * Set start time for the event
     */
    private void setStartTime() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(final TimePicker timePicker, final int hour, final int minute) {
                startDateTime = startDateTime.withTime(hour, minute, 0, 0);
                startTimeEditText.setText(DateTimeUtils.getTime(startDateTime));
            }
        };
        showTimePicker(getActivity(), startDateTime, onTimeSetListener);
    }

    /**
     * Set end date for the event
     */
    private void setEndDate() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
                endDateTime = new DateTime().withDate(year, month + 1, day);
                endDateEditText.setText(DateTimeUtils.getNumericalDate(endDateTime));
            }
        };
        showDatePicker(getActivity(), endDateTime, onDateSetListener);
    }

    /**
     * Set start time for the event
     */
    private void setEndTime() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(final TimePicker timePicker, final int hour, final int minute) {
                endDateTime = endDateTime.withTime(hour, minute, 0, 0);
                endTimeEditText.setText(DateTimeUtils.getTime(endDateTime));
            }
        };
        showTimePicker(getActivity(), endDateTime, onTimeSetListener);
    }

    private void onSaveClick() {
        if (validateItem()) {
            eventTitle = eventTitleEditText.getText().toString();
            ItineraryEvent itineraryEvent = new ItineraryEvent(eventTitle, "", startDateTime, endDateTime);
            trip.events.add(itineraryEvent);
            showProgressDialog();
            updateTripInDB(title);
            dismissProgressDialog();
            getActivity().finish();
        } else {
            Snackbar.make(title, R.string.invalid_packing_item, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Validate that the event has a title and a valid start and end time
     */
    private boolean validateItem() {
        return !eventTitleEditText.getText().toString().isEmpty() &&
               !startDateEditText.getText().toString().isEmpty() &&
               !endDateEditText.getText().toString().isEmpty() &&
               startDateTime.isBefore(endDateTime);
    }
}
