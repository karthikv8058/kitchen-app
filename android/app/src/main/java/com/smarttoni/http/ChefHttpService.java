package com.smarttoni.http;

import com.smarttoni.pegion.PushMessage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ChefHttpService {

    @GET("ping")
    Call<Boolean> clientPing();


    @POST("push")
    Call<String> push(@Body PushMessage message);

}
