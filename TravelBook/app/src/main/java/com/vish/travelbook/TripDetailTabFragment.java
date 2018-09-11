package com.vish.travelbook;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.vish.travelbook.database.DbHelper;
import com.vish.travelbook.database.TripContract;
import com.vish.travelbook.model.Trip;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class TripDetailTabFragment extends BaseFragment {

    public static final String TAB_KEY       = "tab_key";
    public static final String PACKING_TAB   = "packing_tab";
    public static final String ITINERARY_TAB = "itinerary_tab";
    public static final String EXPENSES_TAB  = "expenses_tab";

    private RecyclerView recyclerView;

    private String tabType;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail_tab, container, false);
        recyclerView = view.findViewById(R.id.trip_details_recycler_view);
        if (getArguments() != null) {
            trip = (Trip) getArguments().getSerializable(TRIP_KEY);
            tabType = getArguments().getString(TAB_KEY);
        }
        //populateData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override public void onResume() {
        super.onResume();
        showProgressDialog();
        new GetTripTask().execute();
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

    /**
     * Get all trips from the database
     */
    public class GetTripTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor cursor;
            try {
                Uri uri = TripContract.TripEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(trip.id)).build();
                cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            } catch (Exception exception) {
                Log.e(HomeActivity.class.getSimpleName(), "Failed to load data");
                return null;
            }
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null) {
                trip = DbHelper.cursorToTrips(cursor).get(0);
                populateData();
            }
            dismissProgressDialog();
        }
    }
}
