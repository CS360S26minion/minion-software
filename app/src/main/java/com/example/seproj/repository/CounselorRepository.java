package com.example.seproj.repository;


import com.example.seproj.model.Counselor;
import com.example.seproj.utils.FirestoreCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for counselor-related Firestore operations.
 * Supports retrieving counselor profiles for browsing and login.
 */
public class CounselorRepository {

    private static final String COLLECTION_NAME = "counselors";
    private final FirebaseFirestore db;

    public CounselorRepository() {
        this.db = FirebaseFirestore.getInstance();
    }
    /**

     * Adds a new counselor to Firestore.

     *

     * @param counselor the {@link Counselor} object to be added

     * @param callback  callback invoked on success or failure

     */
    public void addCounselor(Counselor counselor, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(counselor.getCounselorId())
                .set(counselor)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves all active counselors.

     *

     * <p>Only counselors with {@code active = true} are returned.

     * This method is used for student-facing counselor discovery screens.</p>

     *

     * @param callback callback returning a list of active counselors

     */
    public void getAllActiveCounselors(final FirestoreCallback<List<Counselor>> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("active", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Counselor> counselors = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
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

     * Retrieves a counselor by their unique ID.

     *

     * @param counselorId the unique identifier of the counselor

     * @param callback    callback returning the counselor or null if not found

     */
    public void getCounselorById(String counselorId, final FirestoreCallback<Counselor> callback) {
        db.collection(COLLECTION_NAME)
                .document(counselorId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Counselor counselor = documentSnapshot.toObject(Counselor.class);
                        callback.onSuccess(counselor);
                    } else {
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves a counselor by their email address.

     *

     * <p>This method is typically used for login or authentication flows.</p>

     *

     * @param email    the counselor's email address

     * @param callback callback returning the counselor or null if not found

     */
    public void getCounselorByEmail(String email, final FirestoreCallback<Counselor> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        Counselor counselor = doc.toObject(Counselor.class);
                        callback.onSuccess(counselor);
                    } else {
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Updates the bio/description of a counselor.

     *

     * @param counselorId the unique ID of the counselor

     * @param bio         the updated biography text

     * @return a {@link Task} representing the asynchronous update operation

     */
    public Task<Void> updateCounselorBio(String counselorId, String bio) {
        return db.collection(COLLECTION_NAME)
                .document(counselorId)
                .update("bio", bio);
    }
}
