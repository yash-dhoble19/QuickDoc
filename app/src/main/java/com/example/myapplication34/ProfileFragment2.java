package com.example.myapplication34;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class ProfileFragment2 extends Fragment {

    private EditText userNameEditText, mobileNumberEditText, addressEditText, dobEditText, aadharCardEditText, parentNumberEditText;
    private Spinner sexSpinner, bloodGroupSpinner;
    private Button saveProfileButton;
    private CardView profileCardView;
    private TextView profileName, profileMobile, profileAddress, profileDOB, profileAadhar, profileSex, profileBloodGroup, profileParentNumber;
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile2, container, false);

        // Initialize views
        userNameEditText = view.findViewById(R.id.userNameEditText);
        mobileNumberEditText = view.findViewById(R.id.mobileNumberEditText);
        addressEditText = view.findViewById(R.id.addressEditText);
        dobEditText = view.findViewById(R.id.dobEditText);
        aadharCardEditText = view.findViewById(R.id.aadharCardEditText);
        parentNumberEditText = view.findViewById(R.id.parentNumberEditText);
        sexSpinner = view.findViewById(R.id.sexSpinner);
        bloodGroupSpinner = view.findViewById(R.id.bloodGroupSpinner);
        saveProfileButton = view.findViewById(R.id.saveProfileButton);
        profileCardView = view.findViewById(R.id.profileCardView);
        profileName = view.findViewById(R.id.profileName);
        profileMobile = view.findViewById(R.id.profileMobile);
        profileAddress = view.findViewById(R.id.profileAddress);
        profileDOB = view.findViewById(R.id.profileDOB);
        profileAadhar = view.findViewById(R.id.profileAadhar);
        profileSex = view.findViewById(R.id.profileSex);
        profileBloodGroup = view.findViewById(R.id.profileBloodGroup);
        profileParentNumber = view.findViewById(R.id.profileParentNumber);
        databaseHelper = new DatabaseHelper(getContext());

        // Set up spinners
        ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sex_options, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(sexAdapter);

        ArrayAdapter<CharSequence> bloodGroupAdapter = ArrayAdapter.createFromResource(getContext(), R.array.blood_group_options, android.R.layout.simple_spinner_item);
        bloodGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodGroupSpinner.setAdapter(bloodGroupAdapter);

        // Save profile button click listener
        saveProfileButton.setOnClickListener(v -> saveProfile());

        // Display profile if it exists
        String profile = databaseHelper.getUserProfile();
        if (!profile.isEmpty()) {
            displayProfile(profile);
        }

        return view;
    }

    private void saveProfile() {
        String userName = userNameEditText.getText().toString().trim();
        String mobileNumber = mobileNumberEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();
        String aadharCard = aadharCardEditText.getText().toString().trim();
        String sex = sexSpinner.getSelectedItem().toString();
        String bloodGroup = bloodGroupSpinner.getSelectedItem().toString();
        String parentNumber = parentNumberEditText.getText().toString().trim();

        if (userName.isEmpty() || mobileNumber.isEmpty() || address.isEmpty() || dob.isEmpty() || aadharCard.isEmpty() || parentNumber.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isSuccess = databaseHelper.saveUserProfile(userName, mobileNumber, address, dob, aadharCard, sex, bloodGroup, parentNumber);
        if (isSuccess) {
            Toast.makeText(getContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show();
            String profile = databaseHelper.getUserProfile();
            displayProfile(profile);
        } else {
            Toast.makeText(getContext(), "Failed to save profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayProfile(String profile) {
        // Split the profile string into individual fields
        String[] fields = profile.split("\n");

        // Update the TextViews with the profile data
        profileName.setText(fields[0]); // Name
        profileMobile.setText(fields[1]); // Mobile
        profileAddress.setText(fields[2]); // Address
        profileDOB.setText(fields[3]); // DOB
        profileAadhar.setText(fields[4]); // Aadhar
        profileSex.setText(fields[5]); // Sex
        profileBloodGroup.setText(fields[6]); // Blood Group
        profileParentNumber.setText(fields[7]); // Parent's Number

        // Make the CardView visible
        profileCardView.setVisibility(View.VISIBLE);
    }
}