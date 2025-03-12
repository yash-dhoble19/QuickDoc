package com.example.myapplication34;

public class Doctor {

    private String email;
    private String specialization;
    private String collegeName;
    private String experience;
    private String doctorName; // New field for doctor name
    private String hospitalCode;

    public Doctor(String email, String specialization, String collegeName, String experience, String doctorName,String hospitalCode) {
        this.email = email;
        this.specialization = specialization;
        this.collegeName = collegeName;
        this.experience = experience;
        this.doctorName = doctorName;
        this.hospitalCode = hospitalCode;

    }

    // Add setter method
    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getExperience() {
        return experience;
    }

    public String getDoctorName() {
        return doctorName;
    }
    public String getHospitalCode() {
        return hospitalCode;
    }
}