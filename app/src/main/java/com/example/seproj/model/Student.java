package com.example.seproj.model;
/**
 * Represents a student user in the counseling clinic system.
 * This model is used to store student profile data and track
 * the student's currently active appointment if one exists.
 *
 * Outstanding issues:
 * - Authentication is not included yet.
 * - Role-based access control will be handled later.
 */
public class Student {
    private String studentId;
    private String name;
    private String email;
    private String activeAppointmentId;

    // Required empty constructor for Firestore
    public Student() {
    }

    public Student(String studentId, String name, String email, String activeAppointmentId) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.activeAppointmentId = activeAppointmentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActiveAppointmentId() {
        return activeAppointmentId;
    }

    public void setActiveAppointmentId(String activeAppointmentId) {
        this.activeAppointmentId = activeAppointmentId;
    }

    public boolean hasActiveAppointment() {
        return activeAppointmentId != null && !activeAppointmentId.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", activeAppointmentId='" + activeAppointmentId + '\'' +
                '}';
    }
}