package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.page.ProfileTypeActivity;

public class OrgActivityMandatory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_mandatory);

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrgActivityMandatory.this, OrgActivityOptional.class);
            startActivity(intent);
        });

        Button previousButton = findViewById(R.id.previous_button);

        previousButton.setOnClickListener(v ->{
            Intent intent = new Intent(OrgActivityMandatory.this, ProfileTypeActivity.class);
            startActivity(intent);
        });
    }
}
