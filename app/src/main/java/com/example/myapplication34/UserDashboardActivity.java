package com.example.myapplication34;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;


public class UserDashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set default fragment
        loadFragment(new UserHomeFragment());

        // Set up bottom navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new UserHomeFragment();
                } else if (itemId == R.id.nav_find_doctor) {
                    selectedFragment = new FindDoctorFragment();
                } else if (itemId == R.id.nav_appointment2) {
                    selectedFragment = new AppointmentFragment();
                } else if (itemId == R.id.nav_history2) {
                    selectedFragment = new HistoryFragment();
                } else if (itemId == R.id.nav_profile2) {
                    selectedFragment = new ProfileFragment2();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}