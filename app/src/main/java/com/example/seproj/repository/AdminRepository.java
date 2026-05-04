package com.example.seproj.repository;

import com.example.seproj.model.AppointmentSlot;
import com.example.seproj.model.Counselor;
import com.example.seproj.utils.FirestoreCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides Firestore access for admin account lookup and authentication support.
 * Keeps admin data access separate from activity code.
 *
 * Outstanding issues:
 * - Authentication is simple and can be replaced by Firebase Auth role claims later.
 */
public class AdminRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     *
     */
    public AdminRepository() {
        super();
    }
    /**

     * Fetches all appointment slots from Firestore.

     *

     * <p>This method retrieves all documents from the "appointment_slots"

     * collection and maps them into {@link AppointmentSlot} objects.</p>

     *

     * @param callback callback interface used to return the result asynchronously

     *                 or handle errors

     */
    public void getAllAppointmentSlots(FirestoreCallback<List<AppointmentSlot>> callback) {
        db.collection("appointment_slots")
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<AppointmentSlot> slots = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
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

     * Fetches all counselors from Firestore.

     *

     * <p>This method retrieves all documents from the "counselors" collection

     * and converts them into {@link Counselor} objects.</p>

     *

     * @param callback callback interface used to return the list of counselors

     *                 or handle any retrieval errors

     */
    public void getAllCounselors(FirestoreCallback<List<Counselor>> callback) {
        db.collection("counselors")
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Counselor> counselors = new ArrayList<>();

                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        Counselor counselor = doc.toObject(Counselor.class);
                        if (counselor != null) {
                            counselors.add(counselor);
                        }
                    }

                    callback.onSuccess(counselors);
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Adds a new counselor or updates an existing counselor in Firestore.

     *

     * <p>If a counselor with the same ID already exists, it will be overwritten.</p>

     *

     * @param counselor the {@link Counselor} object containing updated data

     * @return a {@link Task} representing the asynchronous Firestore operation

     */
    public Task<Void> addOrUpdateCounselor(Counselor counselor) {
        return db.collection("counselors")
                .document(counselor.getCounselorId())
                .set(counselor);
    }
    /**

     * Deletes a counselor from Firestore using their unique ID.

     *

     * @param counselorId the unique identifier of the counselor to be removed

     * @return a {@link Task} representing the asynchronous delete operation

     */
    public Task<Void> deleteCounselor(String counselorId) {
        return db.collection("counselors")
                .document(counselorId)
                .delete();
    }
}


