package com.example.myapplication;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set the default Fragment
        if (savedInstanceState == null) {
            loadFragment(new HomePage());
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_home)
                {
                    selectedFragment = new HomePage();
                }
                else if (item.getItemId() == R.id.nav_events)
                {
                    selectedFragment = new EventsPage();
                }
                else if (item.getItemId() == R.id.nav_offerings)
                {
                    selectedFragment = new OfferingsPage();
                }
                else if (item.getItemId() == R.id.nav_profile)
                {
                    selectedFragment = new ProfilePage();
                }

                return loadFragment(selectedFragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        // Replace the current Fragment with the new one
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}