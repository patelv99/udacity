package com.vish.travelbook.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import com.vish.travelbook.R;

public class NotificationService extends BroadcastReceiver {

    public static String NOTIFICATION_CHANNEL_ID = "channel_id";
    public static String NOTIFICATION_ID         = "notification_id";
    public static String NOTIFICATION            = "notification";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                                                                  context.getString(R.string.notification_channel_name),
                                                                  NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);

        notificationManager.notify(notificationId, notification);
    }
}
