package com.vish.travelbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vish.travelbook.model.ItineraryEvent;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;

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

    @NonNull @Override public ItineraryEventViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new ItineraryEventViewHolder(LayoutInflater.from(context).inflate(R.layout.list_itinerary_event, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull final ItineraryEventViewHolder holder, final int position) {
        if (!trip.events.isEmpty()) {
            ItineraryEvent itineraryEvent = trip.events.get(position);
            holder.title.setText(itineraryEvent.title);
            holder.description.setText(itineraryEvent.description);
            holder.time.setText(DateTimeUtils.getEventDurationTime(itineraryEvent.startTime, itineraryEvent.endTime));
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
        public TextView time;

        public ItineraryEventViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.itinerary_event_title);
            description = view.findViewById(R.id.itinerary_event_description);
            time = view.findViewById(R.id.itinerary_event_time);
        }

        @Override public void onClick(final View view) {
            //TODO open edit event item
        }
    }
}
