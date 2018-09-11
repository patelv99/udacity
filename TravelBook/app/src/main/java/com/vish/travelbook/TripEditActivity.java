package com.vish.travelbook;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import com.vish.travelbook.model.Trip;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class TripEditActivity extends AppCompatActivity {

    public static final String EDIT_KEY             = "edit_key";
    public static final String EDIT_TRIP            = "edit_trip";
    public static final String EDIT_PACKING_ITEM    = "edit_packing_item";
    public static final String EDIT_ITINERARY_EVENT = "edit_itinerary_event";
    public static final String EDIT_EXPENSE         = "edit_expense";

    private String editType = "";
    private Trip   trip;

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
        if (getIntent().hasExtra(TRIP_KEY)) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(TRIP_KEY, getIntent().getSerializableExtra(TRIP_KEY));
            fragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction()
                                   .add(R.id.activity_trip_add_item_container, fragment, fragment.getClass().getSimpleName())
                                   .commit();
    }
}
