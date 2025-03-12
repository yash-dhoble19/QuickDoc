package com.example.myapplication34;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuickDoc.db";
    private static final int DATABASE_VERSION = 18 ;// Incremented version to 14

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_HOSPITALS = "hospitals";
    private static final String TABLE_DOCTORS = "doctors";
    private static final String TABLE_APPOINTMENTS = "appointments";
    private static final String TABLE_USER_PROFILE = "user_profile";

    // Common Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    // Hospital Columns
    private static final String COLUMN_HOSPITAL_NAME = "hospital_name";
    private static final String COLUMN_HOSPITAL_CODE = "hospital_code";

    // New Hospital Columns
    private static final String COLUMN_TOTAL_EMPLOYEES = "total_employees";
    private static final String COLUMN_HOSPITAL_ADDRESS = "hospital_address";
    private static final String COLUMN_CONTACT_NUMBER = "contact_number";
    private static final String COLUMN_DEPARTMENTS = "departments";

    // Doctor Columns
    private static final String COLUMN_SPECIALIZATION = "specialization";
    private static final String COLUMN_COLLEGE_NAME = "college_name";
    private static final String COLUMN_EXPERIENCE = "experience";
    private static final String COLUMN_DOCTOR_ID = "doctor_id";
    private static final String COLUMN_DOCTOR_NAME = "doctor_name";
    // Add to Table Names


    public static final String TABLE_REPORTS = "reports";

    // Add to Common Columns
    public static final String COLUMN_PATIENT_NAME = "patient_name";
    public static final String COLUMN_REPORT_DATE = "report_date";
    public static final String COLUMN_DIAGNOSIS = "diagnosis";

    // Add to onCreate() method


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_ROLE + " TEXT,"
                + COLUMN_HOSPITAL_CODE + " TEXT)"; // Added hospital_code column
        db.execSQL(CREATE_USERS_TABLE);

        // Create Hospitals Table
        String CREATE_HOSPITALS_TABLE = "CREATE TABLE " + TABLE_HOSPITALS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_HOSPITAL_NAME + " TEXT,"
                + COLUMN_HOSPITAL_CODE + " TEXT UNIQUE,"
                + COLUMN_TOTAL_EMPLOYEES + " INTEGER,"
                + COLUMN_HOSPITAL_ADDRESS + " TEXT,"
                + COLUMN_CONTACT_NUMBER + " TEXT,"
                + COLUMN_DEPARTMENTS + " TEXT)";
        db.execSQL(CREATE_HOSPITALS_TABLE);

        // Create Doctors Table
        String CREATE_DOCTORS_TABLE = "CREATE TABLE " + TABLE_DOCTORS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT UNIQUE,"
                + COLUMN_SPECIALIZATION + " TEXT,"
                + COLUMN_COLLEGE_NAME + " TEXT,"
                + COLUMN_EXPERIENCE + " TEXT,"
                + COLUMN_DOCTOR_ID + " TEXT,"
                + COLUMN_DOCTOR_NAME + " TEXT,"
                + "hospital_code TEXT)";
        db.execSQL(CREATE_DOCTORS_TABLE);

        // Create Appointments Table
        String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_APPOINTMENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_name TEXT,"
                + "user_email TEXT,"  // New column

                + "hospital_code TEXT,"
                + "doctor_email TEXT,"
                + "date TEXT,"
                + "time TEXT,"
                + "problem_description TEXT,"
                + "status TEXT)";
        db.execSQL(CREATE_APPOINTMENTS_TABLE);

        // Create User Profile Table
        String CREATE_USER_PROFILE_TABLE = "CREATE TABLE " + TABLE_USER_PROFILE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_name TEXT,"
                + "mobile_number TEXT,"
                + "address TEXT,"
                + "dob TEXT,"
                + "aadhar_card TEXT,"
                + "sex TEXT,"
                + "blood_group TEXT,"
                + "parent_number TEXT)";
        db.execSQL(CREATE_USER_PROFILE_TABLE);



