package com.example.myapplication34;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {
    private RecyclerView reportsRecyclerView;
    private ReportAdapter adapter;
    private FloatingActionButton fabCreateReport;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        fabCreateReport = view.findViewById(R.id.fab_create_report);
        reportsRecyclerView = view.findViewById(R.id.reports_recycler);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshReportList();

        fabCreateReport.setOnClickListener(v -> showCreateReportDialog());

        return view;
    }

    private void showCreateReportDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Create New Report");

        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_create_report, null);
        builder.setView(dialogView);

        EditText etPatientName = dialogView.findViewById(R.id.et_patient_name);
        EditText etDiagnosis = dialogView.findViewById(R.id.et_diagnosis);

        builder.setPositiveButton("Create", (dialog, which) -> {
            String patientName = etPatientName.getText().toString().trim();
            String diagnosis = etDiagnosis.getText().toString().trim();

            if (!patientName.isEmpty() && !diagnosis.isEmpty()) {
                if (createNewReport(patientName, diagnosis)) {
                    refreshReportList();
                } else {
                    Toast.makeText(requireContext(), "Failed to create report", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private boolean createNewReport(String patientName, String diagnosis) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PATIENT_NAME, patientName);
        values.put(DatabaseHelper.COLUMN_DIAGNOSIS, diagnosis);
        values.put(DatabaseHelper.COLUMN_REPORT_DATE,
                new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        long result = db.insert(DatabaseHelper.TABLE_REPORTS, null, values);
        return result != -1;
    }

    private void refreshReportList() {
        List<Report> reports = getReportsFromDB();
        if (adapter == null) {
            adapter = new ReportAdapter(reports);
            reportsRecyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(reports);
        }
    }

    private List<Report> getReportsFromDB() {
        List<Report> reports = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_PATIENT_NAME,
                DatabaseHelper.COLUMN_REPORT_DATE,
                DatabaseHelper.COLUMN_DIAGNOSIS
        };

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_REPORTS,
                projection,
                null,
                null,
                null,
                null,
                DatabaseHelper.COLUMN_REPORT_DATE + " DESC"
        );

        while (cursor.moveToNext()) {
            reports.add(new Report(
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PATIENT_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REPORT_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DIAGNOSIS))
            ));
        }
        cursor.close();
        return reports;
    }
}