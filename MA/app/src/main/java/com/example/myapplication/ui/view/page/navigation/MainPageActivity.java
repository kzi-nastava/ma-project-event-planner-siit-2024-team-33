package com.example.myapplication.ui.view.page.navigation;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.ui.view.page.communication.ChatFragment;
import com.example.myapplication.ui.view.page.event.EventsPage;
import com.example.myapplication.ui.view.page.home.HomePage;
import com.example.myapplication.ui.view.page.services.OfferingsPage;
import com.example.myapplication.R;
import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.data.services.authentication.AuthenticationService;
import com.example.myapplication.data.services.communication.ChatWebsocketService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPageActivity extends AppCompatActivity {

    ChatWebsocketService chatWebsocketService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        AuthenticationService as = new AuthenticationService(getApplicationContext());
        AuthentifiedUser user = AuthenticationService.getLoggedInUser();
        ApiClient.getClient(getApplicationContext());

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
                    selectedFragment = EventsPage.newInstance();
                } else if (item.getItemId() == R.id.nav_offerings) {
                    selectedFragment = OfferingsPage.newInstance();
                } else if (item.getItemId() == R.id.nav_profile) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.right_drawer, new ProfilePopupFragment())
                            .commit();

                    DrawerLayout drawerLayout = findViewById(R.id.main);
                    drawerLayout.openDrawer(GravityCompat.END);
                    return true;
                }

                return loadFragment(selectedFragment);
            }
        });

        //how to open sides
        DisplayMetrics metrics = new DisplayMetrics();
        getDisplay().getRealMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        int drawerWidth = (int) (screenWidth * 0.9);

        chatWebsocketService = ChatWebsocketService.getInstance();
        DrawerLayout parentDrawer = findViewById(R.id.main);
        chatWebsocketService.getOpenChatTarget().observe(this,
                chatContactDTO -> {parentDrawer.openDrawer(GravityCompat.START);}
                );

        View drawer = findViewById(R.id.left_drawer);
        ViewGroup.LayoutParams params = drawer.getLayoutParams();
        params.width = drawerWidth;
        drawer.setLayoutParams(params);

        View rightDrawer = findViewById(R.id.right_drawer);
        ViewGroup.LayoutParams paramsRight = rightDrawer.getLayoutParams();
        paramsRight.width = (int) (screenWidth * 0.8);
        rightDrawer.setLayoutParams(paramsRight);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChatFragment chatFragment = new ChatFragment();
        fragmentTransaction.replace(R.id.left_drawer, chatFragment);
        fragmentTransaction.commit();
    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}