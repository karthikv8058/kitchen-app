package com.smarttoni.guestgroup.controllers

import com.google.gson.reflect.TypeToken
import com.koushikdutta.async.http.server.AsyncHttpServerRequest
import com.koushikdutta.async.http.server.AsyncHttpServerResponse
import com.smarttoni.auth.HttpSecurityRequest
import com.smarttoni.entities.Room
import com.smarttoni.guestgroup.entities.Guest
import com.smarttoni.server.GSONBuilder
import org.json.JSONException

class PostMergeGuestGroup : HttpSecurityRequest() {

    @Throws(JSONException::class)
    override fun processRequest(request: AsyncHttpServerRequest, response: AsyncHttpServerResponse) {
        val gson = GSONBuilder.createGSON()
        val type = object : TypeToken<Boolean?>() {}.type
        response.send(gson.toJson(true, type))

    }

}