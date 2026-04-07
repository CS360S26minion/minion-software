package com.example.seproj.repository;


import com.example.seproj.model.AppointmentSlot;
import com.example.seproj.utils.FirestoreCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for appointment slot operations.
 * Handles reading and writing appointment slot data from Firestore.
 */
public class AppointmentSlotRepository {

    private static final String COLLECTION_NAME = "appointment_slots";
    private final FirebaseFirestore db;

    public AppointmentSlotRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void addSlot(AppointmentSlot slot, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(slot.getSlotId())
                .set(slot)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void updateSlot(AppointmentSlot slot, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(slot.getSlotId())
                .set(slot)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void getSlotById(String slotId, final FirestoreCallback<AppointmentSlot> callback) {
        db.collection(COLLECTION_NAME)
                .document(slotId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        AppointmentSlot slot = documentSnapshot.toObject(AppointmentSlot.class);
                        callback.onSuccess(slot);
                    } else {
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getAvailableSlotsForCounselor(String counselorId,
                                              final FirestoreCallback<List<AppointmentSlot>> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("counselorId", counselorId)
                .whereEqualTo("status", AppointmentSlot.STATUS_AVAILABLE)
                .orderBy("startTimeMillis", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppointmentSlot> slots = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        AppointmentSlot slot = doc.toObject(AppointmentSlot.class);
                        if (slot != null) {
                            slots.add(slot);
                        }
                    }
                    callback.onSuccess(slots);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getAppointmentsForStudent(String studentId,
                                          final FirestoreCallback<List<AppointmentSlot>> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("studentId", studentId)
                .orderBy("startTimeMillis", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppointmentSlot> slots = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        AppointmentSlot slot = doc.toObject(AppointmentSlot.class);
                        if (slot != null) {
                            slots.add(slot);
                        }
                    }
                    callback.onSuccess(slots);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getBookedAppointmentsForCounselor(String counselorId,
                                                  final FirestoreCallback<List<AppointmentSlot>> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("counselorId", counselorId)
                .whereEqualTo("status", AppointmentSlot.STATUS_BOOKED)
                .orderBy("startTimeMillis", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppointmentSlot> slots = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        AppointmentSlot slot = doc.toObject(AppointmentSlot.class);
                        if (slot != null) {
                            slots.add(slot);
                        }
                    }
                    callback.onSuccess(slots);
                })
                .addOnFailureListener(callback::onFailure);
    }

    public void getSlotsInTimeRange(long startMillis, long endMillis,
                                    final FirestoreCallback<List<AppointmentSlot>> callback) {
        db.collection(COLLECTION_NAME)
                .whereGreaterThanOrEqualTo("startTimeMillis", startMillis)
                .whereLessThanOrEqualTo("startTimeMillis", endMillis)
                .orderBy("startTimeMillis", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppointmentSlot> slots = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        AppointmentSlot slot = doc.toObject(AppointmentSlot.class);
                        if (slot != null) {
                            slots.add(slot);
                        }
                    }
                    callback.onSuccess(slots);
                })
                .addOnFailureListener(callback::onFailure);
    }
}