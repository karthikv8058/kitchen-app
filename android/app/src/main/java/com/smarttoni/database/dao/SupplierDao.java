package com.smarttoni.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smarttoni.entities.RecipeTag;
import com.smarttoni.entities.Supplier;
import com.smarttoni.entities.Tag;

import java.util.List;

@Dao
public interface SupplierDao {

    @Query("SELECT *  FROM supplier")
    List<Supplier> listSupplier();

    @Query("DELETE FROM supplier WHERE id IN (:tagIds)")
    void deleteById(List<String> tagIds);

    @Query("SELECT *  FROM supplier WHERE id = :supplierId")
    Supplier getSupplierById(String supplierId);

    @Insert
    void insertAll(List<Supplier> suppliers);

}
