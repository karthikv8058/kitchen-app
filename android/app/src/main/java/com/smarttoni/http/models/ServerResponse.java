package com.smarttoni.http.models;

public class ServerResponse<T> {

    private boolean status;
    private T data;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
