package edu.northeastern.numad22fateam26.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMReceiveService extends FirebaseMessagingService {
    private static final int NOTIFICATION_UNIQUE_ID = 101;
    private static final String CHANNEL_ID_MESSAGE = "MESSAGE";
    private NotificationManager notificationManager;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        createNotificationChannel();
        // Handling FCM messages here
        String title = remoteMessage.getNotification().getTitle();
        String text = remoteMessage.getNotification().getBody();

        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID_MESSAGE)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.star_big_on)
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(NOTIFICATION_UNIQUE_ID,notification.build());

        super.onMessageReceived(remoteMessage);
    }

    private void createNotificationChannel() {
        // check if compatible fore Oreo and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Message Notification"; // a readable sequence of char values.
            int importance = NotificationManager.IMPORTANCE_HIGH; // urgent: Makes a sound and appears as a heads-up notification
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_MESSAGE,
                    name,
                    importance);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }
}
