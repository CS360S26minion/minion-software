package com.example.seproj.service;

import com.example.seproj.model.FeedbackForm;
import com.example.seproj.model.IntakeForm;
import com.example.seproj.model.NoShowRecord;
import com.example.seproj.repository.FeedbackRepository;
import com.example.seproj.repository.IntakeFormRepository;
import com.example.seproj.repository.NoShowRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Coordinates support features such as intake forms, feedback submission, and no-show marking.
 * Acts as an application service between UI screens and Firestore repositories.
 *
 * Outstanding issues:
 * - The service combines several workflows and may be split if the app grows.
 */
public class SupportFeatureService {

    private final IntakeFormRepository intakeRepo = new IntakeFormRepository();
    private final FeedbackRepository feedbackRepo = new FeedbackRepository();
    private final NoShowRepository noShowRepo = new NoShowRepository();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Task<Void> submitIntakeForm(String slotId,
                                       String studentId,
                                       String studentName,
                                       String counselorId,
                                       String counselorName,
                                       long slotStartTimeMillis,
                                       long slotEndTimeMillis,
                                       String mood,
                                       String goals,
                                       String concerns) {
        String formId = "intake_" + System.currentTimeMillis();

        IntakeForm form = new IntakeForm(
                formId,
                slotId,
                studentId,
                counselorId,
                mood,
                goals,
                concerns,
                System.currentTimeMillis()
        );

        form.setStudentName(studentName);
        form.setCounselorName(counselorName);
        form.setSlotStartTimeMillis(slotStartTimeMillis);
        form.setSlotEndTimeMillis(slotEndTimeMillis);

        return intakeRepo.submitIntakeForm(form);
    }

    public Task<Void> submitFeedback(String slotId,
                                     String studentId,
                                     String studentName,
                                     String counselorId,
                                     String counselorName,
                                     long slotStartTimeMillis,
                                     long slotEndTimeMillis,
                                     int rating,
                                     String comment,
                                     boolean isCounselorFeedback) {

        String feedbackId = "feedback_" + System.currentTimeMillis();

        FeedbackForm feedback = new FeedbackForm(
                feedbackId,
                slotId,
                counselorId,
                rating,
                comment,
                System.currentTimeMillis()
        );

        feedback.setStudentId(studentId);
        feedback.setStudentName(studentName);
        feedback.setCounselorName(counselorName);
        feedback.setSlotStartTimeMillis(slotStartTimeMillis);
        feedback.setSlotEndTimeMillis(slotEndTimeMillis);
        feedback.setCounselorFeedback(isCounselorFeedback);

        String submittedField = isCounselorFeedback
                ? "counselorFeedbackSubmitted"
                : "studentFeedbackSubmitted";

        return feedbackRepo.submitFeedback(feedback)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return db.collection("appointment_slots")
                            .document(slotId)
                            .update(submittedField, true);
                });
    }

    public Task<Void> markNoShow(String slotId, String studentId, String counselorId) {
        String recordId = "noshow_" + System.currentTimeMillis();

        NoShowRecord record = new NoShowRecord(
                recordId,
                slotId,
                studentId,
                counselorId,
                System.currentTimeMillis()
        );

        return noShowRepo.createNoShowRecord(record)
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return com.google.firebase.firestore.FirebaseFirestore.getInstance()
                            .collection("appointment_slots")
                            .document(slotId)
                            .update("status", com.example.seproj.model.AppointmentSlot.STATUS_NO_SHOW);
                });
    }
}


