package com.example.myapplication.page;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.AuthentifiedUser;
import com.example.myapplication.models.UpgradeUser;
import com.example.myapplication.models.UpgradeRequest;
import com.example.myapplication.services.UsersService;
import com.example.myapplication.services.AuthenticationService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpgradeActivity extends AppCompatActivity {

    private EditText inputName, inputSurname, inputPassword, inputConfirmPassword, inputResidency, inputPhone;
    private TextView nameError, surnameError, passwordError, confirmPasswordError, residencyError, phoneError;
    private Button confirmButton, prevButton;

    private AuthentifiedUser authenticatedUser;
    private final UsersService userService = new UsersService();
    private AuthenticationService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_planner_upgrade);

        authService = new AuthenticationService(this);
        bindViews();
        setupListeners();

        authenticatedUser = authService.getLoggedInUser();

        if (authenticatedUser != null) {
            fillFormWithUser(authenticatedUser);
        }
    }

    private void bindViews() {
        inputName = findViewById(R.id.inputName);
        inputSurname = findViewById(R.id.inputSurname);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        inputResidency = findViewById(R.id.inputResidency);
        inputPhone = findViewById(R.id.inputPhone);

        nameError = findViewById(R.id.nameError);
        surnameError = findViewById(R.id.surnameError);
        passwordError = findViewById(R.id.passwordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);
        residencyError = findViewById(R.id.residencyError);
        phoneError = findViewById(R.id.phoneError);

        confirmButton = findViewById(R.id.confirm_button);
        prevButton = findViewById(R.id.prev_button_second);
    }

    private void setupListeners() {
        TextWatcher watcher = new ValidationWatcher();
        inputName.addTextChangedListener(watcher);
        inputSurname.addTextChangedListener(watcher);
        inputPassword.addTextChangedListener(watcher);
        inputConfirmPassword.addTextChangedListener(watcher);
        inputResidency.addTextChangedListener(watcher);
        inputPhone.addTextChangedListener(watcher);

        confirmButton.setOnClickListener(v -> onConfirm());
        prevButton.setOnClickListener(v -> onBackPressed());
    }

    private void fillFormWithUser(AuthentifiedUser user) {
        inputName.setText(user.getName());
        inputSurname.setText(user.getSurname());

        inputName.setEnabled(false);
        inputSurname.setEnabled(false);
        inputPassword.setEnabled(false);
        inputConfirmPassword.setEnabled(false);
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (inputResidency.getText().toString().trim().matches("^[A-Z][a-z]+( [A-Z][a-z]+)*, [A-Z][a-z]+( [A-Z][a-z]+)*$")) {
            residencyError.setVisibility(View.GONE);
        } else {
            residencyError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (inputPhone.getText().toString().trim().matches("^\\+\\d{10,15}$")) {
            phoneError.setVisibility(View.GONE);
        } else {
            phoneError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        confirmButton.setEnabled(isValid);
        return isValid;
    }

    private void onConfirm() {
        if (!validateFields()) {
            Toast.makeText(this, "Please fill out all required fields correctly.", Toast.LENGTH_SHORT).show();
            return;
        }

        UpgradeUser upgradeUser = new UpgradeUser(
                inputResidency.getText().toString().trim(),
                inputPhone.getText().toString().trim(),
                "", "", "ORGANIZER_ROLE"
        );

        Call<UpgradeRequest> call = userService.upgradeUser(upgradeUser);
        call.enqueue(new Callback<UpgradeRequest>() {
            @Override
            public void onResponse(Call<UpgradeRequest> call, Response<UpgradeRequest> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpgradeActivity.this, "Upgrade completed successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpgradeActivity.this, MainPageActivity.class));
                    finish();
                } else {
                    Toast.makeText(UpgradeActivity.this, "Upgrade failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpgradeRequest> call, Throwable t) {
                Toast.makeText(UpgradeActivity.this, "Upgrade failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class ValidationWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            validateFields();
        }
    }
}
