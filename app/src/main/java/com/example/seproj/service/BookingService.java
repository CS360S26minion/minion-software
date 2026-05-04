package com.example.seproj.service;

import android.content.Context;

import com.example.seproj.model.AppointmentSlot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

/**
 * Service class for booking-related business logic.
 * Handles booking, cancellation, notifications, and reminder scheduling.
 */
public class BookingService {

    public interface BookingCallback {
        void onSuccess(String message);
        void onFailure(String errorMessage);
    }

    private static final String STUDENTS_COLLECTION = "students";
    private static final String SLOTS_COLLECTION = "appointment_slots";

    private final FirebaseFirestore db;
    private final NotificationService notificationService;
    private final Context appContext;

    public BookingService(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.notificationService = new NotificationService();
        this.appContext = context.getApplicationContext();
    }

    public void bookSlot(String studentId, String slotId, BookingCallback callback) {
        DocumentReference studentRef = db.collection(STUDENTS_COLLECTION).document(studentId);
        DocumentReference slotRef = db.collection(SLOTS_COLLECTION).document(slotId);

        final String[] counselorIdHolder = new String[1];
        final long[] startTimeHolder = new long[1];

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot studentSnapshot = transaction.get(studentRef);
            DocumentSnapshot slotSnapshot = transaction.get(slotRef);

            if (!studentSnapshot.exists()) {
                throw new RuntimeException("Student record not found.");
            }

            if (!slotSnapshot.exists()) {
                throw new RuntimeException("Appointment slot not found.");
            }

            String activeAppointmentId = studentSnapshot.getString("activeAppointmentId");
            String currentStatus = slotSnapshot.getString("status");
            String counselorId = slotSnapshot.getString("counselorId");
            Long startTimeMillis = slotSnapshot.getLong("startTimeMillis");
            if (startTimeMillis == null) {
                throw new RuntimeException("Slot time information missing.");
            }

            if (startTimeMillis <= System.currentTimeMillis()) {
                throw new RuntimeException("You cannot book a slot that has already passed.");
            }
            if (activeAppointmentId != null && !activeAppointmentId.trim().isEmpty()) {
                DocumentReference activeSlotRef =
                        db.collection(SLOTS_COLLECTION).document(activeAppointmentId);

                DocumentSnapshot activeSlotSnapshot = transaction.get(activeSlotRef);

                if (activeSlotSnapshot.exists()) {
                    Long activeEndTime = activeSlotSnapshot.getLong("endTimeMillis");
                    String activeStatus = activeSlotSnapshot.getString("status");

                    boolean stillUpcoming =
                            activeEndTime != null
                                    && activeEndTime > System.currentTimeMillis()
                                    && AppointmentSlot.STATUS_BOOKED.equals(activeStatus);

                    if (stillUpcoming) {
                        throw new RuntimeException("Student already has an active appointment.");
                    }
                }

                transaction.update(studentRef, "activeAppointmentId", null);
            }

            if (!AppointmentSlot.STATUS_AVAILABLE.equals(currentStatus)) {
                throw new RuntimeException("This slot is no longer available.");
            }

            if (counselorId == null || counselorId.trim().isEmpty()) {
                throw new RuntimeException("Counselor information missing for this slot.");
            }

            if (startTimeMillis == null) {
                throw new RuntimeException("Slot time information missing.");
            }

            counselorIdHolder[0] = counselorId;
            startTimeHolder[0] = startTimeMillis;

            transaction.update(slotRef,
                    "status", AppointmentSlot.STATUS_BOOKED,
                    "studentId", studentId);

            transaction.update(studentRef,
                    "activeAppointmentId", slotId);

            return null;
        }).addOnSuccessListener(unused -> {
            notificationService.createBookingNotifications(
                    studentId,
                    counselorIdHolder[0],
                    slotId,
                    startTimeHolder[0],
                    new NotificationService.NotificationActionCallback() {
                        @Override
                        public void onComplete() {
                            ReminderSchedulerService.scheduleAppointmentReminders(
                                    appContext,
                                    slotId,
                                    studentId,
                                    counselorIdHolder[0],
                                    startTimeHolder[0]
                            );

                            if (callback != null) {
                                callback.onSuccess("Appointment booked successfully.");
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            ReminderSchedulerService.scheduleAppointmentReminders(
                                    appContext,
                                    slotId,
                                    studentId,
                                    counselorIdHolder[0],
                                    startTimeHolder[0]
                            );

                            if (callback != null) {
                                callback.onSuccess("Appointment booked successfully, but notification creation failed.");
                            }
                        }
                    }
            );
        }).addOnFailureListener(e -> {
            if (callback != null) {
                callback.onFailure(e.getMessage() != null ? e.getMessage() : "Booking failed.");
            }
        });
    }

    public void cancelSlot(String studentId, String slotId, BookingCallback callback) {
        DocumentReference studentRef = db.collection(STUDENTS_COLLECTION).document(studentId);
        DocumentReference slotRef = db.collection(SLOTS_COLLECTION).document(slotId);

        final String[] counselorIdHolder = new String[1];
        final long[] startTimeHolder = new long[1];

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot studentSnapshot = transaction.get(studentRef);
            DocumentSnapshot slotSnapshot = transaction.get(slotRef);

            if (!studentSnapshot.exists()) {
                throw new RuntimeException("Student record not found.");
            }

            if (!slotSnapshot.exists()) {
                throw new RuntimeException("Appointment slot not found.");
            }

            String slotStudentId = slotSnapshot.getString("studentId");
            String currentStatus = slotSnapshot.getString("status");
            String counselorId = slotSnapshot.getString("counselorId");
            Long startTimeMillis = slotSnapshot.getLong("startTimeMillis");

            if (!AppointmentSlot.STATUS_BOOKED.equals(currentStatus)) {
                throw new RuntimeException("This slot is not currently booked.");
            }

            if (slotStudentId == null || !slotStudentId.equals(studentId)) {
                throw new RuntimeException("You can only cancel your own appointment.");
            }

            if (counselorId == null || counselorId.trim().isEmpty()) {
                throw new RuntimeException("Counselor information missing for this slot.");
            }

            if (startTimeMillis == null) {
                throw new RuntimeException("Slot time information missing.");
            }

            counselorIdHolder[0] = counselorId;
            startTimeHolder[0] = startTimeMillis;

            transaction.update(slotRef,
                    "status", AppointmentSlot.STATUS_AVAILABLE,
                    "studentId", null,
                    "reminder24hSentStudent", false,
                    "reminder24hSentCounselor", false);

            transaction.update(studentRef,
                    "activeAppointmentId", null);

            return null;
        }).addOnSuccessListener(unused -> {
            ReminderSchedulerService.cancelAppointmentReminders(appContext, slotId);

            notificationService.createCancellationNotifications(
                    studentId,
                    counselorIdHolder[0],
                    slotId,
                    startTimeHolder[0],
                    new NotificationService.NotificationActionCallback() {
                        @Override
                        public void onComplete() {
                            if (callback != null) {
                                callback.onSuccess("Appointment cancelled successfully.");
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            if (callback != null) {
                                callback.onSuccess("Appointment cancelled successfully, but notification creation failed.");
                            }
                        }
                    }
            );
        }).addOnFailureListener(e -> {
            if (callback != null) {
                callback.onFailure(e.getMessage() != null ? e.getMessage() : "Cancellation failed.");
            }
        });
    }

    public void cancelSlotByCounselor(String counselorId, String slotId, BookingCallback callback) {
        DocumentReference slotRef = db.collection(SLOTS_COLLECTION).document(slotId);
        final String[] studentIdHolder = new String[1];
        final long[] startTimeHolder = new long[1];

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot slotSnapshot = transaction.get(slotRef);

            if (!slotSnapshot.exists()) {
                throw new RuntimeException("Appointment slot not found.");
            }

            String currentStatus = slotSnapshot.getString("status");
            String slotCounselorId = slotSnapshot.getString("counselorId");
            String studentId = slotSnapshot.getString("studentId");
            Long startTimeMillis = slotSnapshot.getLong("startTimeMillis");

            if (slotCounselorId == null || !slotCounselorId.equals(counselorId)) {
                throw new RuntimeException("You can only cancel your own appointment slots.");
            }

            if (startTimeMillis == null) {
                throw new RuntimeException("Slot time information missing.");
            }

            if (startTimeMillis <= System.currentTimeMillis()) {
                throw new RuntimeException("Past slots cannot be cancelled.");
            }

            if (!AppointmentSlot.STATUS_BOOKED.equals(currentStatus)
                    && !AppointmentSlot.STATUS_AVAILABLE.equals(currentStatus)) {
                throw new RuntimeException("This slot cannot be cancelled.");
            }

            studentIdHolder[0] = studentId;
            startTimeHolder[0] = startTimeMillis;

            transaction.update(slotRef, "status", AppointmentSlot.STATUS_CANCELLED);

            if (studentId != null && !studentId.trim().isEmpty()) {
                DocumentReference studentRef = db.collection(STUDENTS_COLLECTION).document(studentId);
                transaction.update(studentRef, "activeAppointmentId", null);
            }

            return null;
        }).addOnSuccessListener(unused -> {
            ReminderSchedulerService.cancelAppointmentReminders(appContext, slotId);

            if (studentIdHolder[0] == null || studentIdHolder[0].trim().isEmpty()) {
                if (callback != null) {
                    callback.onSuccess("Slot cancelled successfully.");
                }
                return;
            }

            notificationService.createCounselorCancellationNotification(
                    studentIdHolder[0],
                    counselorId,
                    slotId,
                    startTimeHolder[0],
                    new NotificationService.NotificationActionCallback() {
                        @Override
                        public void onComplete() {
                            if (callback != null) {
                                callback.onSuccess("Appointment cancelled and student notified.");
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            if (callback != null) {
                                callback.onSuccess("Appointment cancelled, but notification creation failed.");
                            }
                        }
                    });
        }).addOnFailureListener(e -> {
            if (callback != null) {
                callback.onFailure(e.getMessage() != null ? e.getMessage() : "Cancellation failed.");
            }
        });
    }
}
