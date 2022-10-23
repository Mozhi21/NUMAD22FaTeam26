package edu.northeastern.numad22fateam26.model;

public class ListRecipesRequest {
    String query;
    int maxCalories;
    int recipeNumbers;
    String diet;

    public String getQuery() {
        return query;
    }

    public int getMaxCalories() {
        return maxCalories;
    }

    public int getRecipeNumbers() {
        return recipeNumbers;
    }

    public String getDiet() {
        return diet;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setMaxCalories(int maxCalories) {
        this.maxCalories = maxCalories;
    }

    public void setRecipeNumbers(int recipeNumbers) {
        this.recipeNumbers = recipeNumbers;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }
}
