package com.example.myapplication34;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, hospitalCodeEditText, hospitalNameEditText;
    private EditText collegeNameEditText, experienceEditText, doctorIdEditText, doctorNameEditText;
    private RadioGroup roleRadioGroup;
    private Button registerButton;
    private DatabaseHelper dbHelper;
    private Spinner specializationSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        hospitalCodeEditText = findViewById(R.id.hospitalCodeEditText);
        hospitalNameEditText = findViewById(R.id.hospitalNameEditText);
        collegeNameEditText = findViewById(R.id.collegeNameEditText);
        experienceEditText = findViewById(R.id.experienceEditText);
        doctorIdEditText = findViewById(R.id.doctorIdEditText);
        doctorNameEditText = findViewById(R.id.doctorNameEditText);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
        registerButton = findViewById(R.id.registerButton);
        specializationSpinner = findViewById(R.id.specializationSpinner);
        dbHelper = new DatabaseHelper(this);

        // Set up specialization spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.specializations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specializationSpinner.setAdapter(adapter);

        // Set up role selection listener
        roleRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.adminRadioButton) {
                hospitalCodeEditText.setVisibility(View.VISIBLE);
                hospitalNameEditText.setVisibility(View.VISIBLE);
                specializationSpinner.setVisibility(View.GONE);
                collegeNameEditText.setVisibility(View.GONE);
                experienceEditText.setVisibility(View.GONE);
                doctorIdEditText.setVisibility(View.GONE);
                doctorNameEditText.setVisibility(View.GONE);
            } else if (checkedId == R.id.doctorRadioButton) {
                hospitalCodeEditText.setVisibility(View.VISIBLE);
                specializationSpinner.setVisibility(View.VISIBLE);
                collegeNameEditText.setVisibility(View.VISIBLE);
                experienceEditText.setVisibility(View.VISIBLE);
                doctorIdEditText.setVisibility(View.VISIBLE);
                doctorNameEditText.setVisibility(View.VISIBLE);
                hospitalNameEditText.setVisibility(View.GONE);
            } else {
                hospitalCodeEditText.setVisibility(View.GONE);
                hospitalNameEditText.setVisibility(View.GONE);
                specializationSpinner.setVisibility(View.GONE);
                collegeNameEditText.setVisibility(View.GONE);
                experienceEditText.setVisibility(View.GONE);
                doctorIdEditText.setVisibility(View.GONE);
                doctorNameEditText.setVisibility(View.GONE);
            }
        });

        // Register button click listener
        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String hospitalCode = hospitalCodeEditText.getText().toString().trim();
        String hospitalName = hospitalNameEditText.getText().toString().trim();
        String collegeName = collegeNameEditText.getText().toString().trim();
        String experience = experienceEditText.getText().toString().trim();
        String doctorId = doctorIdEditText.getText().toString().trim();
        String doctorName = doctorNameEditText.getText().toString().trim();

        // Get selected specialization from spinner
        String specialization = specializationSpinner.getSelectedItem().toString();

        int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRoleButton = findViewById(selectedRoleId);
        String role = selectedRoleButton.getText().toString();

        // Validate fields
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (role.equals("Admin") && (hospitalName.isEmpty() || hospitalCode.isEmpty())) {
            Toast.makeText(this, "Please fill hospital details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (role.equals("Doctor")) {
            if (collegeName.isEmpty() || experience.isEmpty() ||
                    doctorId.isEmpty() || doctorName.isEmpty()) {
                Toast.makeText(this, "Please fill all doctor details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!dbHelper.checkHospitalCode(hospitalCode)) {
                Toast.makeText(this, "Hospital does not exist", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Insert user into database
        String hospitalCodeForUser = null;
        if (role.equals("Admin") || role.equals("Doctor")) {
            hospitalCodeForUser = hospitalCode;
        }

        if (dbHelper.insertUser(email, password, role, hospitalCodeForUser)) {
            if (role.equals("Admin")) {
                dbHelper.insertHospital(hospitalName, hospitalCode);
            } else if (role.equals("Doctor")) {
                dbHelper.insertDoctor(email, specialization, collegeName,
                        experience, doctorId, doctorName, hospitalCode);
            }
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
        }
    }
}