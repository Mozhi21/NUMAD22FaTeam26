package edu.northeastern.numad22fateam26.spoonacular.api;

import edu.northeastern.numad22fateam26.model.ListRecipesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularApi {

    @GET("/recipes/complexSearch?apiKey=56fee42fb92a4507a62612b3d08495ae&sort=calories&sortDirection=desc")
    Call<ListRecipesResponse> listRecipes(@Query("query") String query,
                                          @Query("maxCalories") int maxCalories,
                                          @Query("number") int recipeNumber,
                                          @Query("diet") String diet);
}
