package com.vish.travelbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.vish.travelbook.model.PackingItem;
import com.vish.travelbook.model.Trip;

public class TripPackingAdapter extends RecyclerView.Adapter<TripPackingAdapter.PackingItemViewHolder> {

    private Trip    trip;
    private Context context;

    public TripPackingAdapter(Context context, Trip trip) {
        this.context = context;
        this.trip = trip;
    }

    public void updateTrip(Trip trip) {
        this.trip = trip;
        notifyDataSetChanged();
    }

    @NonNull @Override public PackingItemViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new PackingItemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_packing_item, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull final PackingItemViewHolder holder, final int position) {
        if (!trip.packingItems.isEmpty()) {
            PackingItem packingItem = trip.packingItems.get(position);
            holder.quantity.setText(Integer.toString(packingItem.quantity));
            holder.item.setText(packingItem.item);
        }
    }

    @Override public int getItemCount() {
        if (trip != null && trip.packingItems != null) {
            return trip.packingItems.size();
        } else {
            return 0;
        }
    }

    public class PackingItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView quantity;
        public TextView item;

        public PackingItemViewHolder(View view) {
            super(view);
            quantity = view.findViewById(R.id.packing_item_quantity);
            item = view.findViewById(R.id.packing_item_title);
        }

        @Override public void onClick(final View view) {
            //TODO open edit packing item
        }
    }
}
