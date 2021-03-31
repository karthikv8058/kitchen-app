package com.smarttoni.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.smarttoni.entities.InventoryRequest;
import com.smarttoni.entities.Supplier;

import java.util.List;

@Dao
public interface InventoryRequestDao {


    @Query("SELECT *  FROM inventoryrequest")
    List<InventoryRequest> listInventoryRequests();

    @Query("SELECT *  FROM inventoryrequest WHERE orderId = :orderId")
    List<InventoryRequest> listInventoryRequestByOrder(String orderId);


    @Query("DELETE FROM inventoryrequest WHERE recipeId = :recipe AND orderId = :order")
    void delete(String order,String recipe);

    @Insert
    void insertAll(InventoryRequest... inventoryRequests);
}