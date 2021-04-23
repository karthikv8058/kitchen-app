package com.smarttoni.guestgroup

import android.content.Context
import com.koushikdutta.async.http.server.AsyncHttpServer
import com.smarttoni.guestgroup.controllers.*

object GuestGroupServer {
    fun addControllers(mHttpServer: AsyncHttpServer, context: Context?) {
        mHttpServer.get("/guestGroup/list", ListGuestGroups())
        mHttpServer.post("/guestGroup/guests", ListGuestInGuestGroups())
        mHttpServer.post("/guestGroup/split", ListGuestInGuestGroups())
        mHttpServer.post("/guestGroup/merge", ListGuestInGuestGroups())
        mHttpServer.post("/guestGroup/qr", PostFetchQRCode())

        mHttpServer.post("/guestGroup/payment/meals", ListMealsForPayment())
        mHttpServer.post("/guestGroup/payment/record", PostPaymentRecord())
    }
}