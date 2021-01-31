package com.smarttoni.store

import com.google.gson.GsonBuilder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HttpClient {
    val liveHttpClient: HttpService
        get() {
            val builder = GsonBuilder()
            val gson = builder.create()
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://api.smarttoni.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            return retrofit.create(HttpService::class.java)
        }

    val uatHttpClient: HttpService
        get() {
            val builder = GsonBuilder()
            val gson = builder.create()
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://demo.mypits.org:14058/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            return retrofit.create(HttpService::class.java)
        }

    val devHttpClient: HttpService
        get() {
            val builder = GsonBuilder()
            val gson = builder.create()
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://demo.mypits.org:14052/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            return retrofit.create(HttpService::class.java)
        }
}