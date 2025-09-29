package com.example.myapplication.data.services.authentication;

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

        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt", null);

        Request.Builder builder = original.newBuilder();

        if (token != null && !token.isEmpty()) {
            builder.header("Authorization", "Bearer " + token);
        } else {
            Log.d("JwtInterceptor", "No token found, proceeding without Authorization header.");
        }

        return chain.proceed(builder.build());
    }
}
