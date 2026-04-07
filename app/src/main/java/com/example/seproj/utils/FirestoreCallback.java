package com.example.seproj.utils;

/**
 * Generic callback interface for asynchronous Firestore operations.
 *
 * @param <T> the expected return type
 */
public interface FirestoreCallback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}