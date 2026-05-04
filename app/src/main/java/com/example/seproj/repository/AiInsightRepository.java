package com.example.seproj.repository;

import com.example.seproj.model.AiInsightSummary;
import com.example.seproj.utils.FirestoreCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Persists and retrieves cached AI insight summaries from Firestore.
 * Used by the AI insight service to reuse summaries for completed appointments.
 *
 * Outstanding issues:
 * - Cache invalidation is not versioned by prompt or model.
 */
public class AiInsightRepository {
    private static final String COLLECTION_NAME = "ai_insight_summaries";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**

     * Retrieves an AI insight summary for a given appointment slot ID.

     *

     * <p>If a summary exists in Firestore, it is returned via the callback.

     * Otherwise, {@code null} is returned to indicate no cached summary exists.</p>

     *

     * @param slotId   unique identifier of the appointment slot

     * @param callback callback used to return the {@link AiInsightSummary}

     *                 or handle errors

     */
    public void getSummaryBySlotId(String slotId, FirestoreCallback<AiInsightSummary> callback) {
        db.collection(COLLECTION_NAME)
                .document(slotId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        callback.onSuccess(documentSnapshot.toObject(AiInsightSummary.class));
                    } else {
                        callback.onSuccess(null);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Saves or updates an AI insight summary in Firestore.

     *

     * <p>If a summary for the given slot ID already exists, it will be overwritten.</p>

     *

     * @param summary the {@link AiInsightSummary} object containing generated insights

     * @return a {@link Task} representing the asynchronous Firestore write operation

     */
    public Task<Void> saveSummary(AiInsightSummary summary) {
        return db.collection(COLLECTION_NAME)
                .document(summary.getSlotId())
                .set(summary);
    }
}



