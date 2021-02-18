package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "unit_conversions")
public class UnitConversion {

    @Id(autoincrement = true)
    private Long id;

    @SerializedName("from_uuid")
    private String from;

    @SerializedName("to_uuid")
    private String to;


    @SerializedName("conversion_factor")
    private float factor;

    @Generated(hash = 897206099)
    public UnitConversion() {
    }

    @Generated(hash = 468162090)
    public UnitConversion(Long id, String from, String to, float factor) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.factor = factor;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public float getFactor() {
        return this.factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    
}
