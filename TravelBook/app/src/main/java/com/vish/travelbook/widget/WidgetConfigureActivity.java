package com.vish.travelbook.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vish.travelbook.BaseActivity;
import com.vish.travelbook.HomeActivity;
import com.vish.travelbook.R;
import com.vish.travelbook.database.DbHelper;
import com.vish.travelbook.database.TripContract;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeTypeAdapter;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class WidgetConfigureActivity extends BaseActivity {

    private List<Trip>      trips = new ArrayList<>();
    private TripListAdapter tripArrayAdapter;
    private int             appWidgetId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            appWidgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.activity_widget_configure);
        ListView tripsListView = findViewById(R.id.activity_widget_configure_trip_list);
        tripArrayAdapter = new TripListAdapter(this, R.layout.widget_list_trip, trips);
        tripsListView.setAdapter(tripArrayAdapter);
        tripsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                onTripClick(trips.get(position));
            }
        });
        showProgressDialog();
        new GetAllTripsTask().execute();
    }

    public void onTripClick(Trip trip) {
        Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter()).create();
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                         .putString(Integer.toString(appWidgetId), gson.toJson(trip))
                         .apply();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_itinerary);
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Intent intent = new Intent(this, ItineraryWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId });
        sendBroadcast(intent);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Get all trips from the database
     */
    public class GetAllTripsTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            Cursor cursor;
            try {
                cursor = getContentResolver().query(TripContract.TripEntry.CONTENT_URI,
                                                    null, null, null, null);
            } catch (Exception exception) {
                Log.e(HomeActivity.class.getSimpleName(), "Failed to load data");
                return null;
            }
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if (cursor != null) {
                trips = new ArrayList<>(DbHelper.cursorToTrips(cursor));
                tripArrayAdapter.clear();
                tripArrayAdapter.addAll(trips);
                tripArrayAdapter.notifyDataSetChanged();
            }
            dismissProgressDialog();
        }
    }

    private class TripListAdapter extends ArrayAdapter<Trip> {

        private Context  context;
        private TextView tripTitle;

        public TripListAdapter(@NonNull final Context context, final int resource, @NonNull final List<Trip> objects) {
            super(context, resource, objects);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.widget_list_trip, null);
            }
            tripTitle = view.findViewById(R.id.widget_list_trip_item);
            tripTitle.setText(trips.get(position).title);

            return view;
        }
    }

}
