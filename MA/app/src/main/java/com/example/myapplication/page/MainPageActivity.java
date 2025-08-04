package com.example.myapplication.page;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.ProfilePopupFragment;
import com.example.myapplication.R;
import com.example.myapplication.services.ChatWebsocketService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPageActivity extends AppCompatActivity {
    ChatWebsocketService chatWebsocketService = ChatWebsocketService.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new HomePage());
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.nav_home) {
                    selectedFragment = new HomePage();
                } else if (item.getItemId() == R.id.nav_events) {
                    selectedFragment = new EventsPage();
                } else if (item.getItemId() == R.id.nav_offerings) {
                    selectedFragment = new OfferingsPage();
                } else if (item.getItemId() == R.id.nav_profile) {
                    ProfilePopupFragment profilePopupFragment = new ProfilePopupFragment();
                    profilePopupFragment.show(getSupportFragmentManager(), profilePopupFragment.getTag());
                }

                return loadFragment(selectedFragment);
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getDisplay().getRealMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int drawerWidth = (int) (screenWidth * 0.9);

        DrawerLayout parentDrawer = findViewById(R.id.main);
        chatWebsocketService.getOpenChatTarget().observe(this,
                chatContactDTO -> {parentDrawer.openDrawer(GravityCompat.START);}
                );

        View drawer = findViewById(R.id.left_drawer);
        ViewGroup.LayoutParams params = drawer.getLayoutParams();
        params.width = drawerWidth;
        drawer.setLayoutParams(params);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChatFragment chatFragment = new ChatFragment();
        fragmentTransaction.replace(R.id.left_drawer, chatFragment);
        fragmentTransaction.commit();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}