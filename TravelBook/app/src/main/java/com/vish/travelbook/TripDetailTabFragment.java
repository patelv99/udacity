package com.vish.travelbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vish.travelbook.model.Trip;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class TripDetailTabFragment extends Fragment {

    public static final String TAB_KEY       = "tab_key";
    public static final String PACKING_TAB   = "packing_tab";
    public static final String ITINERARY_TAB = "itinerary_tab";
    public static final String EXPENSES_TAB  = "expenses_tab";

    private RecyclerView recyclerView;

    private Trip               trip;
    private String             tabType;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail_tab, container, false);
        recyclerView = view.findViewById(R.id.trip_details_recycler_view);
        if (getArguments() != null) {
            trip = (Trip) getArguments().getSerializable(TRIP_KEY);
            tabType = getArguments().getString(TAB_KEY);
        }
        populateData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    private void populateData() {
        RecyclerView.Adapter adapter = null;
        switch (tabType) {
            case PACKING_TAB:
                adapter = new TripPackingAdapter(getContext(), trip);
                break;
            case ITINERARY_TAB:
                adapter = new TripItineraryAdapter(getContext(), trip);
                break;
            case EXPENSES_TAB:
                adapter = new TripExpenseAdapter(getContext(), trip);
                break;
            default:
                break;
        }
        if (adapter != null) {
            recyclerView.setAdapter(adapter);
        }
    }

    public void loadPackingList() {
        TripPackingAdapter tripPackingAdapter = new TripPackingAdapter(getContext(), trip);
        tripPackingAdapter.updateTrip(trip);
        recyclerView.setAdapter(tripPackingAdapter);
    }

    public void loadItinerary() {
        TripItineraryAdapter tripItineraryAdapter = new TripItineraryAdapter(getContext(), trip);
        tripItineraryAdapter.updateTrip(trip);
        recyclerView.setAdapter(tripItineraryAdapter);
    }

    public void loadExpenses() {

    }
}
