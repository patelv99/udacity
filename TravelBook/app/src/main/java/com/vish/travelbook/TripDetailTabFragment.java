package com.vish.travelbook;

import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vish.travelbook.database.DbHelper;
import com.vish.travelbook.database.TripContract;
import com.vish.travelbook.model.PackingItem;
import com.vish.travelbook.model.Trip;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.Adapter;
import static androidx.recyclerview.widget.RecyclerView.VERTICAL;
import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class TripDetailTabFragment extends BaseFragment {

    public static final String TAB_KEY = "tab_key";
    public static final String PACKING_TAB = "packing_tab";
    public static final String ITINERARY_TAB = "itinerary_tab";
    public static final String EXPENSES_TAB = "expenses_tab";

    public static final String FIRESTORE_PACKING_LIST_COLLECTION_KEY = "packingLists";
    public static final String FIRESTORE_PACKING_LIST_DOC_KEY = "places";
    public static final String FIRESTORE_PACKING_LIST_PLACES_MAP_KEY = "placesMap";

    private RecyclerView recyclerView;
    private Adapter adapter = null;

    private String tabType;
    private Map<String, List<String>> packingLists;
    private int selectedPlace = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_detail_tab, container, false);
        recyclerView = view.findViewById(R.id.trip_details_recycler_view);
        if (getArguments() != null) {
            trip = (Trip) getArguments().getSerializable(TRIP_KEY);
            tabType = getArguments().getString(TAB_KEY);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showProgressDialog();
        new GetTripTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (tabType.equals(PACKING_TAB)) {
            inflater.inflate(R.menu.menu_packing_list, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_get_suggestions:
                showProgressDialog();
                getPackingSuggestions();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateData() {
        switch (tabType) {
            case PACKING_TAB:
                adapter = new TripPackingAdapter(getContext(), trip);
                setHasOptionsMenu(true);
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

    private void getPackingSuggestions() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                packingLists = (Map<String, List<String>>) dataSnapshot.getValue();
                if (packingLists != null && !packingLists.isEmpty()) {
                    String[] placeStrings = packingLists.keySet().toArray(new String[packingLists.keySet().size()]);
                    showSuggestionDialog(placeStrings);
                }
                dismissProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(HomeActivity.class.getSimpleName(), databaseError.getMessage());
                dismissProgressDialog();
                Snackbar.make(recyclerView, getString(R.string.firebase_suggestions_error), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuggestionDialog(final String[] places) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(places, selectedPlace, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPlace = which;
            }
        }).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addPackingItems(packingLists.get(places[selectedPlace]));
                dialog.dismiss();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void addPackingItems(List<String> suggestedPackingItems) {
        for (String packingItem : suggestedPackingItems) {
            trip.packingItems.add(new PackingItem(packingItem, 1));
        }
        updateTripInDB(recyclerView);
        adapter.notifyDataSetChanged();
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
