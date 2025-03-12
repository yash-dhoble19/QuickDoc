package com.example.myapplication34;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    private EditText etHospitalName, etHospitalCode;
    private Button btnSaveHospital;
    private DatabaseHelper databaseHelper;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        etHospitalName = view.findViewById(R.id.etHospitalName);
        etHospitalCode = view.findViewById(R.id.etHospitalCode);
        btnSaveHospital = view.findViewById(R.id.btnSaveHospital);

        btnSaveHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHospitalInformation();
            }
        });

        return view;
    }

    private void saveHospitalInformation() {
        String hospitalName = etHospitalName.getText().toString().trim();
        String hospitalCode = etHospitalCode.getText().toString().trim();

        if (hospitalName.isEmpty() || hospitalCode.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = databaseHelper.insertHospital(hospitalName, hospitalCode);

        if (isInserted) {
            Toast.makeText(getActivity(), "Hospital information saved successfully", Toast.LENGTH_SHORT).show();
            etHospitalName.setText("");
            etHospitalCode.setText("");
        } else {
            Toast.makeText(getActivity(), "Failed to save hospital information", Toast.LENGTH_SHORT).show();
        }
    }
}