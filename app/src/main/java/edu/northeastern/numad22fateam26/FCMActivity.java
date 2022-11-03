package edu.northeastern.numad22fateam26;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class FCMActivity extends AppCompatActivity {

    private static final String SERVER_KEY = "key=AAAA3CYsa5o:APA91bH1nIvPXwnVUIjXjjwDHIWChJg21gksjWkRDvalw-vFYO3nwBPtlRH3PtA5cNsBq-KCkq_p-CFbXgeYfB3u0Ty89qjbM9u5NYh6cI_GGnb_kOvX4uUfYQaaf0QmHhxD0uLVt4-f";
    private static final String TAG = "FCMActivity";

//    private final ActivityResultLauncher<String> requestPermissionLauncher =
//            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                if (isGranted) {
//                    Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT)
//                            .show();
//                } else {
//                    Toast.makeText(this, "Notification permission is required to proceed in this activity. ",
//                                    Toast.LENGTH_LONG).show();
//                }
//            });

    //    private void askNotificationPermission() {
//        // Necessary for API level >= 33
//        if(Build.VERSION.SDK_INT >= 33) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
//            PackageManager.PERMISSION_GRANTED ) {
//                Toast.makeText(this, "Already having permission", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
//            }
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcmactivity);
//        askNotificationPermission();

        // Device id

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        System.out.println("TOKEN: " + token);
                        // Log and toast
//                    String msg = getString(R.string.msg_token_fmt, token);
                    }
                });




    }

}