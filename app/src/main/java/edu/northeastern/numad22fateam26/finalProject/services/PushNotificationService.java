package edu.northeastern.numad22fateam26.finalProject.services;


import static java.lang.String.valueOf;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.finalProject.ExploreActivity;
import edu.northeastern.numad22fateam26.finalProject.chat.ChatUsersActivity;
import edu.northeastern.numad22fateam26.finalProject.fragments.Notification;
import edu.northeastern.numad22fateam26.sticker.StickerHistoryActivity;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class PushNotificationService extends FirebaseMessagingService {
    private static final String TAG = "Push Notification";
    private static final String CHANNEL_ID_MESSAGE = "MESSAGE";
    private static final int NOTIFICATION_UNIQUE_ID = 101;
    private static final String SERVER_KEY = "key=AAAA3CYsa5o:APA91bH1nIvPXwnVUIjXjjwDHIWChJg21gksjWkRDvalw-vFYO3nwBPtlRH3PtA5cNsBq-KCkq_p-CFbXgeYfB3u0Ty89qjbM9u5NYh6cI_GGnb_kOvX4uUfYQaaf0QmHhxD0uLVt4-f";
    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
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

        Intent activityIntent = new Intent(this, ExploreActivity.class);
        if (!title.equals("Awesome")) {
            activityIntent = new Intent(this, ChatUsersActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_MUTABLE);


        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID_MESSAGE)
                .setContentTitle(title)
                .setContentText(textMessage)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher);

        notification.addAction(R.mipmap.ic_launcher, "Take a look", pendingIntent);

        // build the notification
        NotificationManagerCompat.from(this).notify(NOTIFICATION_UNIQUE_ID, notification.build());
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

    public static void sendNotification(Context context, String receiverToken, String title, String message) {
        Log.v(TAG, receiverToken + "\n" + title + "\n" + message + "\n");

        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            // user token
            JSONObject json = new JSONObject();
            json.put("to", receiverToken);
            // notification display
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", message);
            json.put("notification", notification);

            // reference: https://www.json.org/JSONRequest.html
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.v(TAG, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.getMessage());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return Map.of(
                            "Content-Type", "application/json",
                            "Authorization", SERVER_KEY);
                }
            };

            queue.add(jsonObjectRequest);
            Log.v(TAG, "queued");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
