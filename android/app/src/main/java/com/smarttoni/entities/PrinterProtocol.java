package com.smarttoni.entities;

import com.google.gson.annotations.SerializedName;


public class PrinterProtocol {

    @SerializedName("name")
    private String name;

    @SerializedName("format")
    private String format;

    @SerializedName("uuid")
    private String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
