package com.vish.travelbook;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    public static final String EXPENSE_KEY = "expense_key";

    private TextView          title;
    private TextInputEditText expenseDescriptionEditText;
    private TextInputEditText expenseDateEditText;
    private TextInputEditText expenseAmountEditText;
    private Button            saveButton;

    private DateTime expenseDate = DateTime.now();
    private boolean  modifying   = false;
    private Expense  expense     = new Expense();

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
            if (getArguments().getSerializable(EXPENSE_KEY) != null) {
                expense = (Expense) getArguments().getSerializable(EXPENSE_KEY);
                modifyExpense();
                setHasOptionsMenu(true);
            }
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
                showProgressDialog();
                deleteExpense(title, expense);
                dismissProgressDialog();
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void modifyExpense() {
        modifying = true;
        expenseDescriptionEditText.setText(expense.description);
        expenseAmountEditText.setText(Double.toString(expense.amount));
        expenseDate = expense.dateTime;
        expenseDateEditText.setText(DateTimeUtils.getNumericalDate(expense.dateTime));
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

    /**
     * Save the expense and trip on save click
     */
    private void onSaveClick() {
        if (validateItem()) {
            showProgressDialog();
            if (modifying) {
                int index = trip.expenses.indexOf(expense);
                expense.description = expenseDescriptionEditText.getText().toString();
                expense.amount = Double.parseDouble(expenseAmountEditText.getText().toString());
                expense.dateTime = expenseDate;
                trip.expenses.set(index, expense);
            } else {
                expense = new Expense(expenseDescriptionEditText.getText().toString(),
                                      Double.parseDouble(expenseAmountEditText.getText().toString()),
                                      expenseDate);
                trip.expenses.add(expense);
            }
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
