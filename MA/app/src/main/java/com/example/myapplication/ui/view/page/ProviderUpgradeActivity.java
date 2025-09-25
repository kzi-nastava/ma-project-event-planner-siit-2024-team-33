package com.example.myapplication.ui.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.models.UpgradeRequest;
import com.example.myapplication.data.models.UpgradeUser;
import com.example.myapplication.ui.view.page.navigation.MainPageActivity;
import com.example.myapplication.data.services.AuthenticationService;
import com.example.myapplication.data.services.UsersService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderUpgradeActivity extends AppCompatActivity {

    private EditText nameInput, surnameInput, passwordInput, confirmPasswordInput,
            residencyInput, phoneInput, providerNameInput, descriptionInput;
    private TextView nameError, surnameError, passwordError, confirmPasswordError, residencyError, phoneError, descriptionError,providerNameError ;
    private final UsersService userService = new UsersService();
    private AuthentifiedUser authenticatedUser;
    private AuthenticationService authService;

    private Button confirmButton, prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_upgrade);

        authService = new AuthenticationService(this);

        nameInput = findViewById(R.id.inputName);
        surnameInput = findViewById(R.id.inputSurname);
        passwordInput = findViewById(R.id.inputPassword);
        confirmPasswordInput = findViewById(R.id.inputConfirmPassword);
        residencyInput = findViewById(R.id.inputResidency);
        phoneInput = findViewById(R.id.inputPhone);
        providerNameInput = findViewById(R.id.inputProviderName);
        descriptionInput = findViewById(R.id.inputDescription);

        confirmButton = findViewById(R.id.confirm_button);
        prevButton = findViewById(R.id.prev_button_second);

        residencyError = findViewById(R.id.residencyError);
        phoneError = findViewById(R.id.phoneError);
        descriptionError = findViewById(R.id.descriptionError);
        providerNameError = findViewById(R.id.providerNameError);
        nameError = findViewById(R.id.nameError);
        surnameError = findViewById(R.id.surnameError);
        passwordError = findViewById(R.id.passwordError);
        confirmPasswordError = findViewById(R.id.confirmPasswordError);

        authenticatedUser = authService.getLoggedInUser();
        if (authenticatedUser != null) {
            fillFormWithUser(authenticatedUser);
        }
        confirmButton.setEnabled(true);
        confirmButton.setOnClickListener(v -> onSubmit());
        prevButton.setOnClickListener(v -> onBackPressed());
    }


    private boolean validateFields() {
        boolean isValid = true;

        if (residencyInput.getText().toString().trim()
                .matches("^[A-Z][a-z]+( [A-Z][a-z]+)*, [A-Z][a-z]+( [A-Z][a-z]+)*$")) {
            residencyError.setVisibility(View.GONE);
        } else {
            residencyError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (phoneInput.getText().toString().trim().matches("^\\+\\d{10,15}$")) {
            phoneError.setVisibility(View.GONE);
        } else {
            phoneError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        String providerName = providerNameInput.getText().toString().trim();
        if (!providerName.isEmpty() && providerName.length() <= 20) {
            providerNameError.setVisibility(View.GONE);
        } else {
            providerNameError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        String description = descriptionInput.getText().toString().trim();
        if (description.length() >= 20) {
            descriptionError.setVisibility(View.GONE);
        } else {
            descriptionError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }


    private void onSubmit() {
        if (!validateFields()) {
            Toast.makeText(this, "Please fill out all required fields correctly.", Toast.LENGTH_SHORT).show();
            return;
        }

        UpgradeUser upgradeUser = new UpgradeUser(
                residencyInput.getText().toString().trim(),
                phoneInput.getText().toString().trim(),
                providerNameInput.getText().toString().trim(),
                descriptionInput.getText().toString().trim(),
                "PROVIDER_ROLE"
        );

        Call<UpgradeRequest> call = userService.upgradeUser(upgradeUser);
        call.enqueue(new Callback<UpgradeRequest>() {
            @Override
            public void onResponse(Call<UpgradeRequest> call, Response<UpgradeRequest> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProviderUpgradeActivity.this, "Upgrade completed successfully.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProviderUpgradeActivity.this, MainPageActivity.class));
                    finish();
                } else {
                    Toast.makeText(ProviderUpgradeActivity.this, "Upgrade failed: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpgradeRequest> call, Throwable t) {
                Toast.makeText(ProviderUpgradeActivity.this, "Upgrade failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showError(int errorViewId) {
        TextView errorView = findViewById(errorViewId);
        errorView.setVisibility(View.VISIBLE);
    }

    private void fillFormWithUser(AuthentifiedUser user) {
        nameInput.setText(user.getName());
        surnameInput.setText(user.getSurname());
        passwordInput.setText("********");
        confirmPasswordInput.setText("********");

        nameInput.setEnabled(false);
        surnameInput.setEnabled(false);
        passwordInput.setEnabled(false);
        confirmPasswordInput.setEnabled(false);
    }

}
