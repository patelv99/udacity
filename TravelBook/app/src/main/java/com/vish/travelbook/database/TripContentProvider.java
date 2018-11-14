package com.vish.travelbook.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.vish.travelbook.database.TripContract.TripEntry;

import static com.vish.travelbook.database.TripContract.TripEntry.TABLE_NAME;

public class TripContentProvider extends ContentProvider {

    public static final int TRIP_LOADER_ID = 1;
    public static final int TRIPS        = 100;
    public static final int TRIP_WITH_ID = 101;

    private static final UriMatcher URI_MATCHER = buildUriMatcher();
    private              DbHelper   dbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TripContract.AUTHORITY, TripContract.PATH_TRIPS, TRIPS);
        uriMatcher.addURI(TripContract.AUTHORITY, TripContract.PATH_TRIPS + "/#", TRIP_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        Log.i(this.getClass().getSimpleName(), "DB HELPER CREATED");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        int match = URI_MATCHER.match(uri);
        Cursor cursor;
        switch (match) {
            case TRIPS:
                cursor = database.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TRIP_WITH_ID:
                cursor = database.query(TABLE_NAME, projection, "_id=?", new String[] { uri.getPathSegments().get(1) }, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        Uri uriToReturn;
        switch (match) {
            case TRIPS:
                long id = database.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    uriToReturn = ContentUris.withAppendedId(TripEntry.CONTENT_URI, id);
                } else {
                    throw new SQLException("Failed to insert " + values.get(TripEntry.COLUMN_TRIP_TITLE) + " into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uriToReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        int moviesDeleted;
        switch (match) {
            case TRIP_WITH_ID:
                moviesDeleted = db.delete(TABLE_NAME, "_id=?", new String[] { uri.getPathSegments().get(1) });
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = URI_MATCHER.match(uri);
        int moviesUpdated;
        switch (match) {
            case TRIP_WITH_ID:
                moviesUpdated = db.update(TABLE_NAME, values, "_id=?", new String[] { uri.getPathSegments().get(1)});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        if (moviesUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return moviesUpdated;
    }
}
