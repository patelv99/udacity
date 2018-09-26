package com.vish.travelbook.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.vish.travelbook.HomeActivity;
import com.vish.travelbook.R;
import com.vish.travelbook.TripDetailActivity;
import com.vish.travelbook.model.ItineraryEvent;
import com.vish.travelbook.model.Trip;
import com.vish.travelbook.utils.DateTimeUtils;
import org.joda.time.DateTime;

import static com.vish.travelbook.TripDetailActivity.TRIP_KEY;
import static com.vish.travelbook.notifications.NotificationService.NOTIFICATION;
import static com.vish.travelbook.notifications.NotificationService.NOTIFICATION_CHANNEL_ID;
import static com.vish.travelbook.notifications.NotificationService.NOTIFICATION_ID;

public class NotificationUtils {

    /**
     * Schedule a notification for a trip
     */
    public static void scheduleTripNotification(Context context, Trip trip) {
        Intent intent = new Intent(context, TripDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(TRIP_KEY, trip);
        PendingIntent activity = PendingIntent.getActivity(context, trip.id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(context.getString(R.string.trip_notification_title))
                .setContentText(context.getString(R.string.trip_notification_message, trip.title))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(activity);
        Notification notification = notificationBuilder.build();

        Intent notificationIntent = new Intent(context, NotificationService.class);
        notificationIntent.putExtra(NOTIFICATION_ID, trip.id);
        notificationIntent.putExtra(NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, DateTimeUtils.getTripNotificationTime(trip.startDate).getMillis(), pendingIntent);
        Log.i(NotificationUtils.class.getSimpleName(), "Notification scheduled for: " + trip.title);
    }

    /**
     * Schedule a notification for an event
     */
    public static void scheduleEventNotification(Context context, ItineraryEvent event, Trip trip) {
        Intent intent = new Intent(context, TripDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(TRIP_KEY, trip);
        PendingIntent activity = PendingIntent.getActivity(context, event.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
            new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(context.getString(R.string.event_notification_title))
                .setContentText(context.getString(R.string.event_notification_message, event.title))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(activity);
        Notification notification = notificationBuilder.build();

        Intent notificationIntent = new Intent(context, NotificationService.class);
        notificationIntent.putExtra(NOTIFICATION_ID, event.hashCode());
        notificationIntent.putExtra(NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, event.hashCode(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, DateTimeUtils.getEventNotificationTime(event.startTime).getMillis(), pendingIntent);
        Log.i(NotificationUtils.class.getSimpleName(), "Notification scheduled for: " + event.title);
    }

    /**
     * Cancel a notification for a trip
     */
    public static void cancelTripNotification(Context context, Trip trip) {
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, trip.id, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.i(NotificationUtils.class.getSimpleName(), "Notification canceled for: " + trip.title);
    }

    /**
     * Cancel a notification for an event
     */
    public static void cancelEventNotification(Context context, ItineraryEvent event) {
        Intent intent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, event.hashCode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.i(NotificationUtils.class.getSimpleName(), "Notification canceled for: " + event.title);
    }

}
