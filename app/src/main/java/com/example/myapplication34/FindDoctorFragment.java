package com.example.myapplication34;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;

public class FindDoctorFragment extends Fragment {

    private EditText problemDescription;
    private Spinner specialistSpinner;
    private Button findSpecialistButton;
    private RecyclerView doctorsRecyclerView;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_doctor, container, false);

        // Initialize views
        problemDescription = view.findViewById(R.id.problemDescription);
        specialistSpinner = view.findViewById(R.id.specialistSpinner);
        findSpecialistButton = view.findViewById(R.id.findSpecialistButton);
        doctorsRecyclerView = view.findViewById(R.id.doctorsRecyclerView);
        databaseHelper = new DatabaseHelper(getContext());

        // Set up specialist spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.specialist_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialistSpinner.setAdapter(adapter);

        // Set up "Find Specialist" button click listener
        findSpecialistButton.setOnClickListener(v -> {
            String problem = problemDescription.getText().toString().trim();
            String specialist = specialistSpinner.getSelectedItem().toString();

            if (!problem.isEmpty()) {
                // Fetch doctors by specialization
                List<Doctor> doctors = databaseHelper.getDoctorsBySpecialization(specialist);

                if (!doctors.isEmpty()) {
                    // Display doctors in RecyclerView
                    DoctorAdapter doctorAdapter = new DoctorAdapter(doctors, this::onBookAppointmentClicked,true); // Pass the listener
                    doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    doctorsRecyclerView.setAdapter(doctorAdapter);
                } else {
                    Toast.makeText(getContext(), "No doctors found for this specialization", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please describe your problem", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Handle doctor item click
    private void onDoctorClicked(Doctor doctor) {
        // Redirect to AppointmentFragment
        if (getActivity() instanceof UserDashboardActivity) {
            ((UserDashboardActivity) getActivity()).loadFragment(new AppointmentFragment());
        }
    }

    // Handle "Book Appointment" button click
    private void onBookAppointmentClicked(Doctor doctor) {
        // Redirect to AppointmentFragment
        if (getActivity() instanceof UserDashboardActivity) {
            ((UserDashboardActivity) getActivity()).loadFragment(new AppointmentFragment());
        }
    }
}