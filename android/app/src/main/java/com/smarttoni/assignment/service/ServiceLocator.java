package com.smarttoni.assignment.service;

import android.content.Context;

import com.smarttoni.assignment.AssignmentFactory;
import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.delay.DelayHelper;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.pegion.PushManager;
import com.smarttoni.utils.LocalStorage;
import com.smarttoni.udp.UdpManager;
import com.smarttoni.utils.PrinterManager;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {

    private Queue queue = new Queue();

    public static final String PUSH_SERVICE = "PUSH_SERVICE";
    public static final String DAO_ADAPTER_SERVICE = "DAO_ADAPTER_SERVICE";
    public static final String DELAY_CALCULATOR_SERVICE = "DELAY_CALCULATOR_SERVICE";
    public static final String ASSIGNMENT_SERVICE = "ASSIGNMENT_SERVICE";
    public static final String LOCAL_STORAGE_SERVICE = "LOCAL_STORAGE_SERVICE";


    public static final String SMARTTONI_CONTEXT = "SMARTTONI_CONTEXT";
    public static final String UDP_MANAGER = "UDP_MANAGER";

    private Map<String, Object> services = new HashMap<>();

    private static ServiceLocator instance = null;

    private ServiceLocator() {
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized (ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    public void initBasic(Context context) {
        DaoAdapter daoAdapter = new GreenDaoAdapter(context);
        services.put(DAO_ADAPTER_SERVICE, daoAdapter);
        services.put(LOCAL_STORAGE_SERVICE, new LocalStorage(context));
        services.put(UDP_MANAGER, new UdpManager());
    }

    public void init(Context context) {
        DaoAdapter daoAdapter = null;
        if (getDatabaseAdapter() != null) {
            daoAdapter = getDatabaseAdapter();
        } else {
            daoAdapter = new GreenDaoAdapter(context);
        }
        services.put(PUSH_SERVICE, new PusherService());
        DelayHelper.getInstance().init(daoAdapter);
        services.put(DELAY_CALCULATOR_SERVICE, DelayHelper.getInstance());
        services.put(ASSIGNMENT_SERVICE, AssignmentFactory.getInstance());
        services.put(SMARTTONI_CONTEXT, new SmarttoniContext(daoAdapter, new PushManager(daoAdapter)));

        //
        PrinterManager.getInstance().init(context);
    }

    public Object getService(String serviceName) {
        return services.get(serviceName);
    }

    public DaoAdapter getDatabaseAdapter() {
        return (DaoAdapter) getService(DAO_ADAPTER_SERVICE);
    }


    public LocalStorage getLocalStorage() {
        return (LocalStorage) getService(LOCAL_STORAGE_SERVICE);
    }

    public SmarttoniContext getSmarttoniContext() {
        return (SmarttoniContext) getService(SMARTTONI_CONTEXT);
    }

    public Queue getQueue() {
        return queue;
    }

}
