package com.smarttoni.models.wrappers;

import java.util.List;

public class MealWrapper {

    String mealId;
    public List<RecipeWrapper> recipes;

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public List<RecipeWrapper> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<RecipeWrapper> recipes) {
        this.recipes = recipes;
    }
}
