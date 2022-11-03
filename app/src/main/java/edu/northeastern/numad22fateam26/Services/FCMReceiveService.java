package edu.northeastern.numad22fateam26.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMReceiveService extends FirebaseMessagingService {
    private static final int NOTIFICATION_UNIQUE_ID = 101;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // Handling FCM messages here
        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();
        String CHANNEL_ID = "MESSAGE";
        CharSequence name; // a readable sequence of char values.
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Message Notification",
                NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Context context;
        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.star_big_on)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(NOTIFICATION_UNIQUE_ID,notification.build());

        super.onMessageReceived(remoteMessage);
    }
}
