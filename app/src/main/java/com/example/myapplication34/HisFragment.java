package com.example.myapplication34;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.content.SharedPreferences;
import android.content.Context;

public class HisFragment extends Fragment {

    private RecyclerView rvUpcoming, rvVisited;
    private DatabaseHelper databaseHelper;
    private String hospitalCode;

    public HisFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());

        // Get logged-in admin's email from SharedPreferences
        SharedPreferences prefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String adminEmail = prefs.getString("user_email", ""); // Use the same key you use when storing
        String userRole = prefs.getString("user_role", "");


        // Fetch hospital code for the logged-in admin
        hospitalCode = databaseHelper.getHospitalCodeByAdminEmail(adminEmail);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        rvUpcoming = view.findViewById(R.id.rvUpcoming);
        rvVisited = view.findViewById(R.id.rvVisited);

        rvUpcoming.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvVisited.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Fetch accepted appointments
        List<Appointment> acceptedAppointments = databaseHelper.getAcceptedAppointments(hospitalCode);

        // Categorize appointments into Upcoming and Visited
        List<Appointment> upcomingAppointments = new ArrayList<>();
        List<Appointment> visitedAppointments = new ArrayList<>();

        Calendar now = Calendar.getInstance();
        for (Appointment appointment : acceptedAppointments) {
            Calendar appointmentDateTime = Calendar.getInstance();

            // Split the date using "/" since it's stored as day/month/year
            String[] dateParts = appointment.getDate().split("/");
            int day = Integer.parseInt(dateParts[0]);
            int month = Integer.parseInt(dateParts[1]) - 1; // Calendar month is 0-based
            int year = Integer.parseInt(dateParts[2]);

            // Parse the time as before
            int hour = Integer.parseInt(appointment.getTime().split(":")[0]);
            int minute = Integer.parseInt(appointment.getTime().split(":")[1]);

            // Set the Calendar with the correct values
            appointmentDateTime.set(year, month, day, hour, minute);

            // Categorize based on whether the appointment is in the future or past
            if (appointmentDateTime.after(Calendar.getInstance())) {
                upcomingAppointments.add(appointment);
            } else {
                visitedAppointments.add(appointment);
            }
        }


        // Set up RecyclerView adapters
        AppointmentAdapter2 upcomingAdapter = new AppointmentAdapter2(upcomingAppointments, true);
        AppointmentAdapter2 visitedAdapter = new AppointmentAdapter2(visitedAppointments, false);
        rvUpcoming.setAdapter(upcomingAdapter);
        rvVisited.setAdapter(visitedAdapter);

        return view;
    }
}