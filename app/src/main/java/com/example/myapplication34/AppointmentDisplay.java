package com.example.myapplication34;

public class AppointmentDisplay {
    private String date;
    private String time;
    private String doctorName;
    private String hospitalName;

    public AppointmentDisplay(String date, String time, String doctorName, String hospitalName) {
        this.date = date;
        this.time = time;
        this.doctorName = doctorName;
        this.hospitalName = hospitalName;
    }

    // Getters
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDoctorName() { return doctorName; }
    public String getHospitalName() { return hospitalName; }
}