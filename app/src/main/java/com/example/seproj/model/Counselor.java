package com.example.seproj.model;
/**
 * Represents a counselor in the counseling clinic system.
 * A counselor can have a profile, specialization, and active status.
 *
 *  Outstanding issues:
 * - Profile photo and richer bio fields may be added later.
 * - Admin-managed role settings are not included yet.
 */
public class Counselor {
    private String counselorId;
    private String name;
    private String email;
    private String specialization;
    private String bio;
    private boolean active;

    // Required empty constructor for Firestore
    public Counselor() {
    }

    public Counselor(String counselorId, String name, String email,
                     String specialization, String bio, boolean active) {
        this.counselorId = counselorId;
        this.name = name;
        this.email = email;
        this.specialization = specialization;
        this.bio = bio;
        this.active = active;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Counselor{" +
                "counselorId='" + counselorId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", specialization='" + specialization + '\'' +
                ", bio='" + bio + '\'' +
                ", active=" + active +
                '}';
    }
}