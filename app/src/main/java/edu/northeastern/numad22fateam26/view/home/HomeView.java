package edu.northeastern.numad22fateam26.view.home;

import edu.northeastern.numad22fateam26.model.Categories;
import edu.northeastern.numad22fateam26.model.Meals;

import java.util.List;

public interface HomeView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> meal);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}