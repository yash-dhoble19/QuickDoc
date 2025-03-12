package com.example.myapplication34;  // Adjust package name as needed

public class Report {
    private String patientName;
    private String reportDate;
    private String diagnosis;

    // Constructor
    public Report(String patientName, String reportDate, String diagnosis) {
        this.patientName = patientName;
        this.reportDate = reportDate;
        this.diagnosis = diagnosis;
    }

    // Getters
    public String getPatientName() {
        return patientName;
    }

    public String getReportDate() {
        return reportDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }
}