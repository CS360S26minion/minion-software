package com.example.seproj.model;

import androidx.annotation.NonNull;

/**
 * Represents a counselor in the counseling clinic system.
 * A counselor can have a profile, specialization, and active status.
 *
 * Outstanding issues:
 * - Profile photo and richer bio fields may be added later.
 * - Admin-managed role settings are not included yet.
 */
public class Counselor {
    private String counselorId;
    private String name;
    private String email;
    private String password;
    private String specialization;
    private String bio;
    private boolean active;
    private double averageRating;
    private int ratingCount;

    // Required empty constructor for Firestore
    /**
     * Required empty constructor for Firestore deserialization.
     */
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

    /**
     * Returns the counselor ID.
     *
     * @return the current counselor ID value.
     */
    public String getCounselorId() {
        return counselorId;
    }

    /**
     * Updates the counselor ID.
     *
     * @param counselorId the counselor ID value.
     */
    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
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
     * Returns the specialization.
     *
     * @return the current specialization value.
     */
    public String getSpecialization() {
        return specialization;
    }

    /**
     * Updates the specialization.
     *
     * @param specialization the specialization value.
     */
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * Returns the bio.
     *
     * @return the current bio value.
     */
    public String getBio() {
        return bio;
    }

    /**
     * Updates the bio.
     *
     * @param bio the bio value.
     */
    public void setBio(String bio) {
        this.bio = bio;
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
     * @return
     * @throws CloneNotSupportedException
     */
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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
     * Checks whether active is true.
     *
     * @return true when active is true.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Updates the active.
     *
     * @param active the active value.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns the average rating.
     *
     * @return the current average rating value.
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * Updates the average rating.
     *
     * @param averageRating the average rating value.
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * Returns the rating count.
     *
     * @return the current rating count value.
     */
    public int getRatingCount() {
        return ratingCount;
    }

    /**
     * Updates the rating count.
     *
     * @param ratingCount the rating count value.
     */
    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    @Override
    /**
     * Builds a debug-friendly string representation of this Counselor.
     *
     * @return a string containing the main field values.
     */
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
