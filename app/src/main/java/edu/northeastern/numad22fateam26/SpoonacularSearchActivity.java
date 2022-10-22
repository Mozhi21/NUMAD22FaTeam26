package edu.northeastern.numad22fateam26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import butterknife.BindView;

public class SpoonacularSearchActivity extends AppCompatActivity {
    Slider slider;
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