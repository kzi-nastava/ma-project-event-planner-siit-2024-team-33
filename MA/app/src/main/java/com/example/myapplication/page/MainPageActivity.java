package com.example.myapplication.page;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.myapplication.ProfilePopupFragment;
import com.example.myapplication.R;
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
                else if (item.getItemId() == R.id.nav_profile) {
                    ProfilePopupFragment profilePopupFragment = new ProfilePopupFragment();
                    profilePopupFragment.show(getSupportFragmentManager(), profilePopupFragment.getTag());
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