package com.example.myapplication.ui.viewmodel.profile;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.services.AuthenticationService;

public class ProfilePasswordChangeViewModel extends ViewModel {

    private final MutableLiveData<String> _passwordError = new MutableLiveData<>(null);
    public LiveData<String> passwordError = _passwordError;

    private final MutableLiveData<String> _confirmPasswordError = new MutableLiveData<>(null);
    public LiveData<String> confirmPasswordError = _confirmPasswordError;

    private final MutableLiveData<Boolean> _isFormValid = new MutableLiveData<>(false);
    public LiveData<Boolean> isFormValid = _isFormValid;

    private final MutableLiveData<AuthentifiedUser> _user = new MutableLiveData<>();
    public LiveData<AuthentifiedUser> user = _user;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private AuthenticationService authService;

    public void init(Context context) {
        if (authService == null) {
            authService = new AuthenticationService(context);
        }
    }

    public void validatePasswords(String newPassword, String confirmPassword) {
        boolean valid = true;

        String np = newPassword != null ? newPassword : "";
        if (!np.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$")) {
            _passwordError.setValue("Password must be 8-16 chars, letters & numbers");
            valid = false;
        } else {
            _passwordError.setValue(null);
        }

        String cp = confirmPassword != null ? confirmPassword : "";
        if (!cp.equals(np)) {
            _confirmPasswordError.setValue("Passwords do not match");
            valid = false;
        } else {
            _confirmPasswordError.setValue(null);
        }

        _isFormValid.setValue(valid);
    }

    public void changePassword(String oldPass, String newPass) {
        authService.changePassword(oldPass, newPass, new AuthenticationService.PasswordChangeCallback() {
            @Override
            public void onPasswordChangeSuccess(AuthentifiedUser updatedUser) {
                _user.postValue(updatedUser);
            }

            @Override
            public void onPasswordChangeError(String errorMessage) {
                _error.postValue(errorMessage);
            }
        });
    }

}
