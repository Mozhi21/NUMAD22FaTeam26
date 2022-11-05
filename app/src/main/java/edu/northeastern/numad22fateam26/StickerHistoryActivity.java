package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StickerHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_history);
    }
    public void back(View view){
        startActivity(new Intent(StickerHistoryActivity.this, StickActivity.class));
    }

}