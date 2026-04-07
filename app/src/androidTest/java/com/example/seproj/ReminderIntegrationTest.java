package com.example.seproj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.testing.TestListenableWorkerBuilder;

import com.example.seproj.model.AppNotification;
import com.example.seproj.service.ReminderSchedulerService;
import com.example.seproj.workers.CounselorReminderWorker;
import com.example.seproj.workers.StudentReminderWorker;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Real Firestore integration tests for reminder workers.
 *
 * This version does NOT use Firestore emulator.
 * It writes test data into the real Firestore project, then deletes it.
 *
 * What it verifies:
 * - student reminder notification is created
 * - counselor reminder notification is created
 * - reminder flags are updated
 * - duplicate reminders are prevented
 */
@RunWith(AndroidJUnit4.class)
public class ReminderIntegrationTest {

    private FirebaseFirestore db;
    private Context context;

    // Track created IDs so cleanup is safer
    private final Set<String> createdStudentIds = new HashSet<>();
    private final Set<String> createdCounselorIds = new HashSet<>();
    private final Set<String> createdSlotIds = new HashSet<>();
    private final Set<String> createdNotificationIds = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        db = FirebaseFirestore.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        deleteTrackedNotifications();
        deleteTrackedSlots();
        deleteTrackedStudents();
        deleteTrackedCounselors();
    }

    @Test
    public void studentReminderWorker_createsNotification_and_marksFlag() throws Exception {
        String studentId = uniqueId("student");
        String counselorId = uniqueId("counselor");
        String slotId = uniqueId("slot");

        seedStudent(studentId, "Eman", "eman@test.com");
        seedCounselor(counselorId, "Dr. Sarah Khan", "sarah@test.com");
        seedBookedSlot(slotId, counselorId, studentId, System.currentTimeMillis() + 86400000L);

        Data inputData = new Data.Builder()
                .putString(ReminderSchedulerService.KEY_SLOT_ID, slotId)
                .putString(ReminderSchedulerService.KEY_STUDENT_ID, studentId)
                .putLong(ReminderSchedulerService.KEY_APPOINTMENT_START, System.currentTimeMillis() + 86400000L)
                .build();

        StudentReminderWorker worker =
                TestListenableWorkerBuilder.from(context, StudentReminderWorker.class)
                        .setInputData(inputData)
                        .build();

        ListenableWorker.Result result = worker.doWork();
        assertTrue(result instanceof ListenableWorker.Result.Success);

        QuerySnapshot notifications = Tasks.await(
                db.collection("notifications")
                        .whereEqualTo("recipientId", studentId)
                        .whereEqualTo("recipientRole", AppNotification.ROLE_STUDENT)
                        .whereEqualTo("type", AppNotification.TYPE_REMINDER_24H)
                        .get()
        );

        // Track created notifications for cleanup
        for (DocumentSnapshot doc : notifications.getDocuments()) {
            createdNotificationIds.add(doc.getId());
        }

        assertEquals(1, notifications.size());

        DocumentSnapshot slotSnapshot = Tasks.await(
                db.collection("appointment_slots").document(slotId).get()
        );

        Boolean reminderFlag = slotSnapshot.getBoolean("reminder24hSentStudent");
        assertTrue(Boolean.TRUE.equals(reminderFlag));
    }

    @Test
    public void counselorReminderWorker_createsNotification_and_marksFlag() throws Exception {
        String studentId = uniqueId("student");
        String counselorId = uniqueId("counselor");
        String slotId = uniqueId("slot");

        seedStudent(studentId, "Eman", "eman@test.com");
        seedCounselor(counselorId, "Dr. Sarah Khan", "sarah@test.com");
        seedBookedSlot(slotId, counselorId, studentId, System.currentTimeMillis() + 86400000L);

        Data inputData = new Data.Builder()
                .putString(ReminderSchedulerService.KEY_SLOT_ID, slotId)
                .putString(ReminderSchedulerService.KEY_COUNSELOR_ID, counselorId)
                .putLong(ReminderSchedulerService.KEY_APPOINTMENT_START, System.currentTimeMillis() + 86400000L)
                .build();

        CounselorReminderWorker worker =
                TestListenableWorkerBuilder.from(context, CounselorReminderWorker.class)
                        .setInputData(inputData)
                        .build();

        ListenableWorker.Result result = worker.doWork();
        assertTrue(result instanceof ListenableWorker.Result.Success);

        QuerySnapshot notifications = Tasks.await(
                db.collection("notifications")
                        .whereEqualTo("recipientId", counselorId)
                        .whereEqualTo("recipientRole", AppNotification.ROLE_COUNSELOR)
                        .whereEqualTo("type", AppNotification.TYPE_REMINDER_24H)
                        .get()
        );

        for (DocumentSnapshot doc : notifications.getDocuments()) {
            createdNotificationIds.add(doc.getId());
        }

        assertEquals(1, notifications.size());

        DocumentSnapshot slotSnapshot = Tasks.await(
                db.collection("appointment_slots").document(slotId).get()
        );

        Boolean reminderFlag = slotSnapshot.getBoolean("reminder24hSentCounselor");
        assertTrue(Boolean.TRUE.equals(reminderFlag));
    }

    @Test
    public void studentReminderWorker_doesNotCreateDuplicateNotification() throws Exception {
        String studentId = uniqueId("student");
        String counselorId = uniqueId("counselor");
        String slotId = uniqueId("slot");

        seedStudent(studentId, "Eman", "eman@test.com");
        seedCounselor(counselorId, "Dr. Sarah Khan", "sarah@test.com");
        seedBookedSlot(slotId, counselorId, studentId, System.currentTimeMillis() + 86400000L);

        Data inputData = new Data.Builder()
                .putString(ReminderSchedulerService.KEY_SLOT_ID, slotId)
                .putString(ReminderSchedulerService.KEY_STUDENT_ID, studentId)
                .putLong(ReminderSchedulerService.KEY_APPOINTMENT_START, System.currentTimeMillis() + 86400000L)
                .build();

        StudentReminderWorker worker1 =
                TestListenableWorkerBuilder.from(context, StudentReminderWorker.class)
                        .setInputData(inputData)
                        .build();

        StudentReminderWorker worker2 =
                TestListenableWorkerBuilder.from(context, StudentReminderWorker.class)
                        .setInputData(inputData)
                        .build();

        worker1.doWork();
        worker2.doWork();

        QuerySnapshot notifications = Tasks.await(
                db.collection("notifications")
                        .whereEqualTo("recipientId", studentId)
                        .whereEqualTo("recipientRole", AppNotification.ROLE_STUDENT)
                        .whereEqualTo("type", AppNotification.TYPE_REMINDER_24H)
                        .get()
        );

        for (DocumentSnapshot doc : notifications.getDocuments()) {
            createdNotificationIds.add(doc.getId());
        }

        assertEquals(1, notifications.size());
    }

    @Test
    public void studentReminderWorker_skipsAvailableSlot() throws Exception {
        String studentId = uniqueId("student");
        String counselorId = uniqueId("counselor");
        String slotId = uniqueId("slot");

        seedStudent(studentId, "Eman", "eman@test.com");
        seedCounselor(counselorId, "Dr. Sarah Khan", "sarah@test.com");
        seedAvailableSlot(slotId, counselorId, System.currentTimeMillis() + 86400000L);

        Data inputData = new Data.Builder()
                .putString(ReminderSchedulerService.KEY_SLOT_ID, slotId)
                .putString(ReminderSchedulerService.KEY_STUDENT_ID, studentId)
                .putLong(ReminderSchedulerService.KEY_APPOINTMENT_START, System.currentTimeMillis() + 86400000L)
                .build();

        StudentReminderWorker worker =
                TestListenableWorkerBuilder.from(context, StudentReminderWorker.class)
                        .setInputData(inputData)
                        .build();

        ListenableWorker.Result result = worker.doWork();
        assertTrue(result instanceof ListenableWorker.Result.Success);

        QuerySnapshot notifications = Tasks.await(
                db.collection("notifications")
                        .whereEqualTo("recipientId", studentId)
                        .whereEqualTo("type", AppNotification.TYPE_REMINDER_24H)
                        .get()
        );

        for (DocumentSnapshot doc : notifications.getDocuments()) {
            createdNotificationIds.add(doc.getId());
        }

        assertEquals(0, notifications.size());
    }

    private void seedStudent(String id, String name, String email) throws Exception {
        Map<String, Object> student = new HashMap<>();
        student.put("studentId", id);
        student.put("name", name);
        student.put("email", email);
        student.put("activeAppointmentId", null);

        Tasks.await(db.collection("students").document(id).set(student));
        createdStudentIds.add(id);
    }

    private void seedCounselor(String id, String name, String email) throws Exception {
        Map<String, Object> counselor = new HashMap<>();
        counselor.put("counselorId", id);
        counselor.put("name", name);
        counselor.put("email", email);
        counselor.put("specialization", "Stress Management");
        counselor.put("bio", "Test counselor");
        counselor.put("active", true);

        Tasks.await(db.collection("counselors").document(id).set(counselor));
        createdCounselorIds.add(id);
    }

    private void seedBookedSlot(String slotId,
                                String counselorId,
                                String studentId,
                                long appointmentStartMillis) throws Exception {
        Map<String, Object> slot = new HashMap<>();
        slot.put("slotId", slotId);
        slot.put("counselorId", counselorId);
        slot.put("studentId", studentId);
        slot.put("startTimeMillis", appointmentStartMillis);
        slot.put("endTimeMillis", appointmentStartMillis + (30L * 60L * 1000L));
        slot.put("status", "booked");
        slot.put("reminder24hSentStudent", false);
        slot.put("reminder24hSentCounselor", false);

        Tasks.await(db.collection("appointment_slots").document(slotId).set(slot));
        createdSlotIds.add(slotId);
    }

    private void seedAvailableSlot(String slotId,
                                   String counselorId,
                                   long appointmentStartMillis) throws Exception {
        Map<String, Object> slot = new HashMap<>();
        slot.put("slotId", slotId);
        slot.put("counselorId", counselorId);
        slot.put("studentId", null);
        slot.put("startTimeMillis", appointmentStartMillis);
        slot.put("endTimeMillis", appointmentStartMillis + (30L * 60L * 1000L));
        slot.put("status", "available");
        slot.put("reminder24hSentStudent", false);
        slot.put("reminder24hSentCounselor", false);

        Tasks.await(db.collection("appointment_slots").document(slotId).set(slot));
        createdSlotIds.add(slotId);
    }

    private void deleteTrackedStudents() throws Exception {
        for (String id : createdStudentIds) {
            Tasks.await(db.collection("students").document(id).delete());
        }
        createdStudentIds.clear();
    }

    private void deleteTrackedCounselors() throws Exception {
        for (String id : createdCounselorIds) {
            Tasks.await(db.collection("counselors").document(id).delete());
        }
        createdCounselorIds.clear();
    }

    private void deleteTrackedSlots() throws Exception {
        for (String id : createdSlotIds) {
            Tasks.await(db.collection("appointment_slots").document(id).delete());
        }
        createdSlotIds.clear();
    }

    private void deleteTrackedNotifications() throws Exception {
        for (String id : createdNotificationIds) {
            Tasks.await(db.collection("notifications").document(id).delete());
        }
        createdNotificationIds.clear();
    }

    private String uniqueId(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + Math.abs((int) (Math.random() * 100000));
    }
}