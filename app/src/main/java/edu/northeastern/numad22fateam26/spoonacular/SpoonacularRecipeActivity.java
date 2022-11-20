package edu.northeastern.numad22fateam26.spoonacular;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import edu.northeastern.numad22fateam26.R;
import edu.northeastern.numad22fateam26.spoonacular.api.SpoonacularApi;
import edu.northeastern.numad22fateam26.spoonacular.api.SpoonacularClient;
import edu.northeastern.numad22fateam26.model.ListRecipesRequest;
import edu.northeastern.numad22fateam26.model.ListRecipesResponse;
import edu.northeastern.numad22fateam26.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpoonacularRecipeActivity extends AppCompatActivity {

    RecyclerView recyclerViewRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoonacular_recipe);
        recyclerViewRecipe = findViewById(R.id.recyclerRecipe);

        showLoading();

        Bundle extras = getIntent().getExtras();
        String request_json = extras.getString("request");
        Gson gson = new Gson();
        ListRecipesRequest request = gson.fromJson(request_json, ListRecipesRequest.class);

        SpoonacularApi apiInterface = SpoonacularClient.getClient().create(SpoonacularApi.class);
        Call<ListRecipesResponse> call = apiInterface.listRecipes(request.getQuery(),
                request.getMaxCalories(),
                request.getRecipeNumbers(),
                request.getDiet());
        call.enqueue(new Callback<ListRecipesResponse>() {
            @Override
            public void onResponse(Call<ListRecipesResponse> call, Response<ListRecipesResponse> response) {

                ListRecipesResponse recipesList = response.body();
                if (recipesList == null) {
                    hideLoading();
                } else {
                    List<Recipe> recipes = recipesList.getRecipes();
                    hideLoading();
                    setRecipe(recipes);
                }
            }

            @Override
            public void onFailure(Call<ListRecipesResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void showLoading() {
        findViewById(R.id.shimmerRecipe).setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        findViewById(R.id.shimmerRecipe).setVisibility(View.GONE);
    }

    public void setRecipe(List<Recipe> recipes) {
        RecyclerViewRecipeAdapter recipeAdapter = new RecyclerViewRecipeAdapter(recipes, this);
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

    public void back(View view) {
        SpoonacularRecipeActivity.super.onBackPressed();
    }
}