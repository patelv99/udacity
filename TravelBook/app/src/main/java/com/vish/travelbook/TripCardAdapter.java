package com.vish.travelbook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;
import java.util.List;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;
import static com.vish.travelbook.TripEditActivity.EDIT_KEY;
import static com.vish.travelbook.TripEditActivity.EDIT_TRIP;

public class TripCardAdapter extends RecyclerView.Adapter<TripCardAdapter.TripViewHolder> {

    private List<Trip> trips;
    private Context    context;

    public TripCardAdapter(Context context) {
        this.context = context;
    }

    public void updateTrips(Context context, List<Trip> trips) {
        this.context = context;
        this.trips = trips;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public TripViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_trip_card, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TripViewHolder holder, final int position) {
        Trip trip = trips.get(position);
        holder.tripTitle.setText(trip.title);
        holder.tripDate.setText(DateTimeUtils.getDatesDuration(trip.startDate, trip.endDate));
    }

    @Override
    public int getItemCount() {
        if (trips != null) {
            return trips.size();
        } else {
            return 0;
        }
    }

    public class TripViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView  tripTitle;
        public TextView  tripDate;

        public TripViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            tripTitle = view.findViewById(R.id.trip_title);
            tripDate = view.findViewById(R.id.trip_date);
        }

        @Override
        public void onClick(final View view) {
            //TODO make shared element transition
            Intent intent = new Intent(context, TripDetailActivity.class);
            intent.putExtra(TRIP_KEY, trips.get(getAdapterPosition()));
            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(final View view) {
            Intent intent = new Intent(context, TripEditActivity.class);
            intent.putExtra(EDIT_KEY, EDIT_TRIP);
            intent.putExtra(TRIP_KEY, trips.get(getAdapterPosition()));
            context.startActivity(intent);
            return true;
        }
    }
}
