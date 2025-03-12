package com.example.myapplication34;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleFragment extends Fragment {
    private RecyclerView upcomingRecycler, visitedRecycler;
    private AppointmentAdapter2 upcomingAdapter, visitedAdapter;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(requireContext());

        // Initialize RecyclerViews
        upcomingRecycler = view.findViewById(R.id.upcoming_recycler);
        visitedRecycler = view.findViewById(R.id.visited_recycler);

        // Setup layout managers
        upcomingRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        visitedRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load appointments
        loadAppointments();

        return view;
    }

    private void loadAppointments() {
        List<Appointment> allAppointments = getAppointmentsFromDB();
        List<Appointment> upcoming = new ArrayList<>();
        List<Appointment> visited = new ArrayList<>();

        for (Appointment app : allAppointments) {
            if (isUpcoming(app)) {
                upcoming.add(app);
            } else {
                visited.add(app);
            }
        }

        // Create adapters with the actual lists
        upcomingAdapter = new AppointmentAdapter2(upcoming, true);
        visitedAdapter = new AppointmentAdapter2(visited, false);

        // Set adapters
        upcomingRecycler.setAdapter(upcomingAdapter);
        visitedRecycler.setAdapter(visitedAdapter);
    }

    private boolean isUpcoming(Appointment appointment) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy HH:mm", Locale.getDefault());
            Date appDate = sdf.parse(appointment.getDate() + " " + appointment.getTime());
            return appDate != null && appDate.after(new Date());
        } catch (ParseException e) {
            Log.e("ScheduleFragment", "Date parsing error", e);
            return false;
        }
    }

    private List<Appointment> getAppointmentsFromDB() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String doctorEmail = prefs.getString("user_email", "");
        String hospitalCode = dbHelper.getHospitalCodeByDoctorEmail(doctorEmail);

        Log.d("ScheduleFragment", "Doctor Email: " + doctorEmail);
        Log.d("ScheduleFragment", "Hospital Code: " + hospitalCode);

        List<Appointment> appointments = dbHelper.getAcceptedAppointments(hospitalCode);
        Log.d("ScheduleFragment", "Fetched appointments: " + appointments.size());

        return appointments;
    }
}