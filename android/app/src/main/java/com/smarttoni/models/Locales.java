package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;

public class Locales {
    @SerializedName("name")
    private String name;
    @SerializedName("title")
    private String title;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("description")
    private String description;
    @SerializedName("printerName")
    private String printerName;
    @SerializedName("check_in_criteria")
    private String checkInCriteria;

    @SerializedName("label_uuid")
    private String label_uuid;

    public String getLabel_uuid() {
        return label_uuid;
    }

    public void setLabel_uuid(String label_uuid) {
        this.label_uuid = label_uuid;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getCheckInCriteria() {
        return checkInCriteria;
    }

    public void setCheckInCriteria(String checkInCriteria) {
        this.checkInCriteria = checkInCriteria;
    }
}
