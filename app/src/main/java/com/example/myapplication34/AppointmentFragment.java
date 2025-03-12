package com.example.myapplication34;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import android.app.AlertDialog;
import  android.content.SharedPreferences;
import android.content.Context;

public class AppointmentFragment extends Fragment {

    private EditText userNameEditText, problemDescriptionEditText;
    private AutoCompleteTextView hospitalNameAutoComplete;
    private TextView hospitalCodeTextView;
    private RecyclerView doctorsRecyclerView;
    private Button selectDateButton, selectTimeButton, requestAppointmentButton;
    private DatabaseHelper databaseHelper;
    private String selectedHospitalCode, selectedDoctorEmail, selectedDate, selectedTime;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);
// Initialize RecyclerView
        doctorsRecyclerView = view.findViewById(R.id.doctorsRecyclerView2);
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Initialize views
        userNameEditText = view.findViewById(R.id.userNameEditText);
        hospitalNameAutoComplete = view.findViewById(R.id.hospitalNameAutoComplete);
        hospitalCodeTextView = view.findViewById(R.id.hospitalCodeTextView);
        problemDescriptionEditText = view.findViewById(R.id.problemDescriptionEditText);
        doctorsRecyclerView = view.findViewById(R.id.doctorsRecyclerView2);
        selectDateButton = view.findViewById(R.id.selectDateButton);
        selectTimeButton = view.findViewById(R.id.selectTimeButton);
        requestAppointmentButton = view.findViewById(R.id.requestAppointmentButton);
        databaseHelper = new DatabaseHelper(getContext());

        // Set up hospital name suggestions
        setupHospitalNameSuggestions();

        // Set up date and time pickers
        setupDateAndTimePickers();

        // Set up "Request Appointment" button
        requestAppointmentButton.setOnClickListener(v -> requestAppointment());

        return view;
    }

    // Inside AppointmentFragment
    private void setupHospitalNameSuggestions() {
        // Fetch hospital names from the database
        List<String> hospitalNames = databaseHelper.getAllHospitalNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, hospitalNames);
        hospitalNameAutoComplete.setAdapter(adapter);

        // Handle hospital selection
        hospitalNameAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            String selectedHospitalName = (String) parent.getItemAtPosition(position);
            selectedHospitalCode = databaseHelper.getHospitalCodeByName(selectedHospitalName);
            hospitalCodeTextView.setText("Hospital Code: " + selectedHospitalCode);

            // Fetch and display doctors from the selected hospital
            List<Doctor> doctors = databaseHelper.getDoctorsByHospitalCode(selectedHospitalCode);
            DoctorAdapter doctorAdapter = new DoctorAdapter(doctors, this::onBookAppointmentClicked,true);
            doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            doctorsRecyclerView.setAdapter(doctorAdapter);
        });
    }
    // Handle "Book Appointment" button click
    private void onBookAppointmentClicked(Doctor doctor) {
        selectedDoctorEmail = doctor.getEmail();
        Toast.makeText(getContext(), "Appointment booked for: " + doctor.getDoctorName(), Toast.LENGTH_SHORT).show();

        // Display the selected doctor's details
        String doctorDetails = "Doctor: " + doctor.getDoctorName() + "\n"
                + "Specialization: " + doctor.getSpecialization() + "\n"
                + "College: " + doctor.getCollegeName() + "\n"
                + "Experience: " + doctor.getExperience();

        // Show the details in a dialog
        new AlertDialog.Builder(requireContext())
                .setTitle("Appointment Booked")
                .setMessage(doctorDetails)
                .setPositiveButton("OK", null)
                .show();
    }

    private void setupDateAndTimePickers() {
        // Date Picker
        selectDateButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year1, month1, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                selectDateButton.setText("Selected Date: " + selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        // Time Picker
        selectTimeButton.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, hourOfDay, minute1) -> {
                selectedTime = hourOfDay + ":" + minute1;
                selectTimeButton.setText("Selected Time: " + selectedTime);
            }, hour, minute, true);
            timePickerDialog.show();
        });
    }

    private void onDoctorSelected(Doctor doctor) {
        selectedDoctorEmail = doctor.getEmail();
        Toast.makeText(getContext(), "Selected Doctor: " + doctor.getDoctorName(), Toast.LENGTH_SHORT).show();

        // Display the selected doctor's details
        String doctorDetails = "Doctor: " + doctor.getDoctorName() + "\n"
                + "Specialization: " + doctor.getSpecialization() + "\n"
                + "College: " + doctor.getCollegeName() + "\n"
                + "Experience: " + doctor.getExperience();

        // Show the details in a dialog or a TextView
        new AlertDialog.Builder(getContext())
                .setTitle("Selected Doctor")
                .setMessage(doctorDetails)
                .setPositiveButton("OK", null)
                .show();
    }
    private void requestAppointment() {
        String userName = userNameEditText.getText().toString().trim();
        String problemDescription = problemDescriptionEditText.getText().toString().trim();
        // Get logged-in user's email from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userEmail = prefs.getString("user_email", "");

        if (userName.isEmpty() || selectedHospitalCode == null || selectedDoctorEmail == null || selectedDate == null || selectedTime == null) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the appointment request in the database
        boolean isSuccess = databaseHelper.insertAppointmentRequest(userName,  userEmail,selectedHospitalCode, selectedDoctorEmail, selectedDate, selectedTime, problemDescription);

        if (isSuccess) {
            Toast.makeText(getContext(), "Appointment request sent successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to send appointment request", Toast.LENGTH_SHORT).show();
        }
    }
}