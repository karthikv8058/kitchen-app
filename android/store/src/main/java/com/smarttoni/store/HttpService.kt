package com.smarttoni.store

import retrofit2.Call
import retrofit2.http.GET

interface HttpService {
    @GET("/public/update/check")
    fun checkUpdate(): Call<StoreRequest>
}