package edu.northeastern.numad22fateam26;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import edu.northeastern.numad22fateam26.model.Recipe;

public class SpoonacularRecipeActivity extends AppCompatActivity {

    RecyclerView recyclerViewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoonacular_recipe);
        recyclerViewRecipe = findViewById(R.id.recyclerRecipe);

        List<Recipe> recipes = new LinkedList<>();
        //[{"id":653238,"title":"Noodle Free Eggplant and Spinach Lasagna","image":"https://spoonacular.com/recipeImages/653238-312x231.jpg","imageType":"jpg"},
        //recipes.add(new Recipe(653238, "Title1", "https://spoonacular.com/recipeImages/653238-312x231.jpg", "jpg"));
        // {"id":653234,"title":"Noodle Kugel with Pineapple-Gluten free, Dairy Free","image":"https://spoonacular.com/recipeImages/653234-312x231.jpg","imageType":"jpg"},
        //recipes.add(new Recipe(653234,"Title2","https://spoonacular.com/recipeImages/653234-312x231.jpg","jpg"));
        // {"id":660485,"title":"Soba Noodle & Five-Spice Pork Salad","image":"https://spoonacular.com/recipeImages/660485-312x231.jpg","imageType":"jpg"},
        //recipes.add(new Recipe(660485,"Title3","https://spoonacular.com/recipeImages/660485-312x231.jpg","jpg"));
        // {"id":663942,"title":"Tuna Noodle Casserole: Mommie Cooks","image":"https://spoonacular.com/recipeImages/663942-312x231.jpg","imageType":"jpg"},
        //recipes.add(new Recipe(653238, "Title4", "https://spoonacular.com/recipeImages/653238-312x231.jpg", "jpg"));
        //recipes.add(new Recipe(653238, "Title5", "https://spoonacular.com/recipeImages/653238-312x231.jpg", "jpg"));
        //recipes.add(new Recipe(653238, "Title6", "https://spoonacular.com/recipeImages/653238-312x231.jpg", "jpg"));
        //recipes.add(new Recipe(653238, "Title7", "https://spoonacular.com/recipeImages/653238-312x231.jpg", "jpg"));
        //recipes.add(new Recipe(653238, "Title8", "https://spoonacular.com/recipeImages/653238-312x231.jpg", "jpg"));
        //recipes.add(new Recipe(653238, "Title9", "https://raw.githubusercontent.com/haerulmuttaqin/FoodsApp-starting-code/549ee24881e3ada5510089b9bd6ffad949e49ff5/app/src/main/res/drawable/sample_image_category.png", "png"));
        //recipes.add(new Recipe(653238, "Title9", "https://raw.githubusercontent.com/haerulmuttaqin/FoodsApp-starting-code/549ee24881e3ada5510089b9bd6ffad949e49ff5/app/src/main/res/drawable/sample_image_category.png", "png"));
        recipes.add(new Recipe(653238, "Title9", "https://raw.githubusercontent.com/haerulmuttaqin/FoodsApp-starting-code/549ee24881e3ada5510089b9bd6ffad949e49ff5/app/src/main/res/drawable/sample_image_category.png", "png"));

        // {"id":660494,"title":"Soba Noodle Salad with Avocado and Mango","image":"https://spoonacular.com/recipeImages/660494-312x231.jpg","imageType":"jpg"},
        // {"id":658269,"title":"Rice noodle salad with sesame oil dressing","image":"https://spoonacular.com/recipeImages/658269-312x231.jpg","imageType":"jpg"}]

        //showLoading();
        hideLoading();
        setRecipe(recipes);
    }


    private void showLoading() {
        findViewById(R.id.shimmerRecipe).setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        findViewById(R.id.shimmerRecipe).setVisibility(View.GONE);
    }

    public void setRecipe(List<Recipe> Recipe) {
        RecyclerViewRecipeAdapter recipeAdapter = new RecyclerViewRecipeAdapter(Recipe, this);
        recyclerViewRecipe.setAdapter(recipeAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewRecipe.setLayoutManager(layoutManager);
        recyclerViewRecipe.setNestedScrollingEnabled(true);
        recipeAdapter.notifyDataSetChanged();

//        homeAdapter.setOnItemClickListener((view, position) -> {
//            Intent intent = new Intent(this, RecipeActivity.class);
//            intent.putExtra(EXTRA_Recipe, (Serializable) Recipe);
//            intent.putExtra(EXTRA_POSITION, position);
//            startActivity(intent);
//        });
    }
}