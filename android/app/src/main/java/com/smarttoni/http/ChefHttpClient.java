package com.smarttoni.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChefHttpClient {
    public static final String HTTP_PORT = "8889";
    //private static ChefHttpService httpService;

    public ChefHttpService getHttpClient(String clientIp) {

//        if (httpService == null ) {
//            String url = "http://" + clientIp + ":" + HTTP_PORT + "/";
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(url)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            httpService = retrofit.create(ChefHttpService.class);
//        }
        String url = "http://" + clientIp + ":" + HTTP_PORT + "/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ChefHttpService.class);
    }
}
