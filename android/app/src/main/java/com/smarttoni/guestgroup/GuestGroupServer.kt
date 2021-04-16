package com.smarttoni.guestgroup

import android.content.Context
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.smarttoni.guestgroup.controllers.ListGuestGroups
import com.smarttoni.guestgroup.controllers.ListGuestInGuestGroups
import com.smarttoni.guestgroup.controllers.ListMealsForPayment
import com.smarttoni.guestgroup.controllers.PostPaymentRecord

object GuestGroupServer {
    fun addControllers(mHttpServer: AsyncHttpServer, context: Context?) {
        mHttpServer.get("/guestGroup/list", ListGuestGroups())
        mHttpServer.post("/guestGroup/guests", ListGuestInGuestGroups())
        mHttpServer.post("/guestGroup/split", ListGuestInGuestGroups())
        mHttpServer.post("/guestGroup/merge", ListGuestInGuestGroups())

        mHttpServer.post("/guestGroup/payment/meals", ListMealsForPayment())
        mHttpServer.post("/guestGroup/payment/record", PostPaymentRecord())
    }
}