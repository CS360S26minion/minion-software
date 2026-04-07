package com.example.seproj.repository;


import com.example.seproj.model.Counselor;
import com.example.seproj.utils.FirestoreCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public void addCounselor(Counselor counselor, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(counselor.getCounselorId())
                .set(counselor)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

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
}
