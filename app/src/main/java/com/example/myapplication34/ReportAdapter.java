package com.example.myapplication34;  // Adjust package name as needed

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private List<Report> reportList;

    public ReportAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }


    public void updateData(List<Report> newReports) {
        reportList.clear();
        reportList.addAll(newReports);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.bind(report);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        private TextView patientNameTextView;
        private TextView reportDateTextView;
        private TextView diagnosisTextView;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameTextView = itemView.findViewById(R.id.tv_patient_name);
            reportDateTextView = itemView.findViewById(R.id.tv_report_date);
            diagnosisTextView = itemView.findViewById(R.id.tv_diagnosis);
        }

        public void bind(Report report) {
            patientNameTextView.setText(report.getPatientName());
            reportDateTextView.setText(report.getReportDate());
            diagnosisTextView.setText(report.getDiagnosis());
        }
    }
}