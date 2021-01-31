package com.smarttoni.models;

public class TransportSheet {

    private String from_type;
    private String from_uuid;
    private String to_type;
    private String to_uuid;
    private int transport_mode;
    private String uuid;

    public String getFrom_type() {
        return from_type;
    }

    public void setFrom_type(String from_type) {
        this.from_type = from_type;
    }

    public String getFrom_uuid() {
        return from_uuid;
    }

    public void setFrom_uuid(String from_uuid) {
        this.from_uuid = from_uuid;
    }

    public String getTo_type() {
        return to_type;
    }

    public void setTo_type(String to_type) {
        this.to_type = to_type;
    }

    public String getTo_uuid() {
        return to_uuid;
    }

    public void setTo_uuid(String to_uuid) {
        this.to_uuid = to_uuid;
    }

    public int getTransport_mode() {
        return transport_mode;
    }

    public void setTransport_mode(int transport_mode) {
        this.transport_mode = transport_mode;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
