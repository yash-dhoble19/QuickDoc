package com.example.myapplication34;

public class Appointment {
    private int id;
    private String userName;
    private String userEmail;
    private String doctorEmail;
    private String date;
    private String time;
    private String problemDescription;

    private String status; // Missing field

    private String hospitalCode;
    public Appointment(int id, String userName,String userEmail ,String doctorEmail, String date, String time, String problemDescription,String hospitalCode,String status) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.doctorEmail = doctorEmail;
        this.date = date;
        this.time = time;
        this.problemDescription = problemDescription;
        this.hospitalCode= hospitalCode;
        this.status = status;

    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    // Add getter
    public String getStatus() { return status; }
}
