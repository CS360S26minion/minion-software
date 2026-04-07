package com.example.seproj.model;


/**
 * Represents an in-app notification stored in Firestore.
 * Notifications are used for booking confirmations, cancellations,
 * and 24-hour reminders.
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

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientRole() {
        return recipientRole;
    }

    public void setRecipientRole(String recipientRole) {
        this.recipientRole = recipientRole;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public long getCreatedAtMillis() {
        return createdAtMillis;
    }

    public void setCreatedAtMillis(long createdAtMillis) {
        this.createdAtMillis = createdAtMillis;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
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
