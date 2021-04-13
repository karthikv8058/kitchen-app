package com.smarttoni.guestgroup.controllers

import com.google.gson.reflect.TypeToken
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.smarttoni.auth.HttpSecurityRequest
import com.smarttoni.entities.Room
import com.smarttoni.guestgroup.entities.Guest
import com.smarttoni.server.GSONBuilder
import org.json.JSONException

class ListGuestInGuestGroups : HttpSecurityRequest() {

    @Throws(JSONException::class)
    override fun processRequest(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {

        val guests = arrayOf(Guest("1"), Guest("2"), Guest("3"))
        val gson = GSONBuilder.createGSON()
        val type = object : TypeToken<List<Guest?>?>() {}.type
        response.send(gson.toJson(guests, type))

    }

}