package com.smarttoni.guestgroup.controllers

import com.google.gson.reflect.TypeToken
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.smarttoni.auth.HttpSecurityRequest
import com.smarttoni.entities.Room
import com.smarttoni.guestgroup.entities.GuestGroup
import com.smarttoni.server.GSONBuilder
import org.json.JSONException

class ListGuestGroups : HttpSecurityRequest() {

    @Throws(JSONException::class)
    override fun processRequest(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {

        val guests = arrayOf(GuestGroup(1),GuestGroup(8),GuestGroup(9))
        val gson = GSONBuilder.createGSON()
        val type = object : TypeToken<List<GuestGroup?>?>() {}.type
        response.send(gson.toJson(guests, type))

    }

}