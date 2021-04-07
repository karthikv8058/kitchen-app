package com.smarttoni.server.controlles.station;

import android.content.Context;
import android.util.DisplayMetrics;

import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.tcp.TcpConnection;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.R;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.Printer;
import com.smarttoni.entities.Station;
import com.smarttoni.http.HttpClient;
import com.smarttoni.http.models.QRRequest;
import com.smarttoni.http.models.QRResponse;
import com.smarttoni.http.models.ServerResponse;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostGenerateQR extends HttpSecurityRequest {
    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public PostGenerateQR(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        try {
            JSONObject jsonObject = HttpHelper.postDataToJson(request);
            String stationId = jsonObject.getString("stationId");
            Station station = greenDaoAdapter.getStationById(stationId);
            QRRequest qr = new QRRequest(10, station.getRoom(), stationId);
            new HttpClient(context).getHttpClient().generateQR(ServiceLocator.getInstance().getSmarttoniContext().getRestaurant().getUuid(), qr).enqueue(new Callback<ServerResponse<List<QRResponse>>>() {
                @Override
                public void onResponse(Call<ServerResponse<List<QRResponse>>> call, Response<ServerResponse<List<QRResponse>>> resp) {

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            ServerResponse<List<QRResponse>> data = resp.body();
                            if (data != null && data.getData() != null) {
                                List<QRResponse> qrCodes = data.getData();
                                Printer printerObject = greenDaoAdapter.getPrinterDataById(station.getPrinterUuid());
                                for (QRResponse qr : qrCodes) {
                                    EscPosPrinter printer = null;
                                    try {
                                        printer = new EscPosPrinter(new TcpConnection(printerObject.getIpAddress(), Integer.parseInt(String.valueOf(printerObject.getPort()))), 203, 48f, 32);
                                        printer.printFormattedTextAndCut(
                                                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.smarttoni, DisplayMetrics.DENSITY_MEDIUM)) + "</img>\n" +
                                                        "[L]\n" +
                                                        "[C]<qrcode size='40'>" + qr.getUrl() + "</qrcode>\n" +
                                                        "[L]\n" +
                                                        "[L]   " + qr.getGuest_group_number() + "\n" +
                                                        "[L]\n" +
                                                        "[L]\n" +
                                                        "[L]\n" +
                                                        "[L]SmartTONi\n"
                                        );
                                    } catch (Exception e) {
                                        Gson gson = GSONBuilder.createGSON();
                                        Type type = new TypeToken<Boolean>() {
                                        }.getType();
                                        response.send(gson.toJson(false, type));
                                    }
                                }
                            }
                        }
                    }).start();

                    Gson gson = GSONBuilder.createGSON();
                    Type type = new TypeToken<Boolean>() {
                    }.getType();
                    response.send(gson.toJson(true, type));
                }

                @Override
                public void onFailure(Call<ServerResponse<List<QRResponse>>> call, Throwable t) {
                    Gson gson = GSONBuilder.createGSON();
                    Type type = new TypeToken<Boolean>() {
                    }.getType();
                    response.send(gson.toJson(false, type));
                }
            });
        } catch (Exception e) {
            Gson gson = GSONBuilder.createGSON();
            Type type = new TypeToken<Boolean>() {
            }.getType();
            response.send(gson.toJson(false, type));
        }
    }
}