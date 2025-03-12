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

public class AppointmentAdapter2 extends RecyclerView.Adapter<AppointmentAdapter2.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private boolean isUpcomingList;

    public AppointmentAdapter2(List<Appointment> appointments, boolean isUpcomingList) {
        this.appointments = appointments;
        this.isUpcomingList = isUpcomingList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment2, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);

        // Format date and time
        String formattedDate = formatDate(appointment.getDate());
        String formattedTime = formatTime(appointment.getTime());

        // Set text with formatted values
        holder.userNameTextView.setText("Patient: " + appointment.getUserName());
        holder.doctorEmailTextView.setText("Doctor: " + appointment.getDoctorEmail());
        holder.dateTextView.setText("Date: " + formattedDate);
        holder.timeTextView.setText("Time: " + formattedTime);
        holder.problemDescriptionTextView.setText("Issue: " + appointment.getProblemDescription());

        // Visual differentiation
        int backgroundColor = isUpcomingList ?
                Color.parseColor("#E8F5E9") : // Light green for upcoming
                Color.parseColor("#F5F5F5");   // Light gray for visited

        int textColor = isUpcomingList ?
                Color.parseColor("#388E3C") : // Dark green text
                Color.parseColor("#757575");  // Gray text

        holder.itemView.setBackgroundColor(backgroundColor);
        holder.userNameTextView.setTextColor(textColor);
        holder.dateTextView.setTextColor(textColor);

        // Add status indicator
        String status = isUpcomingList ? "↑ Upcoming" : "✓ Visited";
        holder.doctorEmailTextView.setText(holder.doctorEmailTextView.getText() + " • " + status);
    }

    private String formatDate(String rawDate) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
            Date date = originalFormat.parse(rawDate);
            SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
            return targetFormat.format(date);
        } catch (ParseException e) {
            return rawDate;
        }
    }

    private String formatTime(String rawTime) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("H:mm", Locale.getDefault());
            Date time = originalFormat.parse(rawTime);
            SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return targetFormat.format(time);
        } catch (ParseException e) {
            return rawTime;
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView, doctorEmailTextView, dateTextView,
                timeTextView, problemDescriptionTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            doctorEmailTextView = itemView.findViewById(R.id.doctorEmailTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            problemDescriptionTextView = itemView.findViewById(R.id.problemDescriptionTextView);
        }



    }
}