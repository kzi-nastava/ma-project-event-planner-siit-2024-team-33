package com.example.myapplication.ui.viewmodel.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.services.authentication.AuthenticationService;

public class LoginViewModel extends AndroidViewModel implements AuthenticationService.LoginCallback {
    private final AuthenticationService authService;
    private final MutableLiveData<Boolean> loginSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> loginError = new MutableLiveData<>();
    private final MutableLiveData<AuthentifiedUser> loggedUser = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authService = new AuthenticationService(application.getApplicationContext());
    }

    public LiveData<Boolean> getLoginSuccess() { return loginSuccess; }
    public LiveData<String> getLoginError() { return loginError; }
    public LiveData<AuthentifiedUser> getLoggedUser() { return loggedUser; }

    public void login(String email, String password) {
        authService.login(email, password, this);
    }

    @Override
    public void onLoginSuccess(AuthentifiedUser user) {
        //updating liveData
        loggedUser.postValue(user);
        loginSuccess.postValue(true);
    }

    @Override
    public void onLoginError(String errorMessage) {
        //updating liveData
        loginError.postValue(errorMessage);
        loginSuccess.postValue(false);
    }
}
