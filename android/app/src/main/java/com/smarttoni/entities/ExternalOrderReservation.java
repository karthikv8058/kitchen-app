package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ExternalOrderReservation {

    @SerializedName("uuid")
    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    private String externalOrder;

    private String parentOrder;


    @Generated(hash = 1744490317)
    public ExternalOrderReservation(@NotNull String id, String externalOrder,
            String parentOrder) {
        this.id = id;
        this.externalOrder = externalOrder;
        this.parentOrder = parentOrder;
    }

    @Generated(hash = 2040143389)
    public ExternalOrderReservation() {
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExternalOrder() {
        return this.externalOrder;
    }

    public void setExternalOrder(String externalOrder) {
        this.externalOrder = externalOrder;
    }

    public String getParentOrder() {
        return this.parentOrder;
    }

    public void setParentOrder(String parentOrder) {
        this.parentOrder = parentOrder;
    }
    
}
