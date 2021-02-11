package com.smarttoni.server.controlles.station;

import android.content.Context;
import android.util.DisplayMetrics;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.R;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Recipe;
import com.smarttoni.entities.Station;
import com.smarttoni.http.HttpClient;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.LocalStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestStationPrinter extends HttpSecurityRequest {
    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public TestStationPrinter(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        try {
            JSONObject jsonObject = HttpHelper.postDataToJson(request);
            String stationId = jsonObject.getString("stationId");
            Station station = greenDaoAdapter.getStationById(stationId);
            Printer printerObject = greenDaoAdapter.getPrinterDataById(station.getPrinterUuid());
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EscPosPrinter printer = new EscPosPrinter(new TcpConnection(printerObject.getIpAddress(), Integer.parseInt(String.valueOf(printerObject.getPort()))), 203, 48f, 32);
                        printer.printFormattedTextAndCut(
                                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.smarttoni, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                                        "[L]\n" +
                                        "[C]<font size='normal'>Station Testing </font>\n" +
                                        "[L]\n" +
                                        "[C]================================\n" +
                                        "[L]\n" +
                                        "[L]\n" + "Station : " + station.getName() +
                                        "[L]\n" +
                                        "[C]================================\n" +
                                        "[L]\n" + "[L]\n" +
                                        "[L]SmartTONi\n"
                        );
                    } catch (Exception e) {
                        Gson gson = GSONBuilder.createGSON();
                        Type type = new TypeToken<Boolean>() {
                        }.getType();
                        response.send(gson.toJson(false, type));
                        e.printStackTrace();
                        return;
                    }
                }
            }).start();

            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(true, type));

        } catch (Exception e) {
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(false, type));
            e.printStackTrace();
        }

    }


}
