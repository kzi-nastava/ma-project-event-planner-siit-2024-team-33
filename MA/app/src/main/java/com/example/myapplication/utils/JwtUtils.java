package com.example.myapplication.utils;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class JwtUtils {
    public static String getRoleFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE), StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(payload);
            JSONArray roles = json.getJSONArray("role");
            return roles.getString(0);
        } catch (Exception e) {
            return null;
        }
    }
}

