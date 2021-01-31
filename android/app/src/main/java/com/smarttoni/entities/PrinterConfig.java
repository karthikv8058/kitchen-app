package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "printer_config")
public class PrinterConfig {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "port")
    private long port;

    @Property(nameInDb = "protocol")
    private String protocol;

    @SerializedName("created_at")
    @Property(nameInDb = "createdat")
    private long createdAt;

    @Property(nameInDb = "updatedat")
    private long updatedAt;

    @Property(nameInDb = "ip_address")
    private String ipAdress;


    @Generated(hash = 362518708)
    public PrinterConfig(Long id, String name, long port, String protocol,
                         long createdAt, long updatedAt, String ipAdress) {
        this.id = id;
        this.name = name;
        this.port = port;
        this.protocol = protocol;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.ipAdress = ipAdress;
    }

    @Generated(hash = 1354347153)
    public PrinterConfig() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPort() {
        return this.port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
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

    public String getIpAdress() {
        return this.ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }


}
