package com.example.myapplication.ui.view.page;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.services.VerificationService;

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

        String token = getIntent().getStringExtra("token");
        if (token != null) {
            VerificationService verificationService = new VerificationService();
            Call<String> call = verificationService.verifyToken(token);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        verificationStatus.setText("Verification successful");
                    } else {
                        verificationStatus.setText("Verification failed");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    verificationStatus.setText("Verification failed: " + t.getMessage());
                }
            });
        } else {
            verificationStatus.setText("No token provided.");
        }
    }
}
