package edu.northeastern.numad22fateam26;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import edu.northeastern.numad22fateam26.api.SpoonacularApi;
import edu.northeastern.numad22fateam26.api.SpoonacularClient;
import edu.northeastern.numad22fateam26.model.ListRecipesRequest;
import edu.northeastern.numad22fateam26.model.ListRecipesResponse;
import edu.northeastern.numad22fateam26.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpoonacularSearchActivity extends AppCompatActivity {
    Slider calorieSlider, numberSlider;
    Gson gson;
    EditText dishText;
    CheckBox vegancheck, glutencheck, diarycheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoonacular_search);
        gson = new Gson();
        dishText = findViewById(R.id.editTextTextDishName);
        vegancheck = findViewById(R.id.veganCheckBox);
        glutencheck = findViewById(R.id.glutenCheckBox);
        diarycheck = findViewById(R.id.diaryCheckBox);
        calorieSlider = findViewById(R.id.range_slider);
        numberSlider = findViewById(R.id.number_slider);
    }

    public void back(View view){
        SpoonacularSearchActivity.super.onBackPressed();
    }



    public void startSearch(View view) {
        ListRecipesRequest request = new ListRecipesRequest();
        request.setQuery(dishText.getText().toString());
        request.setMaxCalories((int)calorieSlider.getValue());
        request.setRecipeNumbers((int)numberSlider.getValue());

        Intent intent = new Intent(SpoonacularSearchActivity.this, SpoonacularRecipeActivity.class);
        intent.putExtra("request", gson.toJson(request));
        startActivity(intent);
    }
}