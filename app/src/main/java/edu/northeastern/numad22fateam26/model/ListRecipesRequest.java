package edu.northeastern.numad22fateam26.model;

public class ListRecipesRequest {
    String query;
    int maxCalories;
    int recipeNumbers;
    String diet;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getMaxCalories() {
        return maxCalories;
    }

    public void setMaxCalories(int maxCalories) {
        this.maxCalories = maxCalories;
    }

    public int getRecipeNumbers() {
        return recipeNumbers;
    }

    public void setRecipeNumbers(int recipeNumbers) {
        this.recipeNumbers = recipeNumbers;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }
}
