package com.example.myapplication34;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListFragment extends Fragment {

    private RecyclerView doctorsRecyclerView;
    private DoctorAdapter doctorAdapter;
    private DatabaseHelper databaseHelper;
    private String hospitalCode; // Admin's hospital code

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Initialize views
        doctorsRecyclerView = view.findViewById(R.id.doctorsRecyclerView);
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize database helper
        databaseHelper = new DatabaseHelper(getContext());

        // Retrieve the admin's email from the arguments
        Bundle args = getArguments();
        if (args != null) {
            String adminEmail = args.getString("ADMIN_EMAIL");
            if (adminEmail != null) {
                // Fetch the admin's hospital code
                hospitalCode = databaseHelper.getHospitalCodeByAdminEmail(adminEmail);
                Log.d("ListFragment", "Admin Email: " + adminEmail);
                Log.d("ListFragment", "Hospital Code: " + hospitalCode);

                // Fetch and display doctors
                loadDoctors();
            } else {
                Log.e("ListFragment", "ADMIN_EMAIL is null");
                Toast.makeText(getContext(), "Admin email not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("ListFragment", "Arguments are null");
            Toast.makeText(getContext(), "Arguments not found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadDoctors() {
        if (hospitalCode != null) {
            Log.d("ListFragment", "Fetching doctors for hospital: " + hospitalCode);

            // Fetch doctors from the database
            List<Doctor> doctors = databaseHelper.getDoctorsByHospitalCode(hospitalCode);
            Log.d("ListFragment", "Number of doctors: " + (doctors != null ? doctors.size() : 0));

            if (doctors != null && !doctors.isEmpty()) {
                // Initialize the adapter and set it to the RecyclerView
                doctorAdapter = new DoctorAdapter(doctors, null,false); // No click listener needed for admin
                doctorsRecyclerView.setAdapter(doctorAdapter);
            } else {
                Log.d("ListFragment", "No doctors found");
                Toast.makeText(getContext(), "No doctors found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("ListFragment", "Hospital code is null");
            Toast.makeText(getContext(), "Hospital code not found", Toast.LENGTH_SHORT).show();
        }
    }
}