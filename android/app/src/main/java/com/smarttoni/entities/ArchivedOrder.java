package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "archived_orders")
public class ArchivedOrder {

    @Id(autoincrement = true)
    private Long id;

    @Property
    private String orderId;

    @Property
    private String order;

    @Property
    private int type;
    
    @Generated(hash = 868147540)
    public ArchivedOrder(Long id, String orderId, String order, int type) {
        this.id = id;
        this.orderId = orderId;
        this.order = order;
        this.type = type;
    }

    @Generated(hash = 792777881)
    public ArchivedOrder() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
