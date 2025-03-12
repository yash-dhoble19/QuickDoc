package com.example.myapplication34;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.Button;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctors;
    private OnDoctorClickListener listener;
    private int selectedPosition = -1; // Track the selected doctor position
    private boolean showButton; // Flag to control button visibility

    public DoctorAdapter(List<Doctor> doctors, OnDoctorClickListener listener, boolean showButton) {
        this.doctors = doctors;
        this.listener = listener;
        this.showButton = showButton; // Initialize the flag
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);

        // Bind doctor details
        holder.doctorName.setText(doctor.getDoctorName()); // Display doctor name
        holder.doctorEmail.setText(doctor.getEmail());       // Display doctor email
        holder.specialization.setText(doctor.getSpecialization());
        holder.collegeName.setText(doctor.getCollegeName());
        holder.experience.setText(doctor.getExperience());

        // Control button visibility based on the flag
        if (showButton) {
            // Hide the button if this doctor is selected
            if (selectedPosition == position) {
                holder.bookAppointmentButton.setVisibility(View.GONE);
            } else {
                holder.bookAppointmentButton.setVisibility(View.VISIBLE);
            }

            // Set click listener for the "Book Appointment" button
            holder.bookAppointmentButton.setOnClickListener(v -> {
                selectedPosition = position; // Update the selected position
                notifyDataSetChanged(); // Refresh the RecyclerView to hide the button
                if (listener != null) {
                    listener.onBookAppointmentClicked(doctor); // Notify the listener
                }
            });
        } else {
            // Hide the button if showButton is false
            holder.bookAppointmentButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, doctorEmail, specialization, collegeName, experience;
        Button bookAppointmentButton; // Add button reference

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            doctorEmail = itemView.findViewById(R.id.doctorEmail); // Initialize the email TextView
            specialization = itemView.findViewById(R.id.specialization);
            collegeName = itemView.findViewById(R.id.collegeName);
            experience = itemView.findViewById(R.id.experience);
            bookAppointmentButton = itemView.findViewById(R.id.bookAppointmentButton);
        }
    }

    public interface OnDoctorClickListener {
        void onBookAppointmentClicked(Doctor doctor);
    }
}
