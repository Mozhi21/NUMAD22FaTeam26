package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import edu.northeastern.numad22fateam26.R;

public class RecipeItem extends AppCompatActivity {
    String [] steps = {"step1","steps2","steps3"};
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_item);

        listView = findViewById(R.id.detailed_recipe);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_recipe_item, R.id.recipe_steps, steps);
        listView.setAdapter(arrayAdapter);
    }
}

