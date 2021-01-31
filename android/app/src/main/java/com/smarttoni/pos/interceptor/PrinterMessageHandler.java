package com.smarttoni.pos.interceptor;

import android.content.Context;

import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.OrderDao;
import com.smarttoni.entities.PrinterData;
import com.smarttoni.entities.PrinterDataDao;
import com.smarttoni.grenade.Event;
import com.smarttoni.grenade.EventManager;

import java.util.Date;

public class PrinterMessageHandler {

    private Context context;
    private String message;
    private String ip;

    public PrinterMessageHandler(Context context, String messsage, String ip) {
        this.context = context;
        this.message = messsage;
        this.ip = ip;
        EventManager.getInstance().emit(Event.ORDER_RECEIVED);
    }

    public void saveMessage() {

        PrinterDataDao printerDataDao = DbOpenHelper.Companion.getDaoSession(context).getPrinterDataDao();
        PrinterData printerData = new PrinterData();
        printerData.setTimestamp(new Date().getTime());
        printerData.setIp(ip);
        printerData.setMessage(message);
        printerData.setStatus(PrinterData.STATUS_NEW);
        printerData.setCreatedAt(System.currentTimeMillis());

        printerDataDao.insert(printerData);

        OrderDao orderDao = DbOpenHelper.Companion.getDaoSession(context).getOrderDao();
        Order order = new Order();
        order.setIsInventory(false);
        order.setTableNo(null);
        order.setStatus(Order.ORDER_OPEN);
        order.setPrinterDataId(printerData.getId());
        orderDao.insert(order);
    }
}
