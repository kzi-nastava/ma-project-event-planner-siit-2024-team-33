package com.example.myapplication.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

import com.example.myapplication.utils.Settings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.LocalTime;
import com.google.gson.JsonDeserializer;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient clients = null;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                    LocalDate.parse(json.getAsJsonPrimitive().getAsString()))
            .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, type, context) ->
                    LocalTime.parse(json.getAsJsonPrimitive().getAsString()))
            .create();

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new JwtInterceptor(context))
                    .addInterceptor(logging)
                    .build();

            clients = client;

            retrofit = new Retrofit.Builder()
                    .baseUrl(Settings.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofit(String baseurl){
        return new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(clients)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static void reset() {
        retrofit = null;
    }
}
