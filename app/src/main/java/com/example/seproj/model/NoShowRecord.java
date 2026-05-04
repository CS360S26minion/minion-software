package com.example.seproj.model;

/**
 * Represents a counselor-marked no-show event for a booked appointment.
 * The record preserves who missed which appointment and when it was marked.
 *
 * Outstanding issues:
 * - Reversal or dispute handling for no-show records is not implemented yet.
 */
public class NoShowRecord {
    private String recordId;
    private String slotId;
    private String studentId;
    private String counselorId;
    private long markedAt;

    /**
     * Required empty constructor for Firestore deserialization.
     */
    public NoShowRecord() {}

    public NoShowRecord(String recordId, String slotId, String studentId,
                        String counselorId, long markedAt) {
        this.recordId = recordId;
        this.slotId = slotId;
        this.studentId = studentId;
        this.counselorId = counselorId;
        this.markedAt = markedAt;
    }

    /**
     * Returns the record ID.
     *
     * @return the current record ID value.
     */
    public String getRecordId() { return recordId; }
    /**
     * Returns the slot ID.
     *
     * @return the current slot ID value.
     */
    public String getSlotId() { return slotId; }
    /**
     * Returns the student ID.
     *
     * @return the current student ID value.
     */
    public String getStudentId() { return studentId; }
    /**
     * Returns the counselor ID.
     *
     * @return the current counselor ID value.
     */
    public String getCounselorId() { return counselorId; }
    /**
     * Returns the marked at.
     *
     * @return the current marked at value.
     */
    public long getMarkedAt() { return markedAt; }

    /**
     * Updates the record ID.
     *
     * @param recordId the record ID value.
     */
    public void setRecordId(String recordId) { this.recordId = recordId; }
    /**
     * Updates the slot ID.
     *
     * @param slotId the slot ID value.
     */
    public void setSlotId(String slotId) { this.slotId = slotId; }
    /**
     * Updates the student ID.
     *
     * @param studentId the student ID value.
     */
    public void setStudentId(String studentId) { this.studentId = studentId; }
    /**
     * Updates the counselor ID.
     *
     * @param counselorId the counselor ID value.
     */
    public void setCounselorId(String counselorId) { this.counselorId = counselorId; }
    /**
     * Updates the marked at.
     *
     * @param markedAt the marked at value.
     */
    public void setMarkedAt(long markedAt) { this.markedAt = markedAt; }
}


