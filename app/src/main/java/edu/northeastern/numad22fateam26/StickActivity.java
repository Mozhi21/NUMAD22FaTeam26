package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);
    }

    public void toFCMActivity(View view){
        startActivity(new Intent(StickActivity.this, FCMActivity.class));
    }
}