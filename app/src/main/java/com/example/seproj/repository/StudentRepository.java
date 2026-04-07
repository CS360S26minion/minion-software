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

    public void addStudent(Student student, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(student.getStudentId())
                .set(student)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

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
