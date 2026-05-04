package com.example.seproj.repository;

import com.example.seproj.model.NoShowRecord;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Stores and retrieves no-show records for appointment attendance tracking.
 * Supports counselor attendance workflows and historical reporting.
 *
 * Outstanding issues:
 * - Aggregated no-show reporting currently lives outside this repository.
 */
public class NoShowRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**

     * Creates and stores a no-show record in Firestore.

     *

     * @param record the {@link NoShowRecord} representing a missed appointment

     * @return a {@link Task} representing the asynchronous write operation

     */
    public Task<Void> createNoShowRecord(NoShowRecord record) {
        return db.collection("no_show_records")
                .document(record.getRecordId())
                .set(record);
    }
}


