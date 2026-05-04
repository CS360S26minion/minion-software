package com.example.seproj.repository;

import androidx.annotation.NonNull;

import com.example.seproj.model.Student;
import com.example.seproj.utils.FirestoreCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository for student-related Firestore operations.
 * Handles CRUD-style access for the students collection.
 */
public class StudentRepository {

    private static final String COLLECTION_NAME = "students";
    private final FirebaseFirestore db;

    public StudentRepository() {
        this.db = FirebaseFirestore.getInstance();
    }
    /**

     * Adds a new student to Firestore.

     *

     * @param student  the {@link Student} object to be stored

     * @param callback callback invoked on success or failure

     */
    public void addStudent(Student student, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(student.getStudentId())
                .set(student)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves a student by their unique ID.

     *

     * @param studentId the unique identifier of the student

     * @param callback  callback returning the student or null if not found

     */
    public void getStudentById(String studentId, final FirestoreCallback<Student> callback) {
        db.collection(COLLECTION_NAME)
                .document(studentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Student student = documentSnapshot.toObject(Student.class);
                        callback.onSuccess(student);
                    } else {
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves a student by their email address.

     *

     * <p>This method is commonly used for login and authentication flows.</p>

     *

     * @param email    the student's email address

     * @param callback callback returning the student or null if not found

     */
    public void getStudentByEmail(String email, final FirestoreCallback<Student> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        Student student = doc.toObject(Student.class);
                        callback.onSuccess(student);
                    } else {
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Updates the active appointment ID for a student.

     *

     * <p>This is used to track the currently booked session for a student,

     * enabling features such as preventing double booking or displaying

     * active appointment details.</p>

     *

     * @param studentId     the unique ID of the student

     * @param appointmentId the ID of the active appointment

     * @param callback      callback invoked on success or failure

     */
    public void updateActiveAppointment(String studentId, String appointmentId,
                                        final FirestoreCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("activeAppointmentId", appointmentId);

        db.collection(COLLECTION_NAME)
                .document(studentId)
                .update(updates)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Clears the active appointment for a student.

     *

     * <p>This is typically called when an appointment is completed,

     * cancelled, or rescheduled.</p>

     *

     * @param studentId the unique ID of the student

     * @param callback  callback invoked on success or failure

     */
    public void clearActiveAppointment(String studentId, final FirestoreCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("activeAppointmentId", null);

        db.collection(COLLECTION_NAME)
                .document(studentId)
                .update(updates)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}
