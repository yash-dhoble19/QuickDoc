package com.example.myapplication34;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private OnAppointmentActionListener listener;

    public AppointmentAdapter(List<Appointment> appointments, OnAppointmentActionListener listener) {
        this.appointments = appointments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.userNameTextView.setText("User: " + appointment.getUserName());
        holder.doctorEmailTextView.setText("Doctor: " + appointment.getDoctorEmail());
        holder.dateTextView.setText("Date: " + appointment.getDate());
        holder.timeTextView.setText("Time: " + appointment.getTime());
        holder.problemDescriptionTextView.setText("Problem: " + appointment.getProblemDescription());

        holder.acceptButton.setOnClickListener(v -> listener.onAppointmentAction(appointment.getId(), "Accepted"));
        holder.rejectButton.setOnClickListener(v -> listener.onAppointmentAction(appointment.getId(), "Rejected"));
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, doctorEmailTextView, dateTextView, timeTextView, problemDescriptionTextView;
        Button acceptButton, rejectButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView1);
            doctorEmailTextView = itemView.findViewById(R.id.doctorEmailTextView1);
            dateTextView = itemView.findViewById(R.id.dateTextView1);
            timeTextView = itemView.findViewById(R.id.timeTextView1);
            problemDescriptionTextView = itemView.findViewById(R.id.problemDescriptionTextView1);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }

    public interface OnAppointmentActionListener {
        void onAppointmentAction(int appointmentId, String action);
    }
}