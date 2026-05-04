package com.example.seproj.repository;


import androidx.annotation.NonNull;

import com.example.seproj.model.AppointmentSlot;
import com.example.seproj.utils.FirestoreCallback;
import com.google.android.gms.tasks.Task;
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
    /**

     * Adds a new appointment slot to Firestore.

     *

     * @param slot     the {@link AppointmentSlot} to be added

     * @param callback callback for success or failure of the operation

     */
    public void addSlot(AppointmentSlot slot, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(slot.getSlotId())
                .set(slot)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Marks an appointment slot as a "no-show".

     *

     * @param slotId the unique identifier of the slot

     * @return a {@link Task} representing the asynchronous update operation

     */
    public Task<Void> markSlotNoShow(String slotId) {
        return db.collection("appointment_slots")
                .document(slotId)
                .update("status", AppointmentSlot.STATUS_NO_SHOW);
    }
    /**

     * Updates an existing appointment slot in Firestore.

     *

     * @param slot     the updated {@link AppointmentSlot}

     * @param callback callback for success or failure

     */
    public void updateSlot(AppointmentSlot slot, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(slot.getSlotId())
                .set(slot)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves a specific appointment slot by its ID.

     *

     * @param slotId   the unique identifier of the slot

     * @param callback callback returning the slot or null if not found

     */
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

//    public void getAvailableSlotsForCounselor(String counselorId,
//                                              final FirestoreCallback<List<AppointmentSlot>> callback) {
//        db.collection(COLLECTION_NAME)
//                .whereEqualTo("counselorId", counselorId)
//                .whereEqualTo("status", AppointmentSlot.STATUS_AVAILABLE)
//                .orderBy("startTimeMillis", Query.Direction.ASCENDING)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<AppointmentSlot> slots = new ArrayList<>();
//                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
//                        AppointmentSlot slot = doc.toObject(AppointmentSlot.class);
//                        if (slot != null) {
//                            slots.add(slot);
//                        }
//                    }
//                    callback.onSuccess(slots);
//                })
//                .addOnFailureListener(callback::onFailure);
//    }
    /**
     * Retrieves all future available slots for a specific counselor.
     *
     * <p>Filters slots based on:</p>
     * <ul>
     *     <li>Matching counselor ID</li>
     *     <li>Status = AVAILABLE</li>
     *     <li>Start time greater than current time</li>
     * </ul>
     *
     * @param counselorId the counselor's unique ID
     * @param callback callback returning list of available slots
     */
    public void getAvailableSlotsForCounselor(String counselorId,
                                              final FirestoreCallback<List<AppointmentSlot>> callback) {
        long now = System.currentTimeMillis();

        db.collection(COLLECTION_NAME)
                .whereEqualTo("counselorId", counselorId)
                .whereEqualTo("status", AppointmentSlot.STATUS_AVAILABLE)
                .orderBy("startTimeMillis", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppointmentSlot> slots = new ArrayList<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        AppointmentSlot slot = doc.toObject(AppointmentSlot.class);

                        if (slot != null && slot.getStartTimeMillis() > now) {
                            slots.add(slot);
                        }
                    }

                    callback.onSuccess(slots);
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves all appointments for a given student.

     *

     * @param studentId the student's unique ID

     * @param callback  callback returning list of appointments

     */
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
    /**

     * Retrieves all booked appointments for a counselor.

     *

     * @param counselorId the counselor's unique ID

     * @param callback    callback returning booked slots

     */
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

    /**
     * @return
     * @throws CloneNotSupportedException
     */
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    /**

     * Retrieves all appointments associated with a counselor.

     *

     * @param counselorId the counselor's unique ID

     * @param callback    callback returning sorted list of appointments

     */
    public void getAppointmentsForCounselor(String counselorId,
                                            final FirestoreCallback<List<AppointmentSlot>> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("counselorId", counselorId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppointmentSlot> slots = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        AppointmentSlot slot = doc.toObject(AppointmentSlot.class);
                        if (slot != null) {
                            slots.add(slot);
                        }
                    }
                    slots.sort((first, second) ->
                            Long.compare(first.getStartTimeMillis(), second.getStartTimeMillis()));
                    callback.onSuccess(slots);
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves all appointment slots within a specified time range.

     *

     * @param startMillis start time in milliseconds

     * @param endMillis   end time in milliseconds

     * @param callback    callback returning filtered slot list

     */
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
