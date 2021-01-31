package com.smarttoni.logger;

import com.google.gson.Gson;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.server.GSONBuilder;

public class Logger {

    public static void write(String tag,String log){
        //ServiceLocator.getInstance().getDatabaseAdapter().writeLog(tag,log);
    }

    public static void write(String tag,Object log){
        //Gson gson = GSONBuilder.createGSON();
        //ServiceLocator.getInstance().getDatabaseAdapter().writeLog(tag,gson.toJson(log));
    }
}
