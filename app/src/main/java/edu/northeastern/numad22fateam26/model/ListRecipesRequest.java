package edu.northeastern.numad22fateam26.model;

public class ListRecipesRequest {
    String query;
    int maxCalories;

    public String getQuery() {
        return query;
    }

    public int getMaxCalories() {
        return maxCalories;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setMaxCalories(int maxCalories) {
        this.maxCalories = maxCalories;
    }
}
