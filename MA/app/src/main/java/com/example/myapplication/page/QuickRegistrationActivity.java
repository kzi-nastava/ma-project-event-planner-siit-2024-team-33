package com.example.myapplication.page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.dto.userDTO.RegisterUser;
import com.example.myapplication.dto.userDTO.RegisteredUser;
import com.example.myapplication.services.UsersService;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuickRegistrationActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText nameInput;
    private EditText surnameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private UsersService usersService;
    private Button confirmButton;
    private ImageView profileImagePreview;
    private Button selectProfileImageBtn;

    private boolean hasImageSelected = false;
    private String selectedImageUrl = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quick_registration);
        usersService = new UsersService();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailInput = findViewById(R.id.email_input);
        nameInput = findViewById(R.id.name_input);
        surnameInput = findViewById(R.id.surname_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        confirmButton = findViewById(R.id.confirm_button);


        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setFocusable(false);
        emailInput.setClickable(false);

        Uri data = getIntent().getData();
        if (data != null) {
            String emailFromLink = data.getQueryParameter("email");
            if (emailFromLink != null) {
                emailInput.setText(emailFromLink);
            }
        }

        confirmButton.setOnClickListener(v -> {
            if (validateInputs()) {
                performRegistration();
            }
        });
    }

    private boolean validateInputs() {
        if (emailInput.getText().toString().trim().isEmpty()) {
            emailInput.setError("Email is required");
            return false;
        }
        if (nameInput.getText().toString().trim().isEmpty()) {
            nameInput.setError("Name is required");
            return false;
        }
        if (surnameInput.getText().toString().trim().isEmpty()) {
            surnameInput.setError("Surname is required");
            return false;
        }
        String pass = passwordInput.getText().toString();
        String confirmPass = confirmPasswordInput.getText().toString();
        if (pass.isEmpty()) {
            passwordInput.setError("Password is required");
            return false;
        }
        if (!pass.equals(confirmPass)) {
            confirmPasswordInput.setError("Passwords do not match");
            return false;
        }
        return true;
    }


    private void performRegistration() {
        String email = emailInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String surname = surnameInput.getText().toString().trim();
        String password = passwordInput.getText().toString();

        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail(email);
        registerUser.setName(name);
        registerUser.setSurname(surname);
        registerUser.setPassword(password);
        registerUser.setPicture("");
        registerUser.setRole("AUSER_ROLE");
        
        Call<RegisteredUser> call = usersService.signup(registerUser);
        call.enqueue(new Callback<RegisteredUser>() {
            @Override
            public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                if (response.isSuccessful()) {
                    Snackbar.make(confirmButton, "Registration successful!", Snackbar.LENGTH_LONG).show();
                    Intent intent = new Intent(QuickRegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Toast.makeText(QuickRegistrationActivity.this, "Registration failed: " + errorBody, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(QuickRegistrationActivity.this, "Registration failed: unknown error", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
                Toast.makeText(QuickRegistrationActivity.this, "Registration error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
