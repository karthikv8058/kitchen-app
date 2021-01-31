package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "archivedOrder")
public class ArchivedOrders {

    @Index(unique = true)
    @NotNull
    @Id
    private String id;

    @Property(nameInDb = "orderData")
    private String orderData;

    @Generated(hash = 1219185634)
    public ArchivedOrders(@NotNull String id, String orderData) {
        this.id = id;
        this.orderData = orderData;
    }

    @Generated(hash = 1844869539)
    public ArchivedOrders() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderData() {
        return this.orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

}
