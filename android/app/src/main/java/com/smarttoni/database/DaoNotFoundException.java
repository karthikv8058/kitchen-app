package com.smarttoni.database;

public class DaoNotFoundException extends Exception {
    public DaoNotFoundException() {
        super("Please init Dao Adapter");
    }
}
