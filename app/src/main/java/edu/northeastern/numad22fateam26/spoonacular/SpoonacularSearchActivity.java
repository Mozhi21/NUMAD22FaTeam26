package edu.northeastern.numad22fateam26.spoonacular;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.model.ListRecipesRequest;

public class SpoonacularSearchActivity extends AppCompatActivity {
    Slider calorieSlider, numberSlider;
    Gson gson;
    EditText dishText;
    CheckBox veganCheck, glutenCheck, ketoCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoonacular_search);
        gson = new Gson();
        dishText = findViewById(R.id.editTextTextDishName);
        veganCheck = findViewById(R.id.veganCheckBox);
        glutenCheck = findViewById(R.id.glutenCheckBox);
        ketoCheck = findViewById(R.id.ketoCheckBox);
        calorieSlider = findViewById(R.id.range_slider);
        numberSlider = findViewById(R.id.number_slider);
    }

    public void back(View view) {
        SpoonacularSearchActivity.super.onBackPressed();
    }


    public void startSearch(View view) {
        ListRecipesRequest request = new ListRecipesRequest();
        request.setQuery(dishText.getText().toString());
        request.setMaxCalories((int) calorieSlider.getValue());
        request.setRecipeNumbers((int) numberSlider.getValue());
        List<String> diet = new LinkedList<>();
        if (veganCheck.isChecked()) diet.add("Vegan");
        if (glutenCheck.isChecked()) diet.add("Gluten Free");
        if (ketoCheck.isChecked()) diet.add("Ketogenic");
        request.setDiet(String.join(",", diet));

        Intent intent = new Intent(SpoonacularSearchActivity.this, SpoonacularRecipeActivity.class);
        intent.putExtra("request", gson.toJson(request));
        startActivity(intent);
    }
}