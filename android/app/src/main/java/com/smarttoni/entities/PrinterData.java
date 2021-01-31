package com.smarttoni.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

@Entity(nameInDb = "printer_data")
public class PrinterData {

    @Transient
    public static String STATUS_NEW = "NEW";
    @Transient
    public static String ORDER_TYPE = "PRINTER_MESSAGE";
    @Transient
    public String ORDER_OVERVIEW_ITEM_TYPE;

    @Transient
    public String ip_address;

    @Transient
    public String received_on;

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "timestamp")
    private long timestamp;

    @Property(nameInDb = "ip")
    private String ip;

    @Property(nameInDb = "message")
    private String message;

    @Property(nameInDb = "status")
    private String status;

    @Property(nameInDb = "isUpdated")
    private String isUpdated;

    @Property(nameInDb = "createdAt")
    private long createdAt;

    @Property(nameInDb = "updatedAt")
    private long updatedAt;

    @Generated(hash = 1070006892)
    public PrinterData(Long id, long timestamp, String ip, String message,
            String status, String isUpdated, long createdAt, long updatedAt) {
        this.id = id;
        this.timestamp = timestamp;
        this.ip = ip;
        this.message = message;
        this.status = status;
        this.isUpdated = isUpdated;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Generated(hash = 25411933)
    public PrinterData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIsUpdated() {
        return this.isUpdated;
    }

    public void setIsUpdated(String isUpdated) {
        this.isUpdated = isUpdated;
    }


}
