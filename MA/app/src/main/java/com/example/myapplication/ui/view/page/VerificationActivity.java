package com.example.myapplication.ui.view.page;

import android.os.Bundle;
import android.widget.TextView;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.models.VerificationResponse;
import com.example.myapplication.data.services.user.VerificationService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerificationActivity extends AppCompatActivity {

    private TextView verificationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        verificationStatus = findViewById(R.id.verification_status);

        if (getIntent() != null && getIntent().getData() != null) {
            Uri data = getIntent().getData();
            String token = data.getQueryParameter("token");

            if (token != null) {
                VerificationService verificationService = new VerificationService();
                Call<VerificationResponse> call = verificationService.verifyToken(token);

                call.enqueue(new Callback<VerificationResponse>() {
                    @Override
                    public void onResponse(Call<VerificationResponse> call, Response<VerificationResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            verificationStatus.setText("Verification successful");
                        } else {
                            verificationStatus.setText("Verification failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<VerificationResponse> call, Throwable t) {
                        verificationStatus.setText("Verification failed: " + t.getMessage());
                    }
                });
            } else {
                verificationStatus.setText("No token provided in URL.");
            }
        } else {
            verificationStatus.setText("No token provided.");
        }
    }
}
