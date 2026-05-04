package com.example.seproj.model;

import androidx.annotation.NonNull;

/**
 * Stores a cached AI-generated summary for a completed counseling appointment.
 * Used to avoid regenerating the same insight every time a student or counselor opens it.
 *
 * Outstanding issues:
 * - Versioning of prompts and summaries can be added if the AI format changes.
 */
public class AiInsightSummary {
    private String slotId;
    private String summary;
    private long generatedAt;

    /**
     * Required empty constructor for Firestore deserialization.
     */
    public AiInsightSummary() {
    }

    /**
     * @return
     * @throws CloneNotSupportedException
     */
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Creates a AiInsightSummary instance with its persisted field values.
     *
     * @param slotId the slot ID value.
     * @param summary the summary value.
     * @param generatedAt the generated at value.
     */
    public AiInsightSummary(String slotId, String summary, long generatedAt) {
        this.slotId = slotId;
        this.summary = summary;
        this.generatedAt = generatedAt;
    }

    /**
     * Returns the slot ID.
     *
     * @return the current slot ID value.
     */
    public String getSlotId() {
        return slotId;
    }

    /**
     * Updates the slot ID.
     *
     * @param slotId the slot ID value.
     */
    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    /**
     * Returns the summary.
     *
     * @return the current summary value.
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Updates the summary.
     *
     * @param summary the summary value.
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Returns the generated at.
     *
     * @return the current generated at value.
     */
    public long getGeneratedAt() {
        return generatedAt;
    }

    /**
     * Updates the generated at.
     *
     * @param generatedAt the generated at value.
     */
    public void setGeneratedAt(long generatedAt) {
        this.generatedAt = generatedAt;
    }
}



