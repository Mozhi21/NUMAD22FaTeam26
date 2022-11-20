package edu.northeastern.numad22fateam26.sticker.service;

import static java.lang.String.valueOf;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.sticker.StickerHistoryActivity;

public class FCMReceiveService extends FirebaseMessagingService {
    private static final String TAG = "FCM Receive Service ";
    private static final int NOTIFICATION_UNIQUE_ID = 101;
    private static final String CHANNEL_ID_MESSAGE = "MESSAGE";
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // create customized channel
        createNotificationChannel();

        // check notification payload
        if (remoteMessage.getNotification() != null) {
            Log.v(TAG, remoteMessage.getNotification().getBody());
        }

        // check data payload
        if (remoteMessage.getData().size() > 0) {
            Log.v(TAG, valueOf(remoteMessage.getData()));
        }

        // Handling FCM messages here
        String title = remoteMessage.getNotification().getTitle();
        String textMessage = remoteMessage.getNotification().getBody();
//        Bitmap bitmap = getBitmapFromUrl(Objects.requireNonNull(remoteMessage.getNotification().getImageUrl()).toString());
//        createNotificationWithUrl();

        // handling pre-defined stickers in resources
        String stickerId = remoteMessage.getData().get("stickerId");
        int stickerResId = getResources().getIdentifier(stickerId, "drawable", getPackageName());
        Bitmap stickerBitmap = BitmapFactory.decodeResource(getResources(), stickerResId);


        Intent activityIntent = new Intent(this, StickerHistoryActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, activityIntent, PendingIntent.FLAG_MUTABLE);


        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID_MESSAGE)
                .setContentTitle(title)
                .setContentText(textMessage)

                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        // set LargeIcon
        if (stickerBitmap != null) {
            notification.setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat
                            .BigPictureStyle()
                            .bigPicture(stickerBitmap)
                            .bigLargeIcon(null))
                    .setLargeIcon(stickerBitmap);
        }

        // add action bar
//        notification.addAction(android.R.mipmap.sym_def_app_icon, "REPLY", pendingIntent);
        notification.addAction(R.mipmap.ic_launcher, "Take a look", pendingIntent);

        // build the notification
        NotificationManagerCompat.from(this).notify(NOTIFICATION_UNIQUE_ID, notification.build());

        super.onMessageReceived(remoteMessage);
    }

//    private void createNotificationWithUrl(String title, String textMessage, Bitmap bitmap) {
//        Intent activityIntent = new Intent(this, StickerHistoryActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0, activityIntent,PendingIntent.FLAG_MUTABLE);
//
//
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID_MESSAGE)
//                .setContentTitle(title)
//                .setContentText(textMessage)
//                .setSmallIcon(android.R.drawable.stat_notify_chat)
//                .setStyle(new NotificationCompat
//                        .BigPictureStyle()
//                        .bigPicture(bitmap)
//                        .bigLargeIcon(null))
//                .setLargeIcon(bitmap)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent);
//
//        // add action bar
////        notification.addAction(android.R.mipmap.sym_def_app_icon, "REPLY", pendingIntent);
//        notification.addAction(android.R.drawable.stat_notify_chat, "Take a look", pendingIntent);
//
//        // build the notification
//        NotificationManagerCompat.from(this).notify(NOTIFICATION_UNIQUE_ID,notification.build());
//    }

//    public Bitmap getBitmapFromUrl(String imageUrl) {
//        try {
//            URL url = new URL(imageUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
//            connection.connect();
//            InputStream input = connection.getInputStream();
//            return BitmapFactory.decodeStream(input);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//
//        }
//    }


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


