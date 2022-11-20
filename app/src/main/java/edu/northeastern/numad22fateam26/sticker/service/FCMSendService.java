package edu.northeastern.numad22fateam26.sticker.service;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

public class FCMSendService {
    private static final String SERVER_KEY = "key=AAAA3CYsa5o:APA91bH1nIvPXwnVUIjXjjwDHIWChJg21gksjWkRDvalw-vFYO3nwBPtlRH3PtA5cNsBq-KCkq_p-CFbXgeYfB3u0Ty89qjbM9u5NYh6cI_GGnb_kOvX4uUfYQaaf0QmHhxD0uLVt4-f";
    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String TAG = "FCMSendService: ";

    public static void sendNotification(Context context, String token, String title, String message, String stickerId) {
//        if (BuildConfig.DEBUG) {
//            //if debugging, turn on strict mode
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectAll()   // for all detectable problem
//                    .penaltyLog()
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build());
//        }
        Log.v(TAG, token + "\n" + title + "\n" + message + "\n" + stickerId);

        RequestQueue queue = Volley.newRequestQueue(context);

        try {
            // user token
            JSONObject json = new JSONObject();
            json.put("to", token);
            // notification display
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", message);
            json.put("notification", notification);
            // sticker from resource folder
            JSONObject data = new JSONObject();
            data.put("stickerId", stickerId);
            json.put("data", data);

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
