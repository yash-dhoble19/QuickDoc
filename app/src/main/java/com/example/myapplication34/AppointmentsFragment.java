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

public class AppointmentsFragment extends Fragment {

    private RecyclerView appointmentsRecyclerView;
    private AppointmentAdapter appointmentAdapter;
    private DatabaseHelper databaseHelper;
    private String hospitalCode; // Admin's hospital code

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);

        // Initialize views
        appointmentsRecyclerView = view.findViewById(R.id.appointmentsRecyclerView);
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize database helper
        databaseHelper = new DatabaseHelper(getContext());

        // Retrieve the admin's email from the arguments
        Bundle args = getArguments();
        if (args != null) {
            String adminEmail = args.getString("ADMIN_EMAIL");
            if (adminEmail != null) {
                // Fetch the admin's hospital code
                hospitalCode = databaseHelper.getHospitalCodeByAdminEmail(adminEmail);
                Log.d("AppointmentsFragment", "Admin Email: " + adminEmail);
                Log.d("AppointmentsFragment", "Hospital Code: " + hospitalCode);

                // Fetch and display pending appointments
                loadPendingAppointments();
            } else {
                Log.e("AppointmentsFragment", "ADMIN_EMAIL is null");
                Toast.makeText(getContext(), "Admin email not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("AppointmentsFragment", "Arguments are null");
            Toast.makeText(getContext(), "Arguments not found", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadPendingAppointments() {
        if (hospitalCode != null) {
            Log.d("AppointmentsFragment", "Fetching appointments for hospital: " + hospitalCode);

            // Fetch pending appointments from the database
            List<Appointment> appointments = databaseHelper.getPendingAppointments(hospitalCode);
            Log.d("AppointmentsFragment", "Number of appointments: " + (appointments != null ? appointments.size() : 0));

            if (appointments != null && !appointments.isEmpty()) {
                // Initialize the adapter and set it to the RecyclerView
                appointmentAdapter = new AppointmentAdapter(appointments, this::onAppointmentAction);
                appointmentsRecyclerView.setAdapter(appointmentAdapter);
            } else {
                Log.d("AppointmentsFragment", "No pending appointments found");
                Toast.makeText(getContext(), "No pending appointments", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("AppointmentsFragment", "Hospital code is null");
            Toast.makeText(getContext(), "Hospital code not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void onAppointmentAction(int appointmentId, String action) {
        boolean isSuccess = databaseHelper.updateAppointmentStatus(appointmentId, action);
        if (isSuccess) {
            Toast.makeText(getContext(), "Appointment " + action.toLowerCase() + "ed", Toast.LENGTH_SHORT).show();
            loadPendingAppointments(); // Refresh the list
        } else {
            Toast.makeText(getContext(), "Failed to update appointment", Toast.LENGTH_SHORT).show();
        }
    }
}