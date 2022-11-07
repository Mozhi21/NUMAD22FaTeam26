//package edu.northeastern.numad22fateam26;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.messaging.FirebaseMessaging;
//
//import edu.northeastern.numad22fateam26.service.FCMSendService;
//
//public class FCMActivity extends AppCompatActivity {
//
//    private static final String SERVER_KEY = "key=AAAA3CYsa5o:APA91bH1nIvPXwnVUIjXjjwDHIWChJg21gksjWkRDvalw-vFYO3nwBPtlRH3PtA5cNsBq-KCkq_p-CFbXgeYfB3u0Ty89qjbM9u5NYh6cI_GGnb_kOvX4uUfYQaaf0QmHhxD0uLVt4-f";
//    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
//    private static final String TAG = "FCMActivity";
//    private static final String TOKEN = "DEVICE TOKEN: ";
//    private EditText notification_title, notification_message, userToken;
//
////    private final ActivityResultLauncher<String> requestPermissionLauncher =
////            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
////                if (isGranted) {
////                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT)
////                            .show();
////                } else {
////                    Toast.makeText(this, "Notification permission is required to proceed in this activity. ",
////                                    Toast.LENGTH_LONG).show();
////                }
////            });
//
//    //    private void askNotificationPermission() {
////        // Necessary for API level >= 33
////        if(Build.VERSION.SDK_INT >= 33) {
////            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
////            PackageManager.PERMISSION_GRANTED ) {
////                Toast.makeText(this, "Already having permission", Toast.LENGTH_SHORT)
////                        .show();
////            } else {
////                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
////            }
////        }
////    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fcmactivity);
////        askNotificationPermission(); // not necessary for SDK < 33
//
//        // Get device id
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//                        Log.i(TOKEN, token);
//                    }
//                });
//        notification_title = findViewById(R.id.notification_title);
//        notification_message = findViewById(R.id.notification_message);
//        userToken = findViewById(R.id.user_token_send_to);
//    }
//
//    public void sendNotification(View view) {
//        String title = notification_title.getText().toString().trim();
//        String message = notification_message.getText().toString().trim();
//        String userToken = "faBpwDn6SwOkLGG-k6zf1n:APA91bGHG24-vO2o-KSvIkXNEs1I6Y0Q59GUVuYGA7z1dov52tWib3kc-2p-zB9X5EdPPgBVjPtAHKdMFoFhURC-Pt97f-7bvrEyvWyg7PfBEV083sXlW0dMgxbScSSGqdzGM0ueYaqw";
//
//        String userToken2 = "d9FiXv6bRTusJ5UBlre8gd:APA91bG7HNCjFLkoafsyy6EHCwqCjx4qInt1Fhl07g3_P-MxXgx7k7E91g4zvJ2I-FK9EzK7YezSJDVv8za-iOPRY5tyHJ0GcfY1nzgQRLrmymMP07vdv0NM6ZapZT62alkkAyZZTl3s";
//        Log.v(TAG, "sent");
//        if (title.length() != 0  & message.length() != 0) {
//            FCMSendService.sendNotification(
//                    this,
//                    userToken2,
//                    title,
//                    message
//                    );
//        }
//    }
//}