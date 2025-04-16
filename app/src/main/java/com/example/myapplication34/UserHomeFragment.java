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
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.Arrays;
import java.util.List;
import com.example.myapplication34.SliderAdapter;
public class UserHomeFragment extends Fragment {


    private ViewPager2 viewPager2;
    private List<Integer> imageList;
    private Handler sliderHandler = new Handler();

    private RelativeLayout historyButton;
    private Button findDoctorButton;

    private  Button appointmentButton;

    private  Button emergencyServicesButton;

    // Add this in your view initialization


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        // Initialize views

        // Add this in your view initialization
        viewPager2 = view.findViewById(R.id.viewPager);

        // Add advertisement images to list
        imageList = Arrays.asList(
                R.drawable.ad1,
                R.drawable.ad2,
                R.drawable.ad3,
                R.drawable.ad4
        );


        SliderAdapter sliderAdapter = new SliderAdapter(requireContext(), imageList);

        viewPager2.setAdapter(sliderAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000); // Slide every 3 sec
            }
        });


        historyButton = view.findViewById(R.id.historyButton);
        findDoctorButton = view.findViewById(R.id.findDoctorButton);
         appointmentButton = view.findViewById(R.id.appointmentButton);
        // Set up search bar


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

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            int nextSlide = (viewPager2.getCurrentItem() + 1) % imageList.size();
            viewPager2.setCurrentItem(nextSlide);
        }
    };
}