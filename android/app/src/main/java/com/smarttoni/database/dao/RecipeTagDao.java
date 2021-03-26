package com.smarttoni.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smarttoni.entities.RecipeTag;
import com.smarttoni.entities.Tag;

import java.util.List;

@Dao
public interface RecipeTagDao {

    @Query("DELETE FROM recipeTag WHERE recipe = :recipeId")
    void deleteRecipeTagsByRecipeId(String recipeId);

    @Insert
    void insertAll(RecipeTag... recipeTags);

}
