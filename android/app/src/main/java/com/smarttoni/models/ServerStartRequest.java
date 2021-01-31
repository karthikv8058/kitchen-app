package com.smarttoni.models;

import com.google.gson.annotations.SerializedName;

public class ServerStartRequest {

    @SerializedName("ip_address")
    private String ipAddress;

    public ServerStartRequest(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
