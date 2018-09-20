package com.vish.travelbook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vish.travelbook.model.ItineraryEvent;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;

import static com.vish.travelbook.ItineraryEventDetailFragment.ITINERARY_EVENT_KEY;
import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;
import static com.vish.travelbook.TripEditActivity.EDIT_ITINERARY_EVENT;
import static com.vish.travelbook.TripEditActivity.EDIT_KEY;

public class TripItineraryAdapter extends RecyclerView.Adapter<TripItineraryAdapter.ItineraryEventViewHolder> {

    private Trip    trip;
    private Context context;

    public TripItineraryAdapter(Context context, Trip trip) {
        this.context = context;
        this.trip = trip;
    }

    public void updateTrip(Trip trip) {
        this.trip = trip;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ItineraryEventViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ItineraryEventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_itinerary_event, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ItineraryEventViewHolder holder, final int position) {
        if (!trip.events.isEmpty()) {
            ItineraryEvent itineraryEvent = trip.events.get(position);
            holder.title.setText(itineraryEvent.title);
            holder.description.setText(itineraryEvent.description);
            holder.date.setText(DateTimeUtils.getEventDatesDuration(itineraryEvent.startTime, itineraryEvent.endTime));
            holder.time.setText(DateTimeUtils.getTimeDuration(itineraryEvent.startTime, itineraryEvent.endTime));
        }
    }

    @Override public int getItemCount() {
        if (trip != null && trip.events != null) {
            return trip.events.size();
        } else {
            return 0;
        }
    }

    public class ItineraryEventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView description;
        public TextView date;
        public TextView time;

        public ItineraryEventViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            title = view.findViewById(R.id.itinerary_event_title);
            description = view.findViewById(R.id.itinerary_event_description);
            date = view.findViewById(R.id.itinerary_event_date);
            time = view.findViewById(R.id.itinerary_event_time);
        }

        @Override
        public void onClick(final View view) {
            Intent intent = new Intent(context, TripEditActivity.class);
            intent.putExtra(EDIT_KEY, EDIT_ITINERARY_EVENT);
            intent.putExtra(TRIP_KEY, trip);
            intent.putExtra(ITINERARY_EVENT_KEY, trip.events.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}