// Add this in onCreate() after other table creations
        String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PATIENT_NAME + " TEXT,"
                + COLUMN_REPORT_DATE + " TEXT,"
                + COLUMN_DIAGNOSIS + " TEXT);";  // Note the semicolon here

        db.execSQL(CREATE_REPORTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_DOCTORS + " ADD COLUMN doctor_name TEXT;");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_DOCTORS + " ADD COLUMN hospital_code TEXT;");
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_HOSPITAL_CODE + " TEXT;");
        }
        if (oldVersion < 14) {
            db.execSQL("ALTER TABLE " + TABLE_HOSPITALS + " ADD COLUMN " + COLUMN_TOTAL_EMPLOYEES + " INTEGER;");
            db.execSQL("ALTER TABLE " + TABLE_HOSPITALS + " ADD COLUMN " + COLUMN_HOSPITAL_ADDRESS + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_HOSPITALS + " ADD COLUMN " + COLUMN_CONTACT_NUMBER + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_HOSPITALS + " ADD COLUMN " + COLUMN_DEPARTMENTS + " TEXT;");

        }
        if (oldVersion < 17) {
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENTS + " ADD COLUMN user_email TEXT;");


        }
        if (oldVersion < 18) {
            // Handle schema changes for existing users
            db.execSQL("ALTER TABLE " + TABLE_APPOINTMENTS + " ADD COLUMN status TEXT DEFAULT 'Pending';");
        }
    }

    // Insert User
    public boolean insertUser(String email, String password, String role, String hospitalCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);

        // Set hospital_code only for Admin and Doctor roles
        if (role.equals("Admin") || role.equals("Doctor")) {
            if (hospitalCode == null || hospitalCode.isEmpty()) {
                return false; // Hospital code is required for Admin and Doctor
            }
            values.put(COLUMN_HOSPITAL_CODE, hospitalCode);
        } else {
            values.putNull(COLUMN_HOSPITAL_CODE); // Set hospital_code to null for User
        }

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Insert Hospital
    // Overloaded method for inserting only hospital name and code
    public boolean insertHospital(String hospitalName, String hospitalCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOSPITAL_NAME, hospitalName);
        values.put(COLUMN_HOSPITAL_CODE, hospitalCode);
        // Set default values for the new fields
        values.put(COLUMN_TOTAL_EMPLOYEES, 0); // Default value for total employees
        values.put(COLUMN_HOSPITAL_ADDRESS, ""); // Default value for address
        values.put(COLUMN_CONTACT_NUMBER, ""); // Default value for contact number
        values.put(COLUMN_DEPARTMENTS, ""); // Default value for departments
        long result = db.insert(TABLE_HOSPITALS, null, values);
        return result != -1;
    }

    // Existing method for inserting all hospital details
    public boolean insertHospital(String hospitalName, String hospitalCode, int totalEmployees,
                                  String hospitalAddress, String contactNumber, String departments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOSPITAL_NAME, hospitalName);
        values.put(COLUMN_HOSPITAL_CODE, hospitalCode);
        values.put(COLUMN_TOTAL_EMPLOYEES, totalEmployees);
        values.put(COLUMN_HOSPITAL_ADDRESS, hospitalAddress);
        values.put(COLUMN_CONTACT_NUMBER, contactNumber);
        values.put(COLUMN_DEPARTMENTS, departments);
        long result = db.insert(TABLE_HOSPITALS, null, values);
        return result != -1;
    }

    // Insert Doctor
    public boolean insertDoctor(String email, String specialization, String collegeName, String experience, String doctorId, String doctorName, String hospitalCode) {
        if (!checkHospitalCode(hospitalCode)) {
            return false; // Hospital does not exist
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_SPECIALIZATION, specialization);
        values.put(COLUMN_COLLEGE_NAME, collegeName);
        values.put(COLUMN_EXPERIENCE, experience);
        values.put(COLUMN_DOCTOR_ID, doctorId);
        values.put(COLUMN_DOCTOR_NAME, doctorName);
        values.put("hospital_code", hospitalCode);
        long result = db.insert(TABLE_DOCTORS, null, values);
        return result != -1;
    }

    // Save User Profile
    public boolean saveUserProfile(String userName, String mobileNumber, String address, String dob, String aadharCard, String sex, String bloodGroup, String parentNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", userName);
        values.put("mobile_number", mobileNumber);
        values.put("address", address);
        values.put("dob", dob);
        values.put("aadhar_card", aadharCard);
        values.put("sex", sex);
        values.put("blood_group", bloodGroup);
        values.put("parent_number", parentNumber);
        long result = db.insert(TABLE_USER_PROFILE, null, values);
        return result != -1;
    }

    // Get User Profile
    public String getUserProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_PROFILE, null, null, null, null, null, null);
        StringBuilder profile = new StringBuilder();
        if (cursor.moveToFirst()) {
            do {
                profile.append("Name: ").append(cursor.getString(1)).append("\n");
                profile.append("Mobile: ").append(cursor.getString(2)).append("\n");
                profile.append("Address: ").append(cursor.getString(3)).append("\n");
                profile.append("DOB: ").append(cursor.getString(4)).append("\n");
                profile.append("Aadhar: ").append(cursor.getString(5)).append("\n");
                profile.append("Sex: ").append(cursor.getString(6)).append("\n");
                profile.append("Blood Group: ").append(cursor.getString(7)).append("\n");
                profile.append("Parent's Number: ").append(cursor.getString(8)).append("\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        return profile.toString();
    }

    // Check if Hospital Code Exists
    public boolean checkHospitalCode(String hospitalCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HOSPITALS, new String[]{COLUMN_HOSPITAL_CODE},
                COLUMN_HOSPITAL_CODE + "=?", new String[]{hospitalCode}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Validate Login
    public boolean validateLogin(String email, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=? AND " + COLUMN_ROLE + "=?",
                new String[]{email, password, role}, null, null, null);
        boolean valid = cursor.getCount() > 0;
        cursor.close();
        return valid;
    }

    // Get Doctors by Specialization
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS,
                new String[]{COLUMN_EMAIL, COLUMN_SPECIALIZATION,
                        COLUMN_COLLEGE_NAME, COLUMN_EXPERIENCE,
                        COLUMN_DOCTOR_NAME, "hospital_code"},
                COLUMN_SPECIALIZATION + "=?", new String[]{specialization},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                doctors.add(new Doctor(
                        cursor.getString(0),  // email
                        cursor.getString(1),  // specialization
                        cursor.getString(2),  // collegeName
                        cursor.getString(3),  // experience
                        cursor.getString(4),  // doctorName
                        cursor.getString(5)  // hospitalCode
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return doctors;
    }

    // Fetch all hospital names
    public List<String> getAllHospitalNames() {
        List<String> hospitalNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HOSPITALS, new String[]{COLUMN_HOSPITAL_NAME},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                hospitalNames.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return hospitalNames;
    }

    // Fetch hospital code by hospital name
    public String getHospitalCodeByName(String hospitalName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HOSPITALS, new String[]{COLUMN_HOSPITAL_CODE},
                COLUMN_HOSPITAL_NAME + "=?", new String[]{hospitalName}, null, null, null);

        String hospitalCode = "";
        if (cursor.moveToFirst()) {
            hospitalCode = cursor.getString(0);
        }
        cursor.close();
        return hospitalCode;
    }

    // Fetch doctors by hospital code
    public List<Doctor> getDoctorsByHospitalCode(String hospitalCode) {
        List<Doctor> doctors = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS,
                new String[]{COLUMN_EMAIL, COLUMN_SPECIALIZATION,
                        COLUMN_COLLEGE_NAME, COLUMN_EXPERIENCE,
                        COLUMN_DOCTOR_NAME, "hospital_code"},
                "hospital_code=?", new String[]{hospitalCode},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                doctors.add(new Doctor(
                        cursor.getString(0),  // email
                        cursor.getString(1),  // specialization
                        cursor.getString(2),  // collegeName
                        cursor.getString(3),  // experience
                        cursor.getString(4),  // doctorName
                        cursor.getString(5)  // hospitalCode
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return doctors;
    }

    // Insert appointment request
    public boolean insertAppointmentRequest(String userName, String userEmail,String hospitalCode, String doctorEmail, String date, String time, String problemDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", userName);
        values.put("user_email", userEmail);
        values.put("hospital_code", hospitalCode);
        values.put("doctor_email", doctorEmail);
        values.put("date", date);
        values.put("time", time);
        values.put("problem_description", problemDescription);
        values.put("status", "Pending");
        long result = db.insert(TABLE_APPOINTMENTS, null, values);
        return result != -1;
    }

    // Fetch pending appointments for a specific hospital
    public List<Appointment> getPendingAppointments(String hospitalCode) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPOINTMENTS,
                new String[]{"id", "user_name","user_email" ,"doctor_email", "date", "time",
                        "problem_description", "hospital_code","status"},
                "hospital_code=? AND status=?", new String[]{hospitalCode, "Pending"},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                appointments.add(new Appointment(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }
    // Update appointment status (Accept/Reject)
    public boolean updateAppointmentStatus(int appointmentId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        int result = db.update(TABLE_APPOINTMENTS, values, "id=?", new String[]{String.valueOf(appointmentId)});
        return result > 0;
    }

    // Get hospital code by admin email
    // In DatabaseHelper.java
    // In DatabaseHelper.java
    public String getHospitalCodeByAdminEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_HOSPITAL_CODE},
                COLUMN_EMAIL + "=? AND " + COLUMN_ROLE + "=?",
                new String[]{email, "Admin"}, null, null, null);

        String hospitalCode = "";
        if (cursor.moveToFirst()) {
            hospitalCode = cursor.getString(0);
            Log.d("DatabaseHelper", "Found hospital code: " + hospitalCode + " for email: " + email);
        } else {
            Log.d("DatabaseHelper", "No hospital code found for email: " + email + ". Check that the admin record exists with role 'Admin'");
        }
        cursor.close();
        return hospitalCode;
    }



    // In DatabaseHelper.java
    public List<Appointment> getAcceptedAppointments(String hospitalCode) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPOINTMENTS,
                new String[]{"id", "user_name","user_email", "doctor_email", "date", "time", "problem_description", "hospital_code","status"},
                "hospital_code=? AND status=?", new String[]{hospitalCode, "Accepted"}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                appointments.add(new Appointment(
                        cursor.getInt(0),       // id
                        cursor.getString(1),    // user_name
                        cursor.getString(2),
                        cursor.getString(3),    // doctor_email
                        cursor.getString(4),    // date
                        cursor.getString(5),    // time
                        cursor.getString(6),    // problem_description
                        cursor.getString(7),     // hospital_code (new parameter)
                        cursor.getString(8)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }

    // In DatabaseHelper.java
    // In DatabaseHelper.java
    public String getHospitalCodeByDoctorEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS,
                new String[]{"hospital_code"},
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        String hospitalCode = "";
        if (cursor.moveToFirst()) {
            hospitalCode = cursor.getString(0);
        }
        cursor.close();
        return hospitalCode;
    }

    // Add this method to DatabaseHelper class
    public boolean deleteDoctor(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_DOCTORS, COLUMN_EMAIL + "=?", new String[]{email}) > 0;
    }
    // User-specific methods
    public List<Appointment> getAcceptedAppointmentsByUser(String userName) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_APPOINTMENTS,
                new String[]{"id", "user_name","user_email", "doctor_email", "date", "time",
                        "problem_description", "hospital_code","status"},
                "user_name=? AND status=?", new String[]{userName, "Accepted"},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                appointments.add(new Appointment(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }

    public String getHospitalNameByCode(String hospitalCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HOSPITALS,
                new String[]{COLUMN_HOSPITAL_NAME},
                COLUMN_HOSPITAL_CODE + "=?", new String[]{hospitalCode},
                null, null, null);
        String hospitalName = "Unknown Hospital";
        if (cursor.moveToFirst()) {
            hospitalName = cursor.getString(0);
        }
        cursor.close();
        return hospitalName;
    }

    public Doctor getDoctorByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DOCTORS,
                new String[]{COLUMN_SPECIALIZATION, COLUMN_DOCTOR_NAME, "hospital_code"},
                COLUMN_EMAIL + "=?", new String[]{email}, null, null, null);

        Doctor doctor = null;
        if (cursor.moveToFirst()) {
            doctor = new Doctor(
                    email,
                    cursor.getString(0),  // specialization
                    "",  // collegeName
                    "",  // experience
                    cursor.getString(1),  // doctorName
                    cursor.getString(2)  // hospitalCode
            );
        }
        cursor.close();
        return doctor;
    }
    public String getUserNameFromProfile() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER_PROFILE,
                new String[]{"user_name"}, null, null, null, null, null);
        String userName = "";
        if (cursor.moveToFirst()) {
            userName = cursor.getString(0);
        }
        cursor.close();
        return userName;
    }

    public List<Appointment> getAcceptedAppointmentsByUserEmail(String userEmail) {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_APPOINTMENTS +
                " WHERE user_email = ? AND status = ?";

        Cursor cursor = db.rawQuery(query, new String[]{userEmail, "Accepted"});

        if (cursor.moveToFirst()) {
            do {
                appointments.add(new Appointment(
                        cursor.getInt(0),         // id (0)
                        cursor.getString(1),      // user_name (1)
                        cursor.getString(2),      // user_email (2)
                        cursor.getString(4),      // doctor_email (4)
                        cursor.getString(5),      // date (5)
                        cursor.getString(6),      // time (6)
                        cursor.getString(7),      // problem_description (7)
                        cursor.getString(3),      // hospital_code (3)
                        cursor.getString(8)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return appointments;
    }





}