package com.example.myapplication34;

public class Hospital {
    private String hospitalName;
    private String hospitalCode;
    private int totalEmployees;
    private String hospitalAddress;
    private String contactNumber;
    private String departments;


    public Hospital(String hospitalName, String hospitalCode, int totalEmployees,
                    String hospitalAddress, String contactNumber, String departments) {
        this.hospitalName = hospitalName;
        this.hospitalCode = hospitalCode;
        this.totalEmployees = totalEmployees;
        this.hospitalAddress = hospitalAddress;
        this.contactNumber = contactNumber;
        this.departments = departments;
    }

    // Add getters and setters
}