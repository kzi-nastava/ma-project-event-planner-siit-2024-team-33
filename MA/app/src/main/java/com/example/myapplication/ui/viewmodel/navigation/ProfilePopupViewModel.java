package com.example.myapplication.ui.viewmodel.navigation;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.services.authentication.AuthenticationService;
import com.google.gson.Gson;

public class ProfilePopupViewModel extends ViewModel {
    private final MutableLiveData<AuthentifiedUser> userLiveData = new MutableLiveData<>();

    public LiveData<AuthentifiedUser> getUser() {
        return userLiveData;
    }

    public void loadUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
        String json = prefs.getString("user", null);
        if (json != null) {
            Gson gson = new Gson();
            AuthentifiedUser user = gson.fromJson(json, AuthentifiedUser.class);
            userLiveData.setValue(user);
        } else {
            userLiveData.setValue(null);
        }
    }

    public void logout(Context context) {
        AuthenticationService authenticationService = new AuthenticationService(context);
        authenticationService.logout();
        userLiveData.setValue(null);
    }

    public boolean isOrganizer() {
        return userLiveData.getValue() != null && "ORGANIZER_ROLE".equals(userLiveData.getValue().getRole().getName());
    }

    public boolean isProvider() {
        return userLiveData.getValue() != null && "PROVIDER_ROLE".equals(userLiveData.getValue().getRole().getName());
    }

    public boolean isAdmin() {
        return userLiveData.getValue() != null && "ADMIN_ROLE".equals(userLiveData.getValue().getRole().getName());
    }

    public boolean isRegularUser() {
        return userLiveData.getValue() != null && "AUSER_ROLE".equals(userLiveData.getValue().getRole().getName());
    }

    public boolean isLoggedIn() {
        return isOrganizer() || isProvider() || isAdmin() || isRegularUser();
    }

}
