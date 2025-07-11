package com.example.myapplication.services;

import android.content.SharedPreferences;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {
    private Context context;

    public JwtInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (original.url().encodedPath().equals("/api/auth/login")) {
            return chain.proceed(original);
        }

        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt", null);
        Log.d("JwtInterceptor", "Token found: " + token);
        Log.d("JwtInterceptor", "Adding Authorization header: Bearer " + token);

        Request.Builder builder = original.newBuilder();

        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        Request request = builder.build();
        return chain.proceed(request);
    }
}
