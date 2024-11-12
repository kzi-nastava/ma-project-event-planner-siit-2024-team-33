package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileTypeActivity extends AppCompatActivity {

    private RadioGroup profileTypeGroup;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_type);

        profileTypeGroup = findViewById(R.id.profile_type_group);
        nextButton = findViewById(R.id.button_next);

        nextButton.setOnClickListener(v -> {
            int selectedProfileId = profileTypeGroup.getCheckedRadioButtonId();

            if (selectedProfileId == R.id.radio_event_planner) {
                Intent intent = new Intent(ProfileTypeActivity.this, EventPlannerActivityMandatory.class);
                startActivity(intent);
            } else if (selectedProfileId == R.id.radio_product_seller) {
                Intent intent = new Intent(ProfileTypeActivity.this, OrgActivityMandatory.class);
                startActivity(intent);
            }
        });
    }
}
