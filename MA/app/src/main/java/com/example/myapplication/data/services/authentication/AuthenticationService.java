package com.example.myapplication.data.services.authentication;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.data.api.authentication.AuthApi;
import com.example.myapplication.data.api.user.UserApi;
import com.example.myapplication.data.models.dto.LoginDTO.AuthResponse;
import com.example.myapplication.data.models.dto.LoginDTO.LoginRequest;
import com.example.myapplication.data.models.dto.userDTO.GetUserDTO;
import com.example.myapplication.data.models.dto.userDTO.UpdatePassword;
import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.data.services.communication.ChatWebsocketService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationService {

    public interface LoginCallback {
        void onLoginSuccess(AuthentifiedUser user);
        void onLoginError(String errorMessage);
    }

    public interface EmailCheckCallback {
        void onResult(boolean exists);
        void onError(String message);
    }

    public interface PasswordChangeCallback {
        void onPasswordChangeSuccess(AuthentifiedUser user);
        void onPasswordChangeError(String errorMessage);
    }

    private static AuthApi authApi;
    private static ChatWebsocketService chatWebsocketService;
    private static SharedPreferences prefs;
    private static Context context;

    private static final String PREFS_NAME = "auth";
    private static final String KEY_JWT = "jwt";
    private static final String KEY_ROLE = "role";

    public AuthenticationService(Context context) {
        AuthenticationService.context = context.getApplicationContext();
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        authApi = ApiClient.getClient(context).create(AuthApi.class);
    }

    //login and setup user
    public void login(String email, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(email, password);

        authApi.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();
                    prefs.edit().putString(KEY_JWT, token).apply();

                    //connect websockets
                    chatWebsocketService = ChatWebsocketService.getInstance();
                    chatWebsocketService.connect();

                    fetchCurrentUser(callback);
                } else {
                    callback.onLoginError("Login failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onLoginError("Login API call failed: " + t.getMessage());
            }
        });
    }


    //updating password and getting new jwt
    public void changePassword(String oldPassword, String newPassword, PasswordChangeCallback callback) {
        UpdatePassword request = new UpdatePassword();
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);

        UserApi userApi = ApiClient.getClient(context).create(UserApi.class);
        userApi.updatePassword(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();
                    prefs.edit().putString(KEY_JWT, token).apply();

                    //reconnect WebSocket
                    chatWebsocketService = ChatWebsocketService.getInstance();
                    chatWebsocketService.connect();

                    //fetch user again to refresh cache
                    fetchCurrentUser(new LoginCallback() {
                        @Override
                        public void onLoginSuccess(AuthentifiedUser user) {
                            callback.onPasswordChangeSuccess(user);
                        }

                        @Override
                        public void onLoginError(String errorMessage) {
                            callback.onPasswordChangeError(errorMessage);
                        }
                    });
                } else {
                    callback.onPasswordChangeError("Password change failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onPasswordChangeError("API call failed: " + t.getMessage());
            }
        });
    }

    private void fetchCurrentUser(LoginCallback callback) {
        UserApi userApi = ApiClient.getClient(context).create(UserApi.class);
        userApi.getCurrentUser().enqueue(new Callback<GetUserDTO>() {
            @Override
            public void onResponse(Call<GetUserDTO> call, Response<GetUserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthentifiedUser user = new AuthentifiedUser(response.body());
                    prefs.edit().putString("user", new Gson().toJson(user)).apply();
                    callback.onLoginSuccess(user);
                } else {
                    callback.onLoginError("Failed to fetch user info.");
                }
            }

            @Override
            public void onFailure(Call<GetUserDTO> call, Throwable t) {
                callback.onLoginError("Failed to fetch user: " + t.getMessage());
            }
        });
    }

    //async email availability check
    public void checkEmailAvailability(String email, EmailCheckCallback callback) {
        authApi.checkEmailAvailability(email).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(response.body());
                } else {
                    callback.onError("Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void saveUserToStorage(AuthentifiedUser user) {
        if (prefs != null && user != null) {
            prefs.edit().putString("user", new Gson().toJson(user)).apply();
        }
    }

    public static boolean isLoggedIn() {
        String token = getJwtToken();
        return token != null && !token.isEmpty();
    }

    public static AuthentifiedUser getLoggedInUser() {
        String json = prefs.getString("user", null);
        if (json != null) {
            return new Gson().fromJson(json, AuthentifiedUser.class);
        }
        return null;
    }

    public static String getJwtToken() {
        if (prefs == null && context != null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        return prefs != null ? prefs.getString(KEY_JWT, null) : null;
    }

    public void logout() {
        prefs.edit()
                .remove(KEY_JWT)
                .remove(KEY_ROLE)
                .remove("user")
                .apply();

        chatWebsocketService = ChatWebsocketService.getInstance();
        chatWebsocketService.disconnect();
    }
}
