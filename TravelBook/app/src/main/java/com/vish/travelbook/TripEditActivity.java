package com.vish.travelbook;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class TripEditActivity extends AppCompatActivity {

    public static final String EDIT_KEY             = "edit_key";
    public static final String MODIFY_TRIP_KEY      = "trip_key";
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

        Fragment fragment = new TripInfoFragment();
        switch (editType) {
            case EDIT_TRIP:
                fragment = new TripInfoFragment();
                if (getIntent().hasExtra(MODIFY_TRIP_KEY)) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(TripInfoFragment.MODIFY_TRIP, getIntent().getSerializableExtra(MODIFY_TRIP_KEY));
                    fragment.setArguments(bundle);
                }
                break;
            case EDIT_PACKING_ITEM:
                fragment = new PackingItemDetailFragment();
                break;
            case EDIT_ITINERARY_EVENT:
                break;
            case EDIT_EXPENSE:
                break;
            default:
                fragment = new TripInfoFragment();
                break;
        }
        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.activity_trip_add_item_container, fragment, fragment.getClass().getSimpleName())
                                   .commit();
    }
}
