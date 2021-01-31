package com.smarttoni.pos.models;


import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.SelectedRecipe;

import java.util.List;

public class PosOrderMeal {
    boolean isSelected;

    public List<SelectedRecipe> getSelectedRecipes() {
        return selectedRecipes;
    }

    public void setSelectedRecipes(List<SelectedRecipe> selectedRecipes) {
        this.selectedRecipes = selectedRecipes;
    }

    List<PosRecipe> recipes;
    List<SelectedRecipe> selectedRecipes;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<PosRecipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<PosRecipe> recipes) {
        this.recipes = recipes;
    }
}
