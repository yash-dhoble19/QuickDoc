package com.example.myapplication34;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class AdminDashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String adminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Retrieve the admin's email from the intent
        adminEmail = getIntent().getStringExtra("ADMIN_EMAIL");

        // Load the AppointmentsFragment and pass the admin's email
        AppointmentsFragment appointmentsFragment = new AppointmentsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ADMIN_EMAIL", adminEmail);
        appointmentsFragment.setArguments(bundle);
        loadFragment(appointmentsFragment); // Load the fragment into the container

        // Set up bottom navigation listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId(); // Get the ID of the selected item

                if (itemId == R.id.nav_appointments) {
                    selectedFragment = new AppointmentsFragment();
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                }  else if (itemId == R.id.nav_list) {
                    selectedFragment = new ListFragment();
                } else if (itemId == R.id.nav_history) {
                    selectedFragment = new HisFragment();
                }

                if (selectedFragment != null) {
                    // Pass the admin's email to the selected fragment
                    Bundle args = new Bundle();
                    args.putString("ADMIN_EMAIL", adminEmail);
                    selectedFragment.setArguments(args);

                    // Load the selected fragment
                    loadFragment(selectedFragment);
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }
}