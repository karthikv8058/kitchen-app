package com.smarttoni.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smarttoni.entities.Tag;

import java.util.List;

@Dao
public interface TagDao {
    @Insert
    void insertAll(List<Tag> users);

    @Query("DELETE FROM tag WHERE id IN (:tagIds)")
    void deleteById(List<String> tagIds);
}
