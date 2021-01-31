package com.smarttoni.utils;

import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.User;
import com.smarttoni.pegion.PushMessage;

import java.util.List;

public class PushUserSettings {

    public static void pushSettings(DaoAdapter daoAdapter) {
        List<User> users = daoAdapter.loadUsers();
        for (User user : users) {
            if (user.getOnline() && !"".equals(user.getIpAddress())) {
                PushMessage pushMessage = new PushMessage(PushMessage.TYPE_UPDATE_USER, user);

                SmarttoniContext sc = ServiceLocator.getInstance().getSmarttoniContext();
                if (sc != null) {
                    sc.push(user.getId(), pushMessage, null);
                }
            }
        }
    }
}
