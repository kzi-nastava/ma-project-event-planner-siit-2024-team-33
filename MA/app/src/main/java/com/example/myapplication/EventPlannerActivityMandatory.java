package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;

public class EventPlannerActivityMandatory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_planner_mandatory);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button previousButton = findViewById(R.id.previous_button);
        Button nextButton = findViewById(R.id.next_button);

        previousButton.setOnClickListener(view -> {
            Intent intent = new Intent(EventPlannerActivityMandatory.this, ProfileTypeActivity.class);
            startActivity(intent);
            finish();
        });

        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(EventPlannerActivityMandatory.this, EventPlannerActivityOptional.class);
            startActivity(intent);
        });
    }
}
