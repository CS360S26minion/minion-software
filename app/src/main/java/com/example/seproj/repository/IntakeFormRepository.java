package com.example.seproj.repository;

import com.example.seproj.model.IntakeForm;
import com.example.seproj.utils.FirestoreCallback;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Provides Firestore access for intake forms linked to appointment slots.
 * Used by counselors and AI insight generation to load pre-session student context.
 *
 * Outstanding issues:
 * - Only one intake form per slot is expected and conflict handling is minimal.
 */
public class IntakeFormRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    /**

     * Submits an intake form to Firestore.

     *

     * @param form the {@link IntakeForm} object containing student responses

     * @return a {@link Task} representing the asynchronous write operation

     */
    public Task<Void> submitIntakeForm(IntakeForm form) {
        return db.collection("intake_forms")
                .document(form.getFormId())
                .set(form);
    }
    /**

     * Retrieves the intake form associated with a specific appointment slot.

     *

     * <p>This method assumes that at most one intake form exists per slot.

     * If no form is found, {@code null} is returned via the callback.</p>

     *

     * @param slotId  the unique ID of the appointment slot

     * @param callback callback returning the intake form or null if not found

     */
    public void getIntakeFormBySlotId(String slotId, FirestoreCallback<IntakeForm> callback) {
        db.collection("intake_forms")
                .whereEqualTo("slotId", slotId)
                .limit(1)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.isEmpty()) {
                        callback.onSuccess(null);
                    } else {
                        IntakeForm form = snapshot.getDocuments().get(0).toObject(IntakeForm.class);
                        callback.onSuccess(form);
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }
}


