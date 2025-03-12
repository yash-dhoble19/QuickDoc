package com.example.myapplication34;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AcceptRequestsFragment extends Fragment {
    private static final String TAG = "AcceptRequestsFragment";
    private RecyclerView doctorsRecyclerView;
    private DoctorAdapter adapter;
    private DatabaseHelper dbHelper;
    private String adminHospitalCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accept_requests, container, false);
        dbHelper = new DatabaseHelper(getActivity());

        // Retrieve admin email from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String adminEmail = prefs.getString("email", "");
        Log.d(TAG, "Admin Email from SharedPreferences: " + adminEmail);

        // TEMPORARY: Uncomment the following line for debugging if SharedPreferences is empty
        // adminEmail = "admin@example.com";

        if (adminEmail.isEmpty()) {
            Toast.makeText(getContext(), "Admin email not found in SharedPreferences", Toast.LENGTH_LONG).show();
        }

        // Retrieve hospital code using the admin's email
        adminHospitalCode = dbHelper.getHospitalCodeByAdminEmail(adminEmail);
        Log.d(TAG, "Admin Hospital Code: " + adminHospitalCode);

        if (adminHospitalCode == null || adminHospitalCode.isEmpty()) {
            Toast.makeText(getContext(), "Hospital code not found for admin", Toast.LENGTH_LONG).show();
        }

        setupRecyclerView(view);
        return view;
    }

    private void setupRecyclerView(View view) {
        doctorsRecyclerView = view.findViewById(R.id.doctorsRecyclerView);
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Doctor> doctors = dbHelper.getDoctorsByHospitalCode(adminHospitalCode);
        if (doctors == null || doctors.isEmpty()) {
            Toast.makeText(getContext(), "No doctors found for this hospital", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Doctor list is empty for hospital code: " + adminHospitalCode);
        } else {
            Log.d(TAG, "Found " + doctors.size() + " doctor(s) for hospital code: " + adminHospitalCode);
        }
        adapter = new DoctorAdapter(doctors);
        doctorsRecyclerView.setAdapter(adapter);
    }

    private class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
        private List<Doctor> doctors;

        public DoctorAdapter(List<Doctor> doctors) {
            this.doctors = doctors;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_doctor2, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Doctor doctor = doctors.get(position);
            holder.doctorName.setText(doctor.getDoctorName());
            holder.specialization.setText(doctor.getSpecialization());
            holder.rejectButton.setOnClickListener(v ->
                    showConfirmationDialog(doctor.getEmail(), position));
        }

        @Override
        public int getItemCount() {
            return doctors.size();
        }

        public void removeDoctor(int position) {
            if (position < doctors.size()) {
                doctors.remove(position);
                notifyItemRemoved(position);
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView doctorName, specialization;
            Button rejectButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                doctorName = itemView.findViewById(R.id.doctorName);
                specialization = itemView.findViewById(R.id.specialization);
                rejectButton = itemView.findViewById(R.id.rejectButton);
            }
        }
    }

    private void showConfirmationDialog(String doctorEmail, int position) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Rejection")
                .setMessage("Are you sure you want to delete this doctor?")
                .setPositiveButton("Delete", (dialog, which) -> deleteDoctor(doctorEmail, position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteDoctor(String doctorEmail, int position) {
        boolean deleted = dbHelper.deleteDoctor(doctorEmail);
        if (deleted) {
            adapter.removeDoctor(position);
            Toast.makeText(getContext(), "Doctor deleted", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Deleted doctor with email: " + doctorEmail);
        } else {
            Toast.makeText(getContext(), "Deletion failed", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Failed to delete doctor with email: " + doctorEmail);
        }
    }
}
