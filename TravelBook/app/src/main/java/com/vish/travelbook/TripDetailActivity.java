package com.vish.travelbook;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.vish.travelbook.database.DbHelper;
import com.vish.travelbook.database.TripContract;
import com.vish.travelbook.model.Trip;
import java.util.ArrayList;
import java.util.List;

import static com.vish.travelbook.TripDetailTabFragment.EXPENSES_TAB;
import static com.vish.travelbook.TripDetailTabFragment.ITINERARY_TAB;
import static com.vish.travelbook.TripDetailTabFragment.PACKING_TAB;
import static com.vish.travelbook.TripDetailTabFragment.TAB_KEY;
import static com.vish.travelbook.TripEditActivity.EDIT_KEY;
import static com.vish.travelbook.TripEditActivity.EDIT_PACKING_ITEM;

public class TripDetailActivity extends BaseActivity {

    public static final String TRIP_KEY = "TRIP_KEY";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Trip      trip;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {
            trip = (Trip) getIntent().getSerializableExtra(TRIP_KEY);
        }

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFABClick();
            }
        });

        viewPager = findViewById(R.id.trip_details_viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.trip_details_tablayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetTripTask().execute();
    }

    private void onFABClick() {
    }

    private void setupViewPager(ViewPager viewPager) {
        TripDetailsPagerAdapter adapter = new TripDetailsPagerAdapter(getSupportFragmentManager());

        adapter.addTabFragment(createTabFragment(PACKING_TAB), getString(R.string.packing_tab));
        adapter.addTabFragment(createTabFragment(ITINERARY_TAB), getString(R.string.itinerary_tab));
        adapter.addTabFragment(createTabFragment(EXPENSES_TAB), getString(R.string.expenses_tab));

        viewPager.setAdapter(adapter);
    }

    public TripDetailTabFragment createTabFragment(String tabType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRIP_KEY, trip);
        bundle.putString(TAB_KEY, tabType);
        TripDetailTabFragment tripDetailTabFragment = new TripDetailTabFragment();
        tripDetailTabFragment.setArguments(bundle);
        return tripDetailTabFragment;
    }

    private class TripDetailsPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> tabFragments = new ArrayList<>();
        private List<String>   tabTitles    = new ArrayList<>();

        TripDetailsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(final int position) {
            return tabFragments.get(position);
        }

        @Override
        public int getCount() {
            return tabFragments.size();
        }

        public void addTabFragment(Fragment tabFragment, String tabTitle) {
            tabFragments.add(tabFragment);
            tabTitles.add(tabTitle);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(final int position) {
            return tabTitles.get(position);
        }
    }
}
