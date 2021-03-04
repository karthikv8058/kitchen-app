package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.assignment.service.ServiceLocator;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.database.DbOpenHelper;
import com.smarttoni.database.GreenDaoAdapter;
import com.smarttoni.entities.ArchivedOrder;
import com.smarttoni.entities.ArchivedOrders;
import com.smarttoni.entities.Order;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.http.HttpClient;
import com.smarttoni.sync.orders.SyncOrder;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.LocalStorage;
import com.smarttoni.utils.DateUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoadOrderFromWeb extends HttpSecurityRequest {
    private Context context;
    GreenDaoAdapter greenDaoAdapter;

    public LoadOrderFromWeb(Context context) {
        this.context = context;
        greenDaoAdapter = new GreenDaoAdapter(context);
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse respons) {
        try {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
            int isExternalOrder = jsonObject.getInt("isExternalOrder");

        List<Order> ordersList = greenDaoAdapter.loadOrders();
        String orderId = "";
        LocalStorage ls = (LocalStorage) ServiceLocator.getInstance().getService(ServiceLocator.LOCAL_STORAGE_SERVICE);

            for (int i = 0; i < ordersList.size(); i++) {
                if (i + 1 < ordersList.size()) {
                    Date result = new Date(ordersList.get(i).getCreatedAt());
                    Date result2 = new Date(ordersList.get(i + 1).getCreatedAt());
                    if (result.after(result2)) {
                        orderId = ordersList.get(i).getId();
                    } else {
                        orderId = ordersList.get(i + 1).getId();
                    }
                }
            }
            new HttpClient(context).getHttpClient().loadOrders(ls.getRestaurantId(), orderId, 10,isExternalOrder).enqueue(new Callback<List<SyncOrder>>() {
                @Override
                public void onResponse(Call<List<SyncOrder>> call, Response<List<SyncOrder>> response) {
                    boolean isSuccess = false;
                    if(response.body()!=null) {
                        if(response.body().size() > 0){
                            isSuccess = true;
                        }
                        for (SyncOrder o : response.body()) {
                            List<Order> orders=greenDaoAdapter.loadOrderById(o.uuid);
                            List<ArchivedOrders> archivedOrders1=greenDaoAdapter.loadArchivedOrderById(o.uuid);
                            for (Order os:orders) {
                                greenDaoAdapter.deleteOrder(os);
                            }
                            for (ArchivedOrders archivedOrders2:archivedOrders1) {
                                greenDaoAdapter.deletArchivedOrder(archivedOrders2);
                            }
                            Order order = new Order();
                            ArchivedOrders archivedOrders = new ArchivedOrders();
                            order.setId(o.uuid);
                            order.setUpdatedAt(DateUtil.parse(o.updatedAt));
                            order.setStatus(o.status);
                            order.setIsInventory(o.inventoryOrder);
                            order.setTableNo(o.table);
                            order.setProcessed(false);
                            order.setCreatedAt(DateUtil.parse(o.createdAt));
                            order.setIsArchive(true);
                            archivedOrders.setId(o.uuid);
                            Gson gsons = GSONBuilder.createGSON();
                            archivedOrders.setOrderData(gsons.toJson(o));
                            //greenDaoAdapter.saveOrder(order);
                            greenDaoAdapter.saveArchivedOrders(archivedOrders);


                            ArchivedOrder archivedOrder = new ArchivedOrder();
                            archivedOrder.setOrderId(o.uuid);
                            archivedOrder.setOrder(gsons.toJson(o));
                            archivedOrder.setType(isExternalOrder);
                            greenDaoAdapter.saveArchivedOrder(archivedOrder);

                        }
                    }
                    Gson gson = GSONBuilder.createGSON();
                    Type type = new TypeToken<Boolean >() {
                    }.getType();
                    respons.send(gson.toJson(isSuccess, type));
                }

                @Override
                public void onFailure(Call<List<SyncOrder>> call, Throwable throwable) {
                    Gson gson = GSONBuilder.createGSON();
                    Type type = new TypeToken<Boolean>() {
                    }.getType();
                    respons.send(gson.toJson(false, type));
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}