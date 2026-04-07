package com.example.seproj.workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.seproj.model.AppNotification;
import com.example.seproj.service.ReminderSchedulerService;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * One-time worker for sending a counselor reminder notification.
 */
public class CounselorReminderWorker extends Worker {

    public CounselorReminderWorker(@NonNull Context context,
                                   @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String slotId = getInputData().getString(ReminderSchedulerService.KEY_SLOT_ID);
        String counselorId = getInputData().getString(ReminderSchedulerService.KEY_COUNSELOR_ID);
        long appointmentStartMillis =
                getInputData().getLong(ReminderSchedulerService.KEY_APPOINTMENT_START, -1L);

        if (slotId == null || counselorId == null || appointmentStartMillis <= 0) {
            return Result.failure();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        try {
            DocumentSnapshot slotSnapshot = Tasks.await(
                    db.collection("appointment_slots").document(slotId).get()
            );

            if (!slotSnapshot.exists()) {
                return Result.failure();
            }

            String status = slotSnapshot.getString("status");
            Boolean alreadySent = slotSnapshot.getBoolean("reminder24hSentCounselor");

            if (!"booked".equals(status)) {
                return Result.success();
            }

            if (Boolean.TRUE.equals(alreadySent)) {
                return Result.success();
            }

            String formattedTime = formatDateTime(appointmentStartMillis);

            AppNotification notification = new AppNotification(
                    UUID.randomUUID().toString(),
                    counselorId,
                    AppNotification.ROLE_COUNSELOR,
                    "Appointment Reminder",
                    "Reminder: you have an appointment in about 24 hours at " + formattedTime + ".",
                    slotId,
                    System.currentTimeMillis(),
                    AppNotification.TYPE_REMINDER_24H,
                    false
            );

            Tasks.await(
                    db.collection("notifications")
                            .document(notification.getNotificationId())
                            .set(notification)
            );

            Tasks.await(
                    db.collection("appointment_slots")
                            .document(slotId)
                            .update("reminder24hSentCounselor", true)
            );

            return Result.success();
        } catch (Exception e) {
            return Result.retry();
        }
    }

    private String formatDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
}