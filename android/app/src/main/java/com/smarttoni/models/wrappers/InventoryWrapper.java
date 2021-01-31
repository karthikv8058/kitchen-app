package com.smarttoni.models.wrappers;

import com.smarttoni.entities.Room;
import com.smarttoni.entities.Units;
import com.smarttoni.models.KeyValue;

import java.util.List;

public class InventoryWrapper {

    public List<RecipeWrapper> recipes;
    public List<Room> rooms;
    public List<UnitConversionWrapper> unitConversions;
    public List<Units> units;
}
