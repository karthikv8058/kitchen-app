package com.smarttoni.server.controlles.order;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.smarttoni.auth.HttpSecurityRequest;
import com.smarttoni.pos.models.NewPosOrderModel;
import com.smarttoni.server.GSONBuilder;
import com.smarttoni.server.controlles.pos.PosUtils;
import com.smarttoni.utils.HttpHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class FetchOrder extends HttpSecurityRequest {
    private Context context;

    public FetchOrder(Context context) {
        this.context = context;
    }

    @Override
    public void processRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        JSONObject jsonObject = HttpHelper.postDataToJson(request);
        try {
            String orderId = jsonObject.getString("orderId");
            Gson gson = GSONBuilder.createGSON();
            PosUtils posUtils = new PosUtils(context);
            Type type = new TypeToken<NewPosOrderModel>() {
            }.getType();
            response.send(gson.toJson(posUtils.getPosStateByOrderId(orderId), type));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
