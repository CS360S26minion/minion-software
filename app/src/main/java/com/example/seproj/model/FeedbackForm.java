package com.example.seproj.model;

/**
 * Represents feedback submitted after an attended counseling appointment.
 * Feedback can come from either the student or counselor and is later used for ratings, admin review, and AI insights.
 *
 * Outstanding issues:
 * - Separate structured feedback categories can be added beyond rating and comment.
 */
public class FeedbackForm {
    private String feedbackId;
    private String slotId;
    private String counselorId;
    private int rating;
    private String comment;
    private long submittedAt;

    private String studentId;
    private String studentName;
    private String counselorName;
    private long slotStartTimeMillis;
    private long slotEndTimeMillis;
    private boolean counselorFeedback;

    /**
     * Required empty constructor for Firestore deserialization.
     */
    public FeedbackForm() {}

    public FeedbackForm(String feedbackId, String slotId, String counselorId,
                        int rating, String comment, long submittedAt) {
        this.feedbackId = feedbackId;
        this.slotId = slotId;
        this.counselorId = counselorId;
        this.rating = rating;
        this.comment = comment;
        this.submittedAt = submittedAt;

    }

    /**
     * Returns the feedback ID.
     *
     * @return the current feedback ID value.
     */
    public String getFeedbackId() { return feedbackId; }
    /**
     * Returns the slot ID.
     *
     * @return the current slot ID value.
     */
    public String getSlotId() { return slotId; }
    /**
     * Returns the counselor ID.
     *
     * @return the current counselor ID value.
     */
    public String getCounselorId() { return counselorId; }
    /**
     * Returns the rating.
     *
     * @return the current rating value.
     */
    public int getRating() { return rating; }
    /**
     * Returns the comment.
     *
     * @return the current comment value.
     */
    public String getComment() { return comment; }
    /**
     * Returns the submitted at.
     *
     * @return the current submitted at value.
     */
    public long getSubmittedAt() { return submittedAt; }

    /**
     * Returns the student ID.
     *
     * @return the current student ID value.
     */
    public String getStudentId() { return studentId; }
    /**
     * Updates the student ID.
     *
     * @param studentId the student ID value.
     */
    public void setStudentId(String studentId) { this.studentId = studentId; }

    /**
     * Returns the student name.
     *
     * @return the current student name value.
     */
    public String getStudentName() { return studentName; }
    /**
     * Updates the student name.
     *
     * @param studentName the student name value.
     */
    public void setStudentName(String studentName) { this.studentName = studentName; }

    /**
     * Returns the counselor name.
     *
     * @return the current counselor name value.
     */
    public String getCounselorName() { return counselorName; }
    /**
     * Updates the counselor name.
     *
     * @param counselorName the counselor name value.
     */
    public void setCounselorName(String counselorName) { this.counselorName = counselorName; }

    /**
     * Returns the slot start time millis.
     *
     * @return the current slot start time millis value.
     */
    public long getSlotStartTimeMillis() { return slotStartTimeMillis; }
    /**
     * Updates the slot start time millis.
     *
     * @param slotStartTimeMillis the slot start time millis value.
     */
    public void setSlotStartTimeMillis(long slotStartTimeMillis) { this.slotStartTimeMillis = slotStartTimeMillis; }

    /**
     * Returns the slot end time millis.
     *
     * @return the current slot end time millis value.
     */
    public long getSlotEndTimeMillis() { return slotEndTimeMillis; }
    /**
     * Updates the slot end time millis.
     *
     * @param slotEndTimeMillis the slot end time millis value.
     */
    public void setSlotEndTimeMillis(long slotEndTimeMillis) { this.slotEndTimeMillis = slotEndTimeMillis; }

    /**
     * Checks whether counselor feedback is true.
     *
     * @return true when counselor feedback is true.
     */
    public boolean isCounselorFeedback() { return counselorFeedback; }
    /**
     * Updates the counselor feedback.
     *
     * @param counselorFeedback the counselor feedback value.
     */
    public void setCounselorFeedback(boolean counselorFeedback) { this.counselorFeedback = counselorFeedback; }

    /**
     * Updates the feedback ID.
     *
     * @param feedbackId the feedback ID value.
     */
    public void setFeedbackId(String feedbackId) { this.feedbackId = feedbackId; }
    /**
     * Updates the slot ID.
     *
     * @param slotId the slot ID value.
     */
    public void setSlotId(String slotId) { this.slotId = slotId; }
    /**
     * Updates the counselor ID.
     *
     * @param counselorId the counselor ID value.
     */
    public void setCounselorId(String counselorId) { this.counselorId = counselorId; }
    /**
     * Updates the rating.
     *
     * @param rating the rating value.
     */
    public void setRating(int rating) { this.rating = rating; }
    /**
     * Updates the comment.
     *
     * @param comment the comment value.
     */
    public void setComment(String comment) { this.comment = comment; }
    /**
     * Updates the submitted at.
     *
     * @param submittedAt the submitted at value.
     */
    public void setSubmittedAt(long submittedAt) { this.submittedAt = submittedAt; }
}


