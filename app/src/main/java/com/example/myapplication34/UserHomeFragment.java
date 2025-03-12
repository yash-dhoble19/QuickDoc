package com.example.myapplication34;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Intent;
public class UserHomeFragment extends Fragment {

    private EditText searchBar;
    private RelativeLayout historyButton;
    private Button findDoctorButton;
    private Button chatBoxButton;
    private  Button appointmentButton;

    private  Button emergencyServicesButton;

    // Add this in your view initialization


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        // Initialize views

        // Add this in your view initialization

        searchBar = view.findViewById(R.id.searchBar);
        historyButton = view.findViewById(R.id.historyButton);
        findDoctorButton = view.findViewById(R.id.findDoctorButton);
        chatBoxButton = view.findViewById(R.id.chatBoxButton);
         appointmentButton = view.findViewById(R.id.appointmentButton);
        // Set up search bar
        searchBar.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                searchHospitalOrDoctor(query);
            } else {
                Toast.makeText(getContext(), "Please enter a search term", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up "Check Your History" button
        historyButton.setOnClickListener(v -> {
            if (getActivity() instanceof UserDashboardActivity) {
                ((UserDashboardActivity) getActivity()).loadFragment(new HistoryFragment());
            }
        });

        // Set up "Find Doctor" button
        findDoctorButton.setOnClickListener(v -> {
            if (getActivity() instanceof UserDashboardActivity) {
                ((UserDashboardActivity) getActivity()).loadFragment(new FindDoctorFragment());
            }
        });

        chatBoxButton.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Chatbox button clicked!", Toast.LENGTH_SHORT).show();

            // Start the ChatbotActivity
            Intent intent = new Intent(v.getContext(), chatbot.class);
            v.getContext().startActivity(intent);
        });

        // Set up "Find Doctor" button
        appointmentButton.setOnClickListener(v -> {
            if (getActivity() instanceof UserDashboardActivity) {
                ((UserDashboardActivity) getActivity()).loadFragment(new AppointmentFragment());

            }
        });




        return view;
    }

    // Method to search for hospitals or doctors
    private void searchHospitalOrDoctor(String query) {
        // Implement search logic here
        Toast.makeText(getContext(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
    }
}