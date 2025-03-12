package com.example.myapplication34;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class DoctorDashboardActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Load default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.doctor_container, new ScheduleFragment())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();  // Get the ID of the selected item

                // Use if-else instead of switch
                if (itemId == R.id.navigation_schedule) {
                    selectedFragment = new ScheduleFragment();
                } else if (itemId == R.id.navigation_reports) {
                    selectedFragment = new ReportFragment();
                }

                // Replace the fragment
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.doctor_container, selectedFragment)
                            .commit();
                }

                return true;  // Return true to indicate the item was selected
            };
}