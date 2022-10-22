package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SpoonacularSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoonacular_search);
    }

    public void back(View view){
        SpoonacularSearchActivity.super.onBackPressed();
    }

    public void startSearch(View view) {
        startActivity(new Intent(SpoonacularSearchActivity.this, SpoonacularRecipeActivity.class));
    }
}