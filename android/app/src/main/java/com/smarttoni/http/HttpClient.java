package com.smarttoni.http;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.smarttoni.BuildConfig;
import com.smarttoni.utils.HttpHelper;
import com.smarttoni.utils.LocalStorage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient {

    private Context context;
    private LocalStorage localStorage;

    public HttpClient(Context context) {
        this.context = context;
        this.localStorage = new LocalStorage(context);
    }

    public HttpService getHttpClient() {

        LocalStorage localStorage =  new LocalStorage(context);
        OkHttpClient client = new OkHttpClient
                .Builder()

                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)

                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization", "Bearer " + localStorage.getAuthToken())
                                .addHeader("Server-Signature", localStorage.getString(LocalStorage.RESTAURANT_TOKEN) )
                                .addHeader("locale", "en")
                                .build();
                        HttpUrl url = newRequest.url().newBuilder().addQueryParameter("restaurant_id", String.valueOf(localStorage.getRestaurantId())).build();

                        newRequest.newBuilder().url(url).build();
                        return chain.proceed(newRequest);
                    }

                }).build();


        GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(boolean.class, new BooleanTypeAdapter());
        builder.registerTypeAdapter(Date.class, new DateTypeAdapter());
        Gson gson = builder.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(HttpService.class);
    }

    public HttpService getPushHttpClient(String host) {

        OkHttpClient client = new OkHttpClient
                .Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + new LocalStorage(context).getAuthToken())
                        .build();
                HttpUrl url = newRequest.url().newBuilder().addQueryParameter("restaurant_id", String.valueOf(localStorage.getRestaurantId())).build();
                newRequest.newBuilder().url(url).build();
                return chain.proceed(newRequest);
            }
        }).build();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + host + ":8889/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(HttpService.class);
    }

//    class BooleanTypeAdapter implements JsonDeserializer<Boolean> {
//        @Override
//        public Boolean deserialize(JsonElement json, Type typeOfT,
//                                   JsonDeserializationContext context) throws JsonParseException {
//            int code = json.getAsInt();
//            return code != 0;
//        }
//    }

    class DateTypeAdapter implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {
            String code = json.getAsString();
            return HttpHelper.convertMysqlDateToDate(code);
        }
    }
}