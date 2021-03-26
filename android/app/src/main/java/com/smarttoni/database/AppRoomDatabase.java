package com.smarttoni.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.smarttoni.BuildConfig;
import com.smarttoni.database.dao.RecipeTagDao;
import com.smarttoni.database.dao.TagDao;
import com.smarttoni.entities.RecipeTag;
import com.smarttoni.entities.Tag;

@Database(entities = {Tag.class,RecipeTag.class}, version = BuildConfig.DB_VERSION)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract TagDao tagDao();
    public abstract RecipeTagDao recipeTagDao();
}