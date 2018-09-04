package com.vish.travelbook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.vish.travelbook.database.TripContract.TripEntry;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "trips.db";
    private static final int    DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + TripEntry.TABLE_NAME + " (" +
                                                 TripEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                 TripEntry.COLUMN_TRIP_TITLE + " TEXT NOT NULL, " +
                                                 TripEntry.COLUMN_TRIP_START + " INTEGER NOT NULL, " +
                                                 TripEntry.COLUMN_TRIP_END + " INTEGER NOT NULL, " +
                                                 TripEntry.COLUMN_TRIP_IMAGE + " TEXT NOT NULL" +
                                                 "); ";
        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
        Log.i(this.getClass().getSimpleName(), "DATABASE CREATED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TripEntry.TABLE_NAME);
        onCreate(db);
    }
}
