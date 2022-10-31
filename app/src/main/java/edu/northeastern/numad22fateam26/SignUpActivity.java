package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    private void transitionToSocialMediaActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}