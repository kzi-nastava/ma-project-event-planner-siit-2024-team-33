package com.example.myapplication.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class UpgradeSelectionActivity extends AppCompatActivity {

    private RadioGroup profileRadioGroup;
    private RadioButton radioEventPlanner, radioOfferingProvider;
    private TextView errorMessage;
    private Button nextButton, prevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_user);

        profileRadioGroup = findViewById(R.id.profileRadioGroup);
        radioEventPlanner = findViewById(R.id.radio_ep);
        radioOfferingProvider = findViewById(R.id.radio_op);
        errorMessage = findViewById(R.id.errorMessage);
        nextButton = findViewById(R.id.next_button);
        prevButton = findViewById(R.id.prev_button);

        nextButton.setOnClickListener(v -> onNextClicked());
        prevButton.setOnClickListener(v -> onBackPressed());
    }

    private void onNextClicked() {
        int selectedId = profileRadioGroup.getCheckedRadioButtonId();

        if (selectedId == -1) {
            errorMessage.setVisibility(View.VISIBLE);
            return;
        }

        errorMessage.setVisibility(View.GONE);

        if (selectedId == R.id.radio_ep) {
            Intent intent = new Intent(this, UpgradeActivity.class);
            intent.putExtra("UPGRADE_ROLE", "ORGANIZER_ROLE");
            startActivity(intent);
        } else if (selectedId == R.id.radio_op) {
            Intent intent = new Intent(this, ProviderUpgradeActivity.class);
            intent.putExtra("UPGRADE_ROLE", "PROVIDER_ROLE");
            startActivity(intent);
        }
    }
}
