package com.vish.travelbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vish.travelbook.model.Expense;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;

public class TripExpenseAdapter extends RecyclerView.Adapter<TripExpenseAdapter.ExpenseItemViewHolder> {

    private Trip    trip;
    private Context context;

    public TripExpenseAdapter(Context context, Trip trip) {
        this.context = context;
        this.trip = trip;
    }

    public void updateTrip(Trip trip) {
        this.trip = trip;
        notifyDataSetChanged();
    }

    @NonNull @Override public ExpenseItemViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ExpenseItemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_expense_item, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull final ExpenseItemViewHolder holder, final int position) {
        if (!trip.expenses.isEmpty()) {
            Expense expense = trip.expenses.get(position);
            holder.description.setText(expense.description);
            holder.amount.setText(Double.toString(expense.amount));
            holder.dateTime.setText(DateTimeUtils.getNumericalShortDate(expense.dateTime));
        }
    }

    @Override public int getItemCount() {
        if (trip != null && trip.expenses != null) {
            return trip.expenses.size();
        } else {
            return 0;
        }
    }

    public class ExpenseItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView dateTime;
        public TextView description;
        public TextView amount;

        public ExpenseItemViewHolder(View view) {
            super(view);
            dateTime = view.findViewById(R.id.expense_item_datetime);
            description = view.findViewById(R.id.expense_item_title);
            amount = view.findViewById(R.id.expense_item_amount);
        }

        @Override public void onClick(final View view) {
            //TODO open edit expense item
        }
    }
}
