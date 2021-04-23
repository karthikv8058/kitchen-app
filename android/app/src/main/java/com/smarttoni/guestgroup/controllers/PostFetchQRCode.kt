package com.smarttoni.guestgroup.controllers

import com.google.gson.reflect.TypeToken
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.smarttoni.assignment.service.ServiceLocator
import com.smarttoni.auth.HttpSecurityRequest
import com.smarttoni.guestgroup.entities.GuestGroup
import com.smarttoni.server.GSONBuilder
import org.json.JSONException

class PostFetchQRCode : HttpSecurityRequest() {

    @Throws(JSONException::class)
    override fun processRequest(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {

        val station = ServiceLocator.getInstance().databaseAdapter.stationByName()[0];
        var room : String= "";
        if(station != null){
            room = station.room;
        }

        val gson = GSONBuilder.createGSON()
        val type = object : TypeToken<GuestGroup?>() {}.type
        response.send(gson.toJson(GuestGroup(1,room,station.name,"1"), type))

    }

}