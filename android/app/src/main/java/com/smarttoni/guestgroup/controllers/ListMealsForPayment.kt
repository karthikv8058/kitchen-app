package com.smarttoni.guestgroup.controllers

import com.google.gson.reflect.TypeToken
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.smarttoni.auth.HttpSecurityRequest
import com.smarttoni.guestgroup.PaymentUtils
import com.smarttoni.guestgroup.entities.Guest
import com.smarttoni.guestgroup.entities.GuestMeal
import com.smarttoni.server.GSONBuilder
import org.json.JSONException

class ListMealsForPayment : HttpSecurityRequest() {

    @Throws(JSONException::class)
    override fun processRequest(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        val meals = arrayOf(GuestMeal("1","Tea",10.5f,"1","Tom",PaymentUtils.PAYMENT_NO),
                GuestMeal("2","Biriyani",150f,"1","Tom",PaymentUtils.PAYMENT_NO),
                GuestMeal("3","Tea",10.5f,"2","Jose",PaymentUtils.PAYMENT_NO),
                GuestMeal("4","Coffie",15f,"3","David",PaymentUtils.PAYMENT_NO),
                GuestMeal("5","Steak",200f,"3","David",PaymentUtils.PAYMENT_NO)
        ).toList();
        val gson = GSONBuilder.createGSON()
        val type = object : TypeToken<List<GuestMeal?>?>() {}.type
        response.send(gson.toJson(meals, type))

    }

}