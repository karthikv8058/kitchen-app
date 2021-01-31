package com.smarttoni.core;

import com.smarttoni.assignment.Queue;
import com.smarttoni.assignment.chef.UserManager;
import com.smarttoni.assignment.interventions.InterventionManager;
import com.smarttoni.assignment.order.OrderManager;
import com.smarttoni.assignment.task.TaskManger;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Machine;
import com.smarttoni.entities.Restaurant;
import com.smarttoni.entities.User;
import com.smarttoni.pegion.PushCallback;
import com.smarttoni.pegion.PushManager;
import com.smarttoni.pegion.PushMessage;

import java.util.List;

public class SmarttoniContext {

    private Restaurant restaurant = null;
    private DaoAdapter daoAdapter;
    private PushManager pushManager;


    private OrderManager orderManager;
    private TaskManger taskManger;
    private InterventionManager interventionManager;
    private UserManager userManager;

    public SmarttoniContext(DaoAdapter daoAdapter, PushManager pushManager) {

        this.daoAdapter = daoAdapter;
        this.pushManager = pushManager;
        this.interventionManager = new InterventionManager(this);


        this.orderManager = new OrderManager(this);
        this.taskManger = new TaskManger(this);
        this.userManager = new UserManager(this);
    }

    public void startServer(int port) {

    }

    public int getServerPort() {
        return 8888;
    }


    public void stopServer() {

    }


    public List<User> getUsers() {
        return null;
    }


    public List<Machine> getMachines() {
        return null;
    }


    public Queue getQueue() {
        return null;
    }


    public String updateUser() {
        return null;
    }


    public void findService(String serviceName) {

    }

    public boolean isServerRunning() {
        return restaurant != null;
    }


    public void updateRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }


    public Restaurant getRestaurant() {
        return restaurant;
    }


    public void pushAll(PushMessage pushMessage, PushCallback callback) {
        pushManager.pushAll(pushMessage, callback);
    }

    public void push(String userId, PushMessage pushMessage, PushCallback callback) {
        pushManager.push(userId, pushMessage, callback);
    }

    public OrderManager getOrderManager() {
        return orderManager;
    }

    public TaskManger getTaskManger() {
        return taskManger;
    }

    public InterventionManager getInterventionManager() {
        return interventionManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
