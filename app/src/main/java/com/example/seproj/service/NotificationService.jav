package com.example.seproj.service;

import com.example.seproj.model.AppNotification;
import com.example.seproj.repository.NotificationRepository;
import com.example.seproj.utils.FirestoreCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Service class for creating in-app notifications for booking,
 * cancellation, and later reminder events.
 *
 * Current scope:
 * - Student booking confirmation
 * - Counselor booking confirmation
 * - Student cancellation notification
 * - Counselor cancellation notification
 *
 * Outstanding issues:
 * - Local device push notifications can be added later.
 * - Reminder notifications will be added in the worker step.
 */
public class NotificationService {

    public interface NotificationActionCallback {
        void onComplete();
        void onFailure(String errorMessage);
    }

    private final NotificationRepository notificationRepository;

    public NotificationService() {
        this.notificationRepository = new NotificationRepository();
    }

    public void createBookingNotifications(String studentId,
                                           String counselorId,
                                           String slotId,
                                           long startTimeMillis,
                                           NotificationActionCallback callback) {

        String formattedTime = formatDateTime(startTimeMillis);

        AppNotification studentNotification = new AppNotification(
                UUID.randomUUID().toString(),
                studentId,
                AppNotification.ROLE_STUDENT,
                "Booking Confirmed",
                "Your appointment has been booked for " + formattedTime + ".",
                slotId,
                System.currentTimeMillis(),
                AppNotification.TYPE_BOOKING_CONFIRMATION,
                false
        );

        AppNotification counselorNotification = new AppNotification(
                UUID.randomUUID().toString(),
                counselorId,
                AppNotification.ROLE_COUNSELOR,
                "New Appointment Booked",
                "A student has booked an appointment for " + formattedTime + ".",
                slotId,
                System.currentTimeMillis(),
                AppNotification.TYPE_BOOKING_CONFIRMATION,
                false
        );

        saveTwoNotifications(studentNotification, counselorNotification, callback);
    }

    public void createCancellationNotifications(String studentId,
                                                String counselorId,
                                                String slotId,
                                                long startTimeMillis,
                                                NotificationActionCallback callback) {

        String formattedTime = formatDateTime(startTimeMillis);

        AppNotification studentNotification = new AppNotification(
                UUID.randomUUID().toString(),
                studentId,
                AppNotification.ROLE_STUDENT,
                "Appointment Cancelled",
                "Your appointment for " + formattedTime + " has been cancelled.",
                slotId,
                System.currentTimeMillis(),
                AppNotification.TYPE_CANCELLATION,
                false
        );

        AppNotification counselorNotification = new AppNotification(
                UUID.randomUUID().toString(),
                counselorId,
                AppNotification.ROLE_COUNSELOR,
                "Appointment Cancelled",
                "A student cancelled the appointment scheduled for " + formattedTime + ".",
                slotId,
                System.currentTimeMillis(),
                AppNotification.TYPE_CANCELLATION,
                false
        );

        saveTwoNotifications(studentNotification, counselorNotification, callback);
    }

    private void saveTwoNotifications(AppNotification first,
                                      AppNotification second,
                                      NotificationActionCallback callback) {
        notificationRepository.addNotification(first, new FirestoreCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                notificationRepository.addNotification(second, new FirestoreCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        if (callback != null) {
                            callback.onComplete();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        if (callback != null) {
                            callback.onFailure("Failed to save second notification: " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                if (callback != null) {
                    callback.onFailure("Failed to save first notification: " + e.getMessage());
                }
            }
        });
    }

    private String formatDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
}
