package com.example.myapplication34;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserAppointmentAdapter extends RecyclerView.Adapter<UserAppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private boolean isUpcomingList;
    private DatabaseHelper databaseHelper;

    public UserAppointmentAdapter(List<Appointment> appointments, boolean isUpcomingList, DatabaseHelper dbHelper) {
        this.appointments = appointments;
        this.isUpcomingList = isUpcomingList;
        this.databaseHelper = dbHelper;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);



        // Get additional details
        String hospitalName = databaseHelper.getHospitalNameByCode(appointment.getHospitalCode());
        Doctor doctor = databaseHelper.getDoctorByEmail(appointment.getDoctorEmail());
        String doctorName = doctor != null ? doctor.getDoctorName() : "Unknown Doctor";

        // Format date/time
        holder.dateTextView.setText(formatDate(appointment.getDate()));
        holder.timeTextView.setText(formatTime(appointment.getTime()));
        holder.hospitalTextView.setText(hospitalName);
        holder.doctorTextView.setText(doctorName);
        holder.problemTextView.setText(appointment.getProblemDescription());

        // Styling
        int bgColor = isUpcomingList ? Color.parseColor("#E8F5E9") : Color.parseColor("#F5F5F5");
        int textColor = isUpcomingList ? Color.parseColor("#388E3C") : Color.parseColor("#757575");

        holder.itemView.setBackgroundColor(bgColor);
        holder.hospitalTextView.setTextColor(textColor);
        holder.dateTextView.setTextColor(textColor);
    }

    private String formatDate(String rawDate) {
        try {
            SimpleDateFormat original = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = original.parse(rawDate);
            SimpleDateFormat target = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
            return target.format(date);
        } catch (ParseException e) {
            return rawDate;
        }
    }

    private String formatTime(String rawTime) {
        try {
            SimpleDateFormat original = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date time = original.parse(rawTime);
            SimpleDateFormat target = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return target.format(time);
        } catch (ParseException e) {
            return rawTime;
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView hospitalTextView, doctorTextView, dateTextView, timeTextView, problemTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            hospitalTextView = itemView.findViewById(R.id.tvHospital);
            doctorTextView = itemView.findViewById(R.id.tvDoctor);
            dateTextView = itemView.findViewById(R.id.tvDate);
            timeTextView = itemView.findViewById(R.id.tvTime);
            problemTextView = itemView.findViewById(R.id.problemDescriptionTextView);
        }
    }
}