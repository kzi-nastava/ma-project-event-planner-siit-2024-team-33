package com.example.myapplication.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.dto.userDTO.GetUserDTO;
import com.example.myapplication.services.AuthenticationService;

import com.example.myapplication.dto.LoginDTO.AuthResponse;
import com.example.myapplication.services.ApiClient;
import com.example.myapplication.api.UserApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        registerText = findViewById(R.id.login_register_text);


        Uri data = getIntent().getData();
        if (data != null) {
            String emailFromLink = data.getQueryParameter("email");
            if (emailFromLink != null) {
                emailInput.setText(emailFromLink);
            }
        }

        loginButton.setOnClickListener(v -> performLogin());
        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ProfileTypeActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        Log.d("LoginActivity", "performLogin called");

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        AuthenticationService authServiceWrapper = new AuthenticationService(this);
        authServiceWrapper.login(email, password, new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();
                    Log.d("LoginActivity", "Token received: " + token);

                    UserApi userApi = ApiClient.getClient(LoginActivity.this).create(UserApi.class);
                    userApi.getCurrentUser().enqueue(new Callback<GetUserDTO>() {
                        @Override
                        public void onResponse(Call<GetUserDTO> call, Response<GetUserDTO> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                GetUserDTO user = response.body();
                                Log.d("LoginActivity", "Logged in as: " + user.getEmail());

                                Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Log.e("LoginActivity", "getCurrentUser failed: " + response.code());
                                Toast.makeText(LoginActivity.this, "User fetch failed: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GetUserDTO> call, Throwable t) {
                            Log.e("LoginActivity", "getCurrentUser failed", t);
                            Toast.makeText(LoginActivity.this, "User fetch error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "no error body";
                        Log.e("LoginActivity", "Login failed: " + response.code() + " - " + errorBody);
                        Toast.makeText(LoginActivity.this, "Login failed: " + response.code(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e("LoginActivity", "Login error parsing failed", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("LoginActivity", "Login API call failed: " + t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Login failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



}
