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

    public void addAvailability(Availability availability, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(availability.getAvailabilityId())
                .set(availability)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    public void updateAvailability(Availability availability, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(availability.getAvailabilityId())
                .set(availability)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

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

    public void deleteAvailability(String availabilityId, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(availabilityId)
                .delete()
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
}