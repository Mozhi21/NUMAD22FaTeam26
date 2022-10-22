package edu.northeastern.numad22fateam26.view.category;
import edu.northeastern.numad22fateam26.model.Meals;

import java.util.List;

public interface CategoryView {
    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meals);
    void onErrorLoading(String message);
}
