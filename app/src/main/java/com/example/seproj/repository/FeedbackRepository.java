package com.example.seproj.repository;

import com.example.seproj.model.FeedbackForm;
import com.example.seproj.utils.FirestoreCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides Firestore queries for appointment feedback and counselor rating data.
 * Centralizes feedback retrieval for admin review, counselor lists, and AI insight generation.
 *
 * Outstanding issues:
 * - Filtering and pagination can be added when feedback volume grows.
 */
public class FeedbackRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**

     * Submits a feedback form to Firestore.

     *

     * @param feedback the {@link FeedbackForm} object containing feedback details

     * @return a {@link Task} representing the asynchronous write operation

     */
    public Task<Void> submitFeedback(FeedbackForm feedback) {
        return db.collection("feedback_forms")
                .document(feedback.getFeedbackId())
                .set(feedback);
    }
    /**

     * Retrieves all student-submitted feedback for a given student.

     *

     * <p>This excludes counselor feedback and is typically used for

     * displaying a student's feedback history.</p>

     *

     * @param studentId the unique ID of the student

     * @param callback  callback returning a list of feedback forms

     */
    public void getStudentFeedbackForStudent(String studentId,
                                             FirestoreCallback<List<FeedbackForm>> callback) {
        db.collection("feedback_forms")
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("counselorFeedback", false)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<FeedbackForm> feedbackList = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        FeedbackForm feedback = doc.toObject(FeedbackForm.class);
                        if (feedback != null) {
                            feedbackList.add(feedback);
                        }
                    }

                    callback.onSuccess(feedbackList);
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves all feedback associated with a specific counselor.

     *

     * <p>This method is primarily used by admin dashboards to review

     * counselor performance and feedback trends.</p>

     *

     * @param counselorId the unique ID of the counselor

     * @param callback    callback returning a list of feedback forms

     */
    public void getFeedbackForCounselor(String counselorId,
                                        FirestoreCallback<List<FeedbackForm>> callback) {
        db.collection("feedback_forms")
                .whereEqualTo("counselorId", counselorId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<FeedbackForm> feedbackList = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        FeedbackForm feedback = doc.toObject(FeedbackForm.class);
                        if (feedback != null) {
                            feedbackList.add(feedback);
                        }
                    }

                    callback.onSuccess(feedbackList);
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**
     * Retrieves all feedback associated with a specific appointment slot.
     *
     * <p>This is useful for:</p>
     * <ul>
     *     <li>Viewing feedback tied to a session</li>
     *     <li>Generating AI summaries for a particular appointment</li>
     * </ul>
     *
     * @param slotId the unique ID of the appointment slot
     * @param callback callback returning a list of feedback forms
     */
    public void getFeedbackForSlot(String slotId,
                                   FirestoreCallback<List<FeedbackForm>> callback) {
        db.collection("feedback_forms")
                .whereEqualTo("slotId", slotId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<FeedbackForm> feedbackList = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        FeedbackForm feedback = doc.toObject(FeedbackForm.class);
                        if (feedback != null) {
                            feedbackList.add(feedback);
                        }
                    }

                    callback.onSuccess(feedbackList);
                })
                .addOnFailureListener(callback::onFailure);
    }
}



