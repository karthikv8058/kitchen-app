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
import com.smarttoni.entities.Order;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.Work;
import com.smarttoni.pegion.PushMessage;

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


    public void printTask(String userId,Work t, String interventionName, Task task) throws Exception {
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

            print(printerObject,userId, printDetails);

        }
    }

//    private void printTaskDetails(Printer printerObject, Task task, Station station, String tableNo, Recipe recipe, Work w) {
//        new Thread(() -> {
//            try {
//                EscPosPrinter printer = new EscPosPrinter(new TcpConnection(printerObject.getIpAddress(), Integer.parseInt(String.valueOf(printerObject.getPort()))), 203, 48f, 32);
//                printer.printFormattedTextAndCut(
//                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.smarttoni, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
//                                "[L]\n" + "[C]<font size='normal'> Task Completed</font>\n" +
//                                "[L]\n" +
//                                "[C]================================\n" +
//                                "[L]\n" + "Task            : " + task.getName() + " ( " + (w.getActualQty() > 0 ? w.getActualQty() : task.getOutputQuantity()) + " " + task.getOutputUnitName() + " )" +
//                                "[L]\n" +
//                                "[L]\n" + "Station         : " + station.getName() +
//                                "[L]\n" +
//                                "[L]\n" + "Recipe          : " + recipe.getName() +
//                                "[L]\n" +
//                                "[L]\n" + "Table           : " + tableNo +
//                                "[L]\n" +
//                                "[C]================================\n" +
//                                "[L]\n" + "[L]\n" +
//                                "[L]SmartTONi\n"
//                );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

    private void print(Printer printerObject, String userId,PrintDetails printDetails) {


        String title = Strings.isEmpty(printDetails.intervention) ? "Task Completed" : "Intervention Completed";

        String intervention = Strings.isNotEmpty(printDetails.intervention) ?  "[L]\n" + "Intervention    : " + printDetails.intervention +"[L]\n" :"";
        new Thread(() -> {
            try {
                EscPosPrinter printer = new EscPosPrinter(new TcpConnection(printerObject.getIpAddress(), Integer.parseInt(String.valueOf(printerObject.getPort()))), 203, 48f, 32);
                printer.printFormattedTextAndCut(
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.smarttoni, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                                "[L]\n" + "[C]<font size='normal'> " + title + "</font>\n" +
                                "[L]\n" +
                                "[C]================================\n" +
                                intervention+
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
            sendPrintDetails(userId,  printDetails);

        }).start();
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