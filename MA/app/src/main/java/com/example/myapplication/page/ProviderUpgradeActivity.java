package com.example.myapplication.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.models.UpgradeRequest;
import com.example.myapplication.models.UpgradeUser;
import com.example.myapplication.services.UsersService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderUpgradeActivity extends AppCompatActivity {

    private EditText nameInput, surnameInput, passwordInput, confirmPasswordInput,
            residencyInput, phoneInput, providerNameInput, descriptionInput;
    private TextView nameError, surnameError, passwordError, confirmPasswordError, residencyError, phoneError, descriptionError,providerNameError ;
    private final UsersService userService = new UsersService();

    private Button confirmButton, prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_upgrade);  // Note: different XML layout

        nameInput = findViewById(R.id.inputName);
        surnameInput = findViewById(R.id.inputSurname);
        passwordInput = findViewById(R.id.inputPassword);
        confirmPasswordInput = findViewById(R.id.inputConfirmPassword);
        residencyInput = findViewById(R.id.inputResidency);
        phoneInput = findViewById(R.id.inputPhone);

        providerNameInput = findViewById(R.id.inputProviderName); // New
        descriptionInput = findViewById(R.id.inputDescription);   // New

        confirmButton = findViewById(R.id.confirm_button);
        prevButton = findViewById(R.id.prev_button_second);

        prevButton.setOnClickListener(v -> onBackPressed());

        // Add validation or logic here
        confirmButton.setOnClickListener(v -> onSubmit());
    }

    private boolean validateFields() {
        boolean isValid = true;

        // Residency format: "City, Country" with capitalized words
        if (residencyInput.getText().toString().trim()
                .matches("^[A-Z][a-z]+( [A-Z][a-z]+)*, [A-Z][a-z]+( [A-Z][a-z]+)*$")) {
            residencyError.setVisibility(View.GONE);
        } else {
            residencyError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Phone format: + followed by 10 to 15 digits
        if (phoneInput.getText().toString().trim().matches("^\\+\\d{10,15}$")) {
            phoneError.setVisibility(View.GONE);
        } else {
            phoneError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Provider name: max 20 characters
        String providerName = providerNameInput.getText().toString().trim();
        if (!providerName.isEmpty() && providerName.length() <= 20) {
            providerNameError.setVisibility(View.GONE);
        } else {
            providerNameError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Description: at least 20 characters
        String description = descriptionInput.getText().toString().trim();
        if (description.length() >= 20) {
            descriptionError.setVisibility(View.GONE);
        } else {
            descriptionError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        confirmButton.setEnabled(isValid);
        return isValid;
    }


    private void onSubmit() {
        // Basic placeholder validation
        boolean isValid = true;

        if (nameInput.getText().toString().trim().isEmpty()) {
            showError(R.id.nameError);
            isValid = false;
        }
        // Add similar validation for other fields...

        if (providerNameInput.getText().toString().trim().isEmpty()) {
            showError(R.id.providerNameError);
            isValid = false;
        }

        if (descriptionInput.getText().toString().trim().length() < 20) {
            showError(R.id.descriptionError);
            isValid = false;
        }

        if (isValid) {
            Toast.makeText(this, "Upgrade confirmed!", Toast.LENGTH_SHORT).show();
            // You can continue to another activity or submit data
        }
        UpgradeUser upgradeUser = new UpgradeUser(
                residencyInput.getText().toString().trim(),
                phoneInput.getText().toString().trim(),
                providerNameInput.getText().toString().trim(),
                descriptionInput.getText().toString().trim(),
                "ORGANIZER_ROLE"
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
}
