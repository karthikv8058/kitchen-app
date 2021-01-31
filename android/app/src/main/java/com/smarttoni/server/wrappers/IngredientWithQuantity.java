package com.smarttoni.server.wrappers;

import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Units;


public class IngredientWithQuantity {

    private Recipe recipe;

    private String name;

    private String image;

    private String qty;

    private float quantity;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.name = recipe.getName();
        this.image = recipe.getImageUrl();
        this.recipe = recipe;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity, Units unit) {
        qty = quantity+unit.getSymbol();
        this.quantity = quantity;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
