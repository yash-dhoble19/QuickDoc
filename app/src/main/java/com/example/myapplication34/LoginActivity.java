package com.example.myapplication34;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;


public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private RadioGroup roleRadioGroup;
    private Button loginButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
        loginButton = findViewById(R.id.loginButton);
        dbHelper = new DatabaseHelper(this);

        // Apply animations
        ImageView logo = findViewById(R.id.logo);
        TextView title = findViewById(R.id.title);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);

        logo.startAnimation(fadeIn);
        title.startAnimation(slideUp);

        loginButton.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
            loginUser();
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRoleButton = findViewById(selectedRoleId);
        String role = selectedRoleButton.getText().toString();

        if (dbHelper.validateLogin(email, password, role)) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_email", email);
            editor.putString("user_role", role);
            editor.apply();

            // Redirect to respective dashboard based on role
            Intent intent;
            switch (role) {
                case "User":
                    intent = new Intent(LoginActivity.this, UserDashboardActivity.class);
                    break;
                case "Admin":
                    intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                    intent.putExtra("ADMIN_EMAIL", email); // Pass the admin's email
                    break;
                case "Doctor":
                    intent = new Intent(LoginActivity.this, DoctorDashboardActivity.class);
                    break;
                default:
                    throw new IllegalStateException("Unexpected role: " + role);
            }

            // Start the appropriate activity
            startActivity(intent);
            finish(); // Close the LoginActivity to prevent going back
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }


        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // Simulate network delay
        new Handler().postDelayed(() -> {
            // Your existing login logic
            progressBar.setVisibility(View.GONE);
            loginButton.setEnabled(true);
        }, 1500);
    }


    public void goToRegistration(View view) {
        startActivity(new Intent(this, RegistrationActivity.class));
    }
}