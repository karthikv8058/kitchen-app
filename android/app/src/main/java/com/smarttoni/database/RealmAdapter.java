package com.smarttoni.database;

import com.smarttoni.entities.NetworkLog;

import io.realm.Realm;


public class RealmAdapter {


    private RealmAdapter() {
    }

    private static RealmAdapter INSTANCE = new RealmAdapter();

    public static RealmAdapter getInstance() {
        return INSTANCE;
    }


    public void logNetwork(NetworkLog networkLog) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(networkLog);
        realm.commitTransaction();
    }
}
