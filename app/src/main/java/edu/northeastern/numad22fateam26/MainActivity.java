package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toSearchActivity(View view){
        startActivity(new Intent(MainActivity.this, SpoonacularSearchActivity.class));
    }
    public void toSignInActivity(View view){
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
    }
    public void toAboutActivity(View view){
        startActivity(new Intent(MainActivity.this, AboutActivity.class));
    }
}