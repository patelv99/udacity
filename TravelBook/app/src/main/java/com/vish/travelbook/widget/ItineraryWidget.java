package com.vish.travelbook.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vish.travelbook.R;
import com.vish.travelbook.TripDetailActivity;
import com.vish.travelbook.model.ItineraryEvent;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeTypeAdapter;
import com.vish.travelbook.utils.DateTimeUtils;
import org.joda.time.DateTime;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;

public class ItineraryWidget extends AppWidgetProvider {

    private Trip trip;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(context, ItineraryWidget.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            try {
                Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter()).create();

                String tripJson = PreferenceManager.getDefaultSharedPreferences(context).getString(Integer.toString(widgetId), "");
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_itinerary);

                ItineraryEvent itineraryEvent = new ItineraryEvent();

                if (!TextUtils.isEmpty(tripJson)) {
                    trip = gson.fromJson(tripJson, Trip.class);
                    itineraryEvent = trip.events.get(0);
                }

                Intent intent = new Intent(context, TripDetailActivity.class);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                if (!TextUtils.isEmpty(itineraryEvent.title)) {
                    views.setTextViewText(R.id.widget_itinerary_title, trip.title);
                    views.setTextViewText(R.id.widget_itinerary_event_name, itineraryEvent.title);
                    views.setTextViewText(R.id.widget_itinerary_event_time, DateTimeUtils.getTime(itineraryEvent.startTime));
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setComponent(new ComponentName(context.getPackageName(), TripDetailActivity.class.getName()));
                intent.putExtra(TRIP_KEY, trip);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widget_itinerary, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, views);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context.getApplicationContext(), context.getString(R.string.widget_error_loading_app), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
