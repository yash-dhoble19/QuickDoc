package com.example.myapplication34;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import  com.example.myapplication34.Appointment;

public class HistoryFragment extends Fragment {

    private static final String TAG = "HistoryFragment";
    private RecyclerView rvUpcoming, rvVisited;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireActivity());

        // Get user email from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        userEmail = prefs.getString("user_email", "");
        Log.d(TAG, "User email: " + userEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history2, container, false);

        rvUpcoming = view.findViewById(R.id.rvUpcoming);
        rvVisited = view.findViewById(R.id.rvVisited);

        rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        rvVisited.setLayoutManager(new LinearLayoutManager(getContext()));

        loadAppointments();
        return view;
    }

    private void loadAppointments() {
        if (userEmail.isEmpty()) {
            Log.e(TAG, "No user email found");
            return;
        }



        List<Appointment> allAppointments = databaseHelper.getAcceptedAppointmentsByUserEmail(userEmail);
        Log.d(TAG, "Total accepted appointments: " + allAppointments.size());

        List<Appointment> upcomingAppointments = new ArrayList<>();
        List<Appointment> visitedAppointments = new ArrayList<>();

        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        for (Appointment appointment : allAppointments) {
            try {
                // Handle null time
                String date = appointment.getDate() != null ? appointment.getDate() : "01/01/1970";
                String time = appointment.getTime() != null ? appointment.getTime() : "00:00";
                String dateTimeString = date + " " + time;

                // Rest of the code remains the same
                Date appointmentDate = sdf.parse(dateTimeString);


                if (appointmentDate == null) {
                    Log.e(TAG, "Failed to parse date/time for appointment: " + appointment.getId());
                    continue;
                }

                Calendar appointmentCal = Calendar.getInstance();
                appointmentCal.setTime(appointmentDate);

                if (appointmentCal.after(now)) {
                    upcomingAppointments.add(appointment);
                } else {
                    visitedAppointments.add(appointment);
                }
            } catch (ParseException e) {
                Log.e(TAG, "Error parsing date/time for appointment ID: " + appointment.getId(), e);
                Log.e(TAG, "Raw date: " + appointment.getDate() + ", time: " + appointment.getTime());
            }
        }

        Log.d(TAG, "Upcoming: " + upcomingAppointments.size() + ", Visited: " + visitedAppointments.size());

        // Update adapters
        UserAppointmentAdapter upcomingAdapter = new UserAppointmentAdapter(
                upcomingAppointments,
                true,
                databaseHelper
        );

        UserAppointmentAdapter visitedAdapter = new UserAppointmentAdapter(
                visitedAppointments,
                false,
                databaseHelper
        );

        rvUpcoming.setAdapter(upcomingAdapter);
        rvVisited.setAdapter(visitedAdapter);


    }


}