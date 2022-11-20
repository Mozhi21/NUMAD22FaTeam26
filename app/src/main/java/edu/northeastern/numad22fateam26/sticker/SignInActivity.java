package edu.northeastern.numad22fateam26.sticker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import edu.northeastern.numad22fateam26.R;

public class SignInActivity extends AppCompatActivity {

    private static String TAG = "SignInActivity";
    private static String DEFAULT_EMAIL_SUFFIX = "@group26.com";
    private static String DEFAULT_PASSWORD = "group26";
    private static String DATABASE_USERS = "users";
    private static String DATABASE_USERNAME = "username";
    private static String DATABASE_FCMToken = "FCMToken";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseMessaging mMessageing;
    private TextInputLayout usernameInput;

    private static String wrapWithEmailSuffix(String username) {
        return username + DEFAULT_EMAIL_SUFFIX;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mMessageing = FirebaseMessaging.getInstance();
        usernameInput = findViewById(R.id.username);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            transitionToSocialMediaActivity();
        }
    }

    public void login(View view) {
        String username = usernameInput.getEditText().getText().toString();
        signIn(username);
    }

    private void signIn(String username) {
        mAuth.signInWithEmailAndPassword(wrapWithEmailSuffix(username), DEFAULT_PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            updateUserTokenInDatabase(uid, username);
                            transitionToSocialMediaActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            signUp(username);
                        }
                    }
                });
    }

    private void signUp(String username) {
        mAuth.createUserWithEmailAndPassword(wrapWithEmailSuffix(username), DEFAULT_PASSWORD)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            createUserInDatabase(uid, username);
                            transitionToSocialMediaActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, String.format("Authentication failed: %s", task.getException().getMessage()),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //transit to city category activity if sign in successfully
    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(this, StickerActivity.class);
        startActivity(intent);
    }

    private void createUserInDatabase(String uid, String username) {
        mMessageing.getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        DatabaseReference userRef = mDatabase.getReference(DATABASE_USERS).child(uid);
                        userRef.child(DATABASE_USERNAME).setValue(username);
                        userRef.child(DATABASE_FCMToken).setValue(token);
                    }
                });
    }

    private void updateUserTokenInDatabase(String uid, String username) {
        mMessageing.getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();

                        mDatabase.getReference(DATABASE_USERS).child(uid).child(DATABASE_FCMToken).setValue(token);
                    }
                });
    }
}
