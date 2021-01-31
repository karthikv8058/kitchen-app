package com.smarttoni.pos.parser.Parser;

public interface Order {
    void addCourse();

    void addMeal();

    void addRecipe();

    void setRecipeAmount();

    void setOrderTime();
}
