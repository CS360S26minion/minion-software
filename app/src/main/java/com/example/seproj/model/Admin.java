package com.example.seproj.model;

/**
 * Represents an administrator account that can access analytics and counselor management screens.
 * Stores the minimum admin profile fields used by Firebase login and admin UI flows.
 *
 * Outstanding issues:
 * - Admin permissions are role-based in the UI but not modeled as fine-grained privileges yet.
 */
public class Admin {
    private String adminId;
    private String name;
    private String email;

    /**
     * Required empty constructor for Firestore deserialization.
     */
    public Admin() {}

    /**
     * Creates a Admin instance with its persisted field values.
     *
     * @param adminId the admin ID value.
     * @param name the name value.
     * @param email the email value.
     */
    public Admin(String adminId, String name, String email) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the admin ID.
     *
     * @return the current admin ID value.
     */
    public String getAdminId() { return adminId; }
    /**
     * Updates the admin ID.
     *
     * @param adminId the admin ID value.
     */
    public void setAdminId(String adminId) { this.adminId = adminId; }

    /**
     * Returns the name.
     *
     * @return the current name value.
     */
    public String getName() { return name; }
    /**
     * Updates the name.
     *
     * @param name the name value.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the email.
     *
     * @return the current email value.
     */
    public String getEmail() { return email; }
    /**
     * Updates the email.
     *
     * @param email the email value.
     */
    public void setEmail(String email) { this.email = email; }
}


