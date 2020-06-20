package com.hang.doan.readbooks.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hang.doan.readbooks.Fragment.AccountFragment;
import com.hang.doan.readbooks.Fragment.HomeFragment;
import com.hang.doan.readbooks.Fragment.LibraryFragment;
import com.hang.doan.readbooks.Fragment.NotificationFragment;
import com.hang.doan.readbooks.Fragment.WriteFragment;
import com.hang.doan.readbooks.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //bottomNav.setBackgroundColor(Color.parseColor("#f1b814"));

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AccountFragment()).commit();
        }

        bottomNav.setSelectedItemId(R.id.nav_home);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_account:
                            selectedFragment = new AccountFragment();
                            break;
                        case R.id.nav_writeNew:
                            selectedFragment = new WriteFragment();
                            break;
                        case R.id.nav_library:
                            selectedFragment = new LibraryFragment();
                            break;
                        case R.id.nav_notify:
                            selectedFragment = new NotificationFragment();
                            break;
                        default:
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                            .addToBackStack(null)
                            .commit();

                    return true;
                }
            };
}
