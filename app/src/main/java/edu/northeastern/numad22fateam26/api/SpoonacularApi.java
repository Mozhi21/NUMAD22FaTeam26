package edu.northeastern.numad22fateam26.api;

import java.util.List;

import edu.northeastern.numad22fateam26.model.ListRecipesResponse;
import edu.northeastern.numad22fateam26.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface SpoonacularApi {

    @GET("/recipes/complexSearch?apiKey=15aae58e3c2e4b6ebb17a1333ecac595&sort=calories&sortDirection=desc")
    Call<ListRecipesResponse> listRecipes(@Query("query") String query,
                                          @Query("maxCalories")int maxCalories,
                                          @Query("number")int recipeNumber,
                                          @Query("diet") String diet);
}
