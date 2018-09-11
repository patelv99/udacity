package com.vish.travelbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vish.travelbook.database.TripContract.TripEntry;
import com.vish.travelbook.model.Expense;
import com.vish.travelbook.model.ItineraryEvent;
import com.vish.travelbook.model.PackingItem;
import com.vish.travelbook.model.Trip;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "trips.db";
    private static final int    DATABASE_VERSION = 1;

    private static Type packingItemTypeToken    = new TypeToken<List<PackingItem>>() {}.getType();
    private static Type itineraryEventTypeToken = new TypeToken<List<ItineraryEvent>>() {}.getType();
    private static Type expenseTypeToken        = new TypeToken<List<Expense>>() {}.getType();

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TRIP_TABLE = "CREATE TABLE " + TripEntry.TABLE_NAME + " (" +
                                             TripEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                             TripEntry.COLUMN_TRIP_TITLE + " TEXT NOT NULL, " +
                                             TripEntry.COLUMN_TRIP_START + " INTEGER NOT NULL, " +
                                             TripEntry.COLUMN_TRIP_END + " INTEGER NOT NULL, " +
                                             TripEntry.COLUMN_TRIP_IMAGE + " TEXT NOT NULL, " +
                                             TripEntry.COLUMN_TRIP_PACKING_LIST + " TEXT," +
                                             TripEntry.COLUMN_TRIP_ITINERARY_LIST + " TEXT, " +
                                             TripEntry.COLUMN_TRIP_EXPENSES + " TEXT " +
                                             "); ";
        db.execSQL(SQL_CREATE_TRIP_TABLE);
        Log.i(this.getClass().getSimpleName(), "DATABASE CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TripEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Create a set of ContentValues from a given Trip
     */
    public static ContentValues createTripContentValues(Trip trip) {
        Gson gson = new Gson();

        ContentValues values = new ContentValues();
        values.put(TripEntry.COLUMN_TRIP_TITLE, trip.title);
        values.put(TripEntry.COLUMN_TRIP_START, trip.startDate.getMillis());
        values.put(TripEntry.COLUMN_TRIP_END, trip.endDate.getMillis());
        values.put(TripEntry.COLUMN_TRIP_IMAGE, trip.getImage());
        if (trip.packingItems != null) {
            values.put(TripEntry.COLUMN_TRIP_PACKING_LIST, gson.toJson(trip.packingItems));
        }
        if (trip.events != null) {
            values.put(TripEntry.COLUMN_TRIP_ITINERARY_LIST, gson.toJson(trip.events));
        }
        if (trip.expenses != null) {
            values.put(TripEntry.COLUMN_TRIP_EXPENSES, gson.toJson(trip.expenses));
        }
        return values;
    }

    /**
     * Convert a Cursor with Trips into a List of Trips
     */
    public static List<Trip> cursorToTrips(Cursor tripsCursor) {
        Gson gson = new Gson();
        List<Trip> trips = new ArrayList<>();
        if (tripsCursor != null) {
            while (tripsCursor.moveToNext()) {
                Trip trip = new Trip();
                trip.id = tripsCursor.getInt(tripsCursor.getColumnIndex(TripEntry._ID));
                trip.title = tripsCursor.getString(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_TITLE));
                trip.startDate = new DateTime(tripsCursor.getLong(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_START)));
                trip.endDate = new DateTime(tripsCursor.getLong(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_END)));
                trip.image = tripsCursor.getString(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_IMAGE));
                String packingItemsString = tripsCursor.getString(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_PACKING_LIST));
                String itineraryEventsString = tripsCursor.getString(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_ITINERARY_LIST));
                String expensesString = tripsCursor.getString(tripsCursor.getColumnIndex(TripEntry.COLUMN_TRIP_EXPENSES));

                if (packingItemsString != null && !packingItemsString.isEmpty()) {
                    ArrayList<PackingItem> packingItems = gson.fromJson(packingItemsString, packingItemTypeToken);
                    trip.packingItems = new ArrayList<>(packingItems);
                }
                if (itineraryEventsString != null && !itineraryEventsString.isEmpty()) {
                    ArrayList<ItineraryEvent> itineraryEvents = gson.fromJson(itineraryEventsString, itineraryEventTypeToken);
                    trip.events = new ArrayList<>(itineraryEvents);
                }
                if (expensesString != null && !expensesString.isEmpty()) {
                    ArrayList<Expense> expenses = gson.fromJson(expensesString, expenseTypeToken);
                    trip.expenses = new ArrayList<>(expenses);
                }
                trips.add(trip);
            }
        }
        return trips;
    }
}
