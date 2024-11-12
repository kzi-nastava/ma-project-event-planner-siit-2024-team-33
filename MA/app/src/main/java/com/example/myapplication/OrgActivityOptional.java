package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class OrgActivityOptional extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_optional);

        Button previousButton = findViewById(R.id.previous_button);
        previousButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrgActivityOptional.this, OrgActivityMandatory.class);
            startActivity(intent);
        });
        Button nextButton = findViewById(R.id.confirm_button);
        nextButton.setOnClickListener(v ->{
            Intent intent = new Intent(OrgActivityOptional.this,RegisterActivity.class);
            startActivity(intent);
        });
    }
}
