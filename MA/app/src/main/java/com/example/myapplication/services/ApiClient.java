package com.example.myapplication.services;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

public class ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient clients = null;

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
                    .baseUrl("http://192.168.2.8:8080/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofit(String baseurl){
        return new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(clients)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void reset() {
        retrofit = null;
    }
}