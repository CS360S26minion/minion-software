package com.example.seproj.repository;


import com.example.seproj.model.AppNotification;
import com.example.seproj.utils.FirestoreCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for in-app notification storage and retrieval.
 * Used for confirmations, cancellations, and reminder events.
 */
public class NotificationRepository {

    private static final String COLLECTION_NAME = "notifications";
    private final FirebaseFirestore db;

    public NotificationRepository() {
        this.db = FirebaseFirestore.getInstance();
    }
    /**

     * Adds a new notification to Firestore.

     *

     * @param notification the {@link AppNotification} object to be stored

     * @param callback     callback invoked on success or failure

     */
    public void addNotification(AppNotification notification, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(notification.getNotificationId())
                .set(notification)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Retrieves all notifications for a specific recipient.

     *

     * <p>Notifications are ordered by creation time in descending order,

     * so the most recent notifications appear first.</p>

     *

     * @param recipientId the unique ID of the notification recipient

     * @param callback    callback returning a list of notifications

     */
    public void getNotificationsForRecipient(String recipientId,
                                             final FirestoreCallback<List<AppNotification>> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("recipientId", recipientId)
                .orderBy("createdAtMillis", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<AppNotification> notifications = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        AppNotification notification = doc.toObject(AppNotification.class);
                        if (notification != null) {
                            notifications.add(notification);
                        }
                    }
                    callback.onSuccess(notifications);
                })
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Marks a specific notification as read.

     *

     * @param notificationId the unique ID of the notification

     * @param callback       callback invoked on success or failure

     */
    public void markAsRead(String notificationId, final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .document(notificationId)
                .update("read", true)
                .addOnSuccessListener(unused -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }
    /**

     * Deletes all notifications for a specific recipient.

     *

     * <p>This operation uses a Firestore batch to efficiently remove

     * multiple documents in a single transaction.</p>

     *

     * @param recipientId the unique ID of the notification recipient

     * @param callback    callback invoked on success or failure

     */
    public void clearNotificationsForRecipient(String recipientId,
                                               final FirestoreCallback<Void> callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("recipientId", recipientId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    WriteBatch batch = db.batch();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        batch.delete(doc.getReference());
                    }
                    batch.commit()
                            .addOnSuccessListener(unused -> callback.onSuccess(null))
                            .addOnFailureListener(callback::onFailure);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
