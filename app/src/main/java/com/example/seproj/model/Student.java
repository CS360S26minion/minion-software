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
    private String password;
    private String activeAppointmentId;

    // Required empty constructor for Firestore
    /**
     * Required empty constructor for Firestore deserialization.
     */
    public Student() {
    }

    /**
     * Creates a Student instance with its persisted field values.
     *
     * @param studentId the student ID value.
     * @param name the name value.
     * @param email the email value.
     * @param activeAppointmentId the active appointment ID value.
     */
    public Student(String studentId, String name, String email, String activeAppointmentId) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.activeAppointmentId = activeAppointmentId;
    }

    /**
     * Returns the student ID.
     *
     * @return the current student ID value.
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Updates the student ID.
     *
     * @param studentId the student ID value.
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Returns the name.
     *
     * @return the current name value.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name.
     *
     * @param name the name value.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email.
     *
     * @return the current email value.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the email.
     *
     * @param email the email value.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Returns the password.
     *
     * @return the current password value.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the password.
     *
     * @param password the password value.
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * Returns the active appointment ID.
     *
     * @return the current active appointment ID value.
     */
    public String getActiveAppointmentId() {
        return activeAppointmentId;
    }

    /**
     * Updates the active appointment ID.
     *
     * @param activeAppointmentId the active appointment ID value.
     */
    public void setActiveAppointmentId(String activeAppointmentId) {
        this.activeAppointmentId = activeAppointmentId;
    }

    /**
     * Checks whether this student currently has an active appointment reference.
     *
     * @return true when an active appointment ID is present.
     */
    public boolean hasActiveAppointment() {
        return activeAppointmentId != null && !activeAppointmentId.trim().isEmpty();
    }

    @Override
    /**
     * Builds a debug-friendly string representation of this Student.
     *
     * @return a string containing the main field values.
     */
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", activeAppointmentId='" + activeAppointmentId + '\'' +
                '}';
    }
}
