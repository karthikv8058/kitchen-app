package com.smarttoni.models;

import com.smarttoni.entities.Inventory;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Room;
import java.util.List;

public class InventoryList {
    public List<Recipe> Recipes;
    public List<Order> orders;
    public List<Order> externalOrders;

    public List<Room> rooms;
    public List<Inventory> inventories;

}
