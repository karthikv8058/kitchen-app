package com.smarttoni.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.smarttoni.R;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.core.SmarttoniContext;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Label;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.Work;
import com.smarttoni.pegion.PushMessage;
import com.smarttoni.pos.models.NewPosOrderModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrinterManager {


    private Context context;

    private static PrinterManager instance = null;

    private PrinterManager() {
    }


    public static PrinterManager getInstance() {

        if (instance == null) {
            synchronized (ServiceLocator.class) {
                instance = new PrinterManager();
            }
        }
        return instance;
    }


    public void init(Context context) {
        this.context = context;
    }


    public void printTask(String userId, Work t, String interventionName, Task task) throws Exception {
        if (context == null) {
            throw new IllegalStateException("init before print");
        }
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        if (task.getStationId() != null) {
            Station station = daoAdapter.getStationById(task.getStationId());
            if (station == null) {
                return;
            }
            if (Strings.isEmpty(station.getPrinterUuid())) {
                return;
            }
            Printer printerObject = daoAdapter.getPrinterDataById(station.getPrinterUuid());
            Recipe recipe = daoAdapter.getRecipeById(task.getRecipeId());
            Order order = null;
            if (!(Strings.isEmpty(t.getOrderId()))) {
                order = daoAdapter.getOrderById(t.getOrderId());
            }
            Order finalOrder = order;
            String tableNo = finalOrder != null ? (finalOrder.getTableNo() != "" ? finalOrder.getTableNo() : "--") : "--";


            PrintDetails printDetails = new PrintDetails();
            printDetails.intervention = interventionName;
            printDetails.task = task.getName() + " ( " + (t.getQuantity() * task.getOutputQuantity()) + " " + task.getOutputUnitName() + " )";
            printDetails.station = station.getName();
            printDetails.recipe = recipe.getName();
            printDetails.table = tableNo;

            print(printerObject, userId, printDetails);

        }
    }

    private void print(Printer printerObject, String userId, PrintDetails printDetails) {


        String title = Strings.isEmpty(printDetails.intervention) ? "Task Completed" : "Intervention Completed";

        String intervention = Strings.isNotEmpty(printDetails.intervention) ? "[L]\n" + "Intervention    : " + printDetails.intervention + "[L]\n" : "";
        new Thread(() -> {
            try {
                EscPosPrinter printer = new EscPosPrinter(new TcpConnection(printerObject.getIpAddress(), Integer.parseInt(String.valueOf(printerObject.getPort()))), 203, 48f, 32);
                printer.printFormattedTextAndCut(
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.smarttoni, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                                "[L]\n" + "[C]<font size='normal'> " + title + "</font>\n" +
                                "[L]\n" +
                                "[C]================================\n" +
                                intervention +
                                "[L]\n" + "Task            : " + printDetails.task +
                                "[L]\n" +
                                "[L]\n" + "Station         : " + printDetails.station +
                                "[L]\n" +
                                "[L]\n" + "Recipe          : " + printDetails.recipe +
                                "[L]\n" +
                                "[L]\n" + "Table           : " + printDetails.table +
                                "[L]\n" +
                                "[C]================================\n" +
                                "[L]\n" + "[L]\n" +
                                "[L]SmartTONi\n"
                );
                printDetails.isSuccess = true;
            } catch (Exception e) {
                printDetails.isSuccess = false;
            }
            sendPrintDetails(userId, printDetails);

        }).start();
    }


    public void printOrder(Label label, String recipe, String table,long deliveryTime,String staff) {
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        if (Strings.isEmpty(label.getPrinterUuid())) {
            return;
        }
        Printer printerObject = daoAdapter.getPrinterDataById(label.getPrinterUuid());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(deliveryTime));
        String createdDate = sdf.format(new Date());
        String labelName = label.getName();

        String _table = Strings.isNotEmpty(table) ? table : "--" ;
        if (printerObject != null) {
            new Thread(() -> {
                try {
                    EscPosPrinter printer = new EscPosPrinter(new TcpConnection(printerObject.getIpAddress(), Integer.parseInt(String.valueOf(printerObject.getPort()))), 203, 48f, 32);
                    printer.printFormattedTextAndCut(
                            "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.smarttoni, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                                    "[L]\n" +
                                    "[C]<font size='normal'>Order Received </font>\n" +
                                    "[L]\n" +
                                    "[C]=========================================\n" +
                                    "[L]\n" +
                                    "[L]\n" + "Table             : " + _table +
                                    "[L]\n" +
                                    "[L]\n" + "Order received at : " + createdDate +
                                    "[L]\n" +
                                    "[L]\n" + "Delivery Time     : " + date +
                                    "[L]\n" +
                                    "[L]\n" + "Label             : " + labelName +
                                    "[L]\n" +
                                    "[L]\n" + "Recipe            : " + recipe +
                                    "[L]\n" +
                                    "[L]\n" + "Ordering Staff    : " + staff +
                                    "[L]\n" +
                                    "[C]=========================================\n" +
                                    "[L]\n" + "[L]\n" +
                                    "[L]SmartTONi\n"
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void sendPrintDetails(String userId, PrintDetails printDetails) {

        SmarttoniContext context = ServiceLocator.getInstance().getSmarttoniContext();

        PushMessage pm = new PushMessage(PushMessage.TYPE_PRINT_COMPLETE, printDetails);

        context.push(userId, pm, null);

    }


    class PrintDetails {
        boolean isSuccess;
        String intervention;
        String task;
        String station;
        String recipe;
        String table;
    }
}