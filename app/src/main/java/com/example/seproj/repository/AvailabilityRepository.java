package com.example.seproj.repository;

import com.example.seproj.model.Availability;
import com.example.seproj.utils.FirestoreCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for counselor availability operations.
 * Used to store and retrieve recurring availability blocks.
 */
public class AvailabilityRepository {

    private static final String COLLECTION_NAME = "availability";
    private final FirebaseFirestore db;

    public AvailabilityRepository() {
        this.db = FirebaseFirestore.getInstance();
    }
    /**

     * Adds a new availability record for a counselor.

     *

     * @param availability the {@link Availability} object to be stored

     * @param callback     callback invoked on success or failure

     */
    public void addAvailability(Availability availability, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(availability.getAvailabilityId())
                .set(availability)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Updates an existing availability record.

     *

     * @param availability the updated {@link Availability} object

     * @param callback     callback invoked on success or failure

     */
    public void updateAvailability(Availability availability, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(availability.getAvailabilityId())
                .set(availability)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves all availability records for a specific counselor.

     *

     * <p>This method is typically used to generate appointment slots

     * based on recurring availability patterns.</p>

     *

     * @param counselorId the unique ID of the counselor

     * @param callback    callback returning a list of availability records

     */
    public void getAvailabilityForCounselor(String counselorId,
                                            final FirestoreCallback<List<Availability>> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("counselorId", counselorId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Availability> availabilityList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Availability availability = doc.toObject(Availability.class);
                        if (availability != null) {
                            availabilityList.add(availability);
                        }
                    }
                    callback.onSuccess(availabilityList);
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Deletes an availability record from Firestore.

     *

     * @param availabilityId the unique ID of the availability record

     * @param callback       callback invoked on success or failure

     */
    public void deleteAvailability(String availabilityId, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(availabilityId)
                .delete()
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}