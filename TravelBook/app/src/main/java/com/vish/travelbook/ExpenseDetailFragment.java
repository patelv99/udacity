package com.vish.travelbook;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.vish.travelbook.model.Expense;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;
import org.joda.time.DateTime;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class ExpenseDetailFragment extends BaseFragment {

    private TextView          title;
    private TextInputEditText expenseDescriptionEditText;
    private TextInputEditText expenseDateEditText;
    private TextInputEditText expenseAmountEditText;
    private Button            saveButton;

    private DateTime expenseDate = DateTime.now();

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        title = view.findViewById(R.id.add_expense_title);
        expenseDescriptionEditText = view.findViewById(R.id.expense_description);
        expenseAmountEditText = view.findViewById(R.id.expense_amount);
        expenseDateEditText = view.findViewById(R.id.expense_date);
        expenseDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(final View v) {
                setDate();
            }
        });
        expenseAmountEditText = view.findViewById(R.id.expense_amount);
        saveButton = view.findViewById(R.id.expense_save_button);
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
     * Set date for the expense
     */
    private void setDate() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(final DatePicker datePicker, final int year, final int month, final int day) {
                expenseDate = new DateTime().withDate(year, month + 1, day);
                expenseDateEditText.setText(DateTimeUtils.getNumericalDate(expenseDate));
            }
        };
        showDatePicker(getActivity(), expenseDate, onDateSetListener);
    }

    private void onSaveClick() {
        if (validateItem()) {
            Expense expense = new Expense(expenseDescriptionEditText.getText().toString(), 0.0, new DateTime());
            trip.expenses.add(expense);
            showProgressDialog();
            updateTripInDB(title);
            dismissProgressDialog();
            getActivity().finish();
        } else {
            Snackbar.make(title, R.string.invalid_packing_item, Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Validate that the expense has a description, date and amount
     */
    private boolean validateItem() {
        return !expenseDescriptionEditText.getText().toString().isEmpty() &&
               !expenseDateEditText.getText().toString().isEmpty() &&
               !expenseAmountEditText.getText().toString().isEmpty();
    }
}
