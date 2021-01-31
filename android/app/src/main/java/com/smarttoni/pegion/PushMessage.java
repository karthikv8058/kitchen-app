package com.smarttoni.pegion;

public class PushMessage {

    public static final int TYPE_SERVER_LOGOUT = 1;
    public static final int TYPE_UPDATE_QUEUE = 2;
    public static final int TYPE_UPDATE_USER = 3;
    public static final int TYPE_INTERVENTION = 4;
    public static final int TYPE_UDP = 5;

    private int type;
    private Object data;

    public PushMessage(int type) {
        this.type = type;
    }

    public PushMessage(int type, Object data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
