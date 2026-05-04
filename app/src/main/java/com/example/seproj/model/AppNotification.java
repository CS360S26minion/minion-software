package com.example.seproj.model;


/**
 * Represents an in-app notification stored in Firestore.
 * Notifications are used for booking confirmations, cancellations,
 * and 24-hour reminders.
 *
 * Outstanding issues:
 * - Push notifications are not fully implemented yet.
 * - Read/unread filtering UI will be added later if needed.
 */
public class AppNotification {
    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_COUNSELOR = "counselor";

    public static final String TYPE_BOOKING_CONFIRMATION = "booking_confirmation";
    public static final String TYPE_CANCELLATION = "cancellation";
    public static final String TYPE_REMINDER_24H = "reminder_24h";

    private String notificationId;
    private String recipientId;
    private String recipientRole;
    private String title;
    private String message;
    private String appointmentId;
    private long createdAtMillis;
    private String type;
    private boolean read;

    // Required empty constructor for Firestore
    /**
     * Required empty constructor for Firestore deserialization.
     */
    public AppNotification() {
    }

    public AppNotification(String notificationId, String recipientId, String recipientRole,
                           String title, String message, String appointmentId,
                           long createdAtMillis, String type, boolean read) {
        this.notificationId = notificationId;
        this.recipientId = recipientId;
        this.recipientRole = recipientRole;
        this.title = title;
        this.message = message;
        this.appointmentId = appointmentId;
        this.createdAtMillis = createdAtMillis;
        this.type = type;
        this.read = read;
    }

    /**
     * Returns the notification ID.
     *
     * @return the current notification ID value.
     */
    public String getNotificationId() {
        return notificationId;
    }

    /**
     * Updates the notification ID.
     *
     * @param notificationId the notification ID value.
     */
    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    /**
     * Returns the recipient ID.
     *
     * @return the current recipient ID value.
     */
    public String getRecipientId() {
        return recipientId;
    }

    /**
     * Updates the recipient ID.
     *
     * @param recipientId the recipient ID value.
     */
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    /**
     * Returns the recipient role.
     *
     * @return the current recipient role value.
     */
    public String getRecipientRole() {
        return recipientRole;
    }

    /**
     * Updates the recipient role.
     *
     * @param recipientRole the recipient role value.
     */
    public void setRecipientRole(String recipientRole) {
        this.recipientRole = recipientRole;
    }

    /**
     * Returns the title.
     *
     * @return the current title value.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the title.
     *
     * @param title the title value.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the message.
     *
     * @return the current message value.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the message.
     *
     * @param message the message value.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the appointment ID.
     *
     * @return the current appointment ID value.
     */
    public String getAppointmentId() {
        return appointmentId;
    }

    /**
     * Updates the appointment ID.
     *
     * @param appointmentId the appointment ID value.
     */
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Returns the created at millis.
     *
     * @return the current created at millis value.
     */
    public long getCreatedAtMillis() {
        return createdAtMillis;
    }

    /**
     * Updates the created at millis.
     *
     * @param createdAtMillis the created at millis value.
     */
    public void setCreatedAtMillis(long createdAtMillis) {
        this.createdAtMillis = createdAtMillis;
    }

    /**
     * Returns the type.
     *
     * @return the current type value.
     */
    public String getType() {
        return type;
    }

    /**
     * Updates the type.
     *
     * @param type the type value.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Checks whether read is true.
     *
     * @return true when read is true.
     */
    public boolean isRead() {
        return read;
    }

    /**
     * Updates the read.
     *
     * @param read the read value.
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    /**
     * Builds a debug-friendly string representation of this AppNotification.
     *
     * @return a string containing the main field values.
     */
    public String toString() {
        return "AppNotification{" +
                "notificationId='" + notificationId + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", recipientRole='" + recipientRole + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", appointmentId='" + appointmentId + '\'' +
                ", createdAtMillis=" + createdAtMillis +
                ", type='" + type + '\'' +
                ", read=" + read +
                '}';
    }
}
