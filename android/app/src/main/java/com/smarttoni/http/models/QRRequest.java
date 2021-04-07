package com.smarttoni.http.models;

public class QRRequest {

    private int total_number;
    private String room;
    private String station;

    public QRRequest(int total_number, String room, String station) {
        this.total_number = total_number;
        this.room = room;
        this.station = station;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
