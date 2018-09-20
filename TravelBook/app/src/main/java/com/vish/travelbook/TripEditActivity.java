package com.vish.travelbook;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import static com.vish.travelbook.ItineraryEventDetailFragment.ITINERARY_EVENT_KEY;
import static com.vish.travelbook.PackingItemDetailFragment.PACKING_ITEM_KEY;
import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class TripEditActivity extends BaseActivity {

    public static final String EDIT_KEY             = "edit_key";
    public static final String EDIT_TRIP            = "edit_trip";
    public static final String EDIT_PACKING_ITEM    = "edit_packing_item";
    public static final String EDIT_ITINERARY_EVENT = "edit_itinerary_event";
    public static final String EDIT_EXPENSE         = "edit_expense";

    private String editType = "";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_add_item);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            editType = getIntent().getStringExtra(EDIT_KEY);
        }

        Fragment fragment;
        Bundle bundle = new Bundle();

        switch (editType) {
            case EDIT_TRIP:
                fragment = new TripInfoFragment();
                break;
            case EDIT_PACKING_ITEM:
                fragment = new PackingItemDetailFragment();
                if (getIntent().hasExtra(PACKING_ITEM_KEY)) {
                    bundle.putSerializable(PACKING_ITEM_KEY, getIntent().getSerializableExtra(PACKING_ITEM_KEY));
                }
                break;
            case EDIT_ITINERARY_EVENT:
                fragment = new ItineraryEventDetailFragment();
                if (getIntent().hasExtra(ITINERARY_EVENT_KEY)) {
                    bundle.putSerializable(ITINERARY_EVENT_KEY, getIntent().getSerializableExtra(ITINERARY_EVENT_KEY));
                }
                break;
            case EDIT_EXPENSE:
                fragment = new ExpenseDetailFragment();
                break;
            default:
                fragment = new TripInfoFragment();
                break;
        }
        if (getIntent().hasExtra(TRIP_KEY)) {
            bundle.putSerializable(TRIP_KEY, getIntent().getSerializableExtra(TRIP_KEY));
            fragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.activity_trip_add_item_container, fragment, fragment.getClass().getSimpleName())
                                   .commit();
    }
}
