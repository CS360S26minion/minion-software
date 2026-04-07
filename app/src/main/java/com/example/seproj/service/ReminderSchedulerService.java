package com.example.seproj.service;

import android.content.Context;

import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.seproj.workers.CounselorReminderWorker;
import com.example.seproj.workers.StudentReminderWorker;

import java.util.concurrent.TimeUnit;

/**
 * Schedules and cancels one-time reminder workers for appointments.
 *
 * Design:
 * - when an appointment is booked, schedule one student reminder worker
 * - schedule one counselor reminder worker
 * - both are set to run about 24 hours before the appointment
 *
 * Outstanding issues:
 * - WorkManager is not exact-to-the-minute
 * - rescheduling flow will need to cancel old work and create new work
 */
public class ReminderSchedulerService {

    public static final String KEY_SLOT_ID = "slotId";
    public static final String KEY_STUDENT_ID = "studentId";
    public static final String KEY_COUNSELOR_ID = "counselorId";
    public static final String KEY_APPOINTMENT_START = "appointmentStartMillis";

    private static final long TWENTY_FOUR_HOURS_MILLIS = 24L * 60L * 60L * 1000L;

    public static void scheduleAppointmentReminders(Context context,
                                                    String slotId,
                                                    String studentId,
                                                    String counselorId,
                                                    long appointmentStartMillis) {

        long now = System.currentTimeMillis();
        long triggerAtMillis = appointmentStartMillis - TWENTY_FOUR_HOURS_MILLIS;
        long delayMillis = Math.max(0L, triggerAtMillis - now);

        Data studentData = new Data.Builder()
                .putString(KEY_SLOT_ID, slotId)
                .putString(KEY_STUDENT_ID, studentId)
                .putLong(KEY_APPOINTMENT_START, appointmentStartMillis)
                .build();

        Data counselorData = new Data.Builder()
                .putString(KEY_SLOT_ID, slotId)
                .putString(KEY_COUNSELOR_ID, counselorId)
                .putLong(KEY_APPOINTMENT_START, appointmentStartMillis)
                .build();

        OneTimeWorkRequest studentReminderRequest =
                new OneTimeWorkRequest.Builder(StudentReminderWorker.class)
                        .setInputData(studentData)
                        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                        .build();

        OneTimeWorkRequest counselorReminderRequest =
                new OneTimeWorkRequest.Builder(CounselorReminderWorker.class)
                        .setInputData(counselorData)
                        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                        .build();

        WorkManager workManager = WorkManager.getInstance(context);

        workManager.enqueueUniqueWork(
                getStudentReminderWorkName(slotId),
                ExistingWorkPolicy.REPLACE,
                studentReminderRequest
        );

        workManager.enqueueUniqueWork(
                getCounselorReminderWorkName(slotId),
                ExistingWorkPolicy.REPLACE,
                counselorReminderRequest
        );
    }

    public static void cancelAppointmentReminders(Context context, String slotId) {
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.cancelUniqueWork(getStudentReminderWorkName(slotId));
        workManager.cancelUniqueWork(getCounselorReminderWorkName(slotId));
    }

    public static String getStudentReminderWorkName(String slotId) {
        return "student_reminder_" + slotId;
    }

    public static String getCounselorReminderWorkName(String slotId) {
        return "counselor_reminder_" + slotId;
    }
}