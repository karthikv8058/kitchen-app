package com.smarttoni.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.smarttoni.R;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.database.DaoAdapter;
import com.smarttoni.entities.Order;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Station;
import com.smarttoni.entities.Task;
import com.smarttoni.entities.Work;

public class PrinterManager {


    private  Context context;

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


    public void init(Context context){
        this.context = context;
    }


    public void printTask( Work t) throws Exception{
        if(context == null){
            throw new IllegalStateException("init before print");
        }
        DaoAdapter daoAdapter = ServiceLocator.getInstance().getDatabaseAdapter();
        Task task = daoAdapter.getTaskById(t.getTaskId());
        if (task.getPrintLabel() == true) {
            if (task.getStationId() != null) {
                Station station = daoAdapter.getStationById(t.getStationId());
                Printer printerObject = daoAdapter.getPrinterDataById(station.getPrinterUuid());
                Recipe recipe = daoAdapter.getRecipeById(t.getRecipeId());
                Order order = null;
                if (!(Strings.isEmpty(t.getOrderId()))) {
                    order = daoAdapter.getOrderById(t.getOrderId());
                }
                Order finalOrder = order;
                String tableNo=finalOrder != null ?( finalOrder.getTableNo()!=""? finalOrder.getTableNo():"--") : "--";
                new Thread(() -> {
                    try {
                        EscPosPrinter printer = new EscPosPrinter(new TcpConnection(printerObject.getIpAddress(), Integer.parseInt(String.valueOf(printerObject.getPort()))), 203, 48f, 32);
                        printer.printFormattedTextAndCut(
                                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.smarttoni, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                                        "[L]\n" +
                                        "[C]<font size='normal'>  Task Completed</font>\n" +
                                        "[L]\n" +
                                        "[C]================================\n" +
                                        "[L]\n" + "Task    : " + task.getName() + " ( " + task.getOutputQuantity() + " " + task.getOutputUnitName() + " )" +
                                        "[L]\n" +
                                        "[L]\n" + "Station : " + station.getName() +
                                        "[L]\n" +
                                        "[L]\n" + "Recipe  : " + recipe.getName() +
                                        "[L]\n" +
                                        "[L]\n" + "Table   : " +  tableNo+
                                        "[L]\n" +
                                        "[C]================================\n" +
                                        "[L]\n" + "[L]\n" +
                                        "[L]SmartTONi\n"
                        );
                    } catch (Exception e) {
                    }
                }).start();
            }
        }
    }



}
