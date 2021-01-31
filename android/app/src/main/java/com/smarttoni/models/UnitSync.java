package com.smarttoni.models;

import com.smarttoni.entities.UnitConversion;
import com.smarttoni.entities.Units;

import java.util.List;

public class UnitSync {

    private SyncData<Units> units;

    private List<UnitConversion> conversions;

    public SyncData<Units> getUnits() {
        return units;
    }

    public void setUnits(SyncData<Units> units) {
        this.units = units;
    }

    public List<UnitConversion> getConversions() {
        return conversions;
    }

    public void setConversions(List<UnitConversion> conversions) {
        this.conversions = conversions;
    }
}
