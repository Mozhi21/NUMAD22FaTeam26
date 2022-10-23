package edu.northeastern.numad22fateam26.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListRecipesResponse {
    @SerializedName("results")
    List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }
}
