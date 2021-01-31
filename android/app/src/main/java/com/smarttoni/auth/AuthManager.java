package com.smarttoni.auth;

import android.content.Context;

import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.User;

import java.util.UUID;

public class AuthManager {


    private Context context;

    private AuthManager() {
    }

    private static AuthManager INSTANCE = new AuthManager();

    public static AuthManager getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
    }

    public User findUserByToken(String token) {
        if (context == null) {
            throw new AuthManagerNotInitException();
        }
        return new GreenDaoAdapter(context).getUserByToken(token);
    }

    public void generateToken(User user) {
        user.setToken(user.getUsername() + "_" + UUID.randomUUID());
    }

    class AuthManagerNotInitException extends RuntimeException {
    }
}
