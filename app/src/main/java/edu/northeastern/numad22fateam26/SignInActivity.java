package edu.northeastern.numad22fateam26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null) {
//            transitionToSocialMediaActivity();
//        }
//    }
//
//    // read users' input and respond accordingly
//    private void signIn() {
//        mAuth.signInWithEmailAndPassword(username.getEditText().getText().toString(), password.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Toast.makeText(SigninActivity.this, "Sign In Succeed", Toast.LENGTH_LONG).show();
//                    transitionToSocialMediaActivity();
//                } else {
//                    Toast.makeText(SigninActivity.this, "Sign In Failed", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

    //transit to city category activity if sign in successfully
    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}