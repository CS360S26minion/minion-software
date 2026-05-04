package com.example.seproj.model;

/**
 * Represents a bookable appointment slot for a counselor.
 * A slot may be available, booked, or cancelled.
 *
 * Outstanding issues:
 * - No-show state can be added later for US11.
 * - Intake form linkage can be added later for US13.
 */
public class AppointmentSlot {
    public static final String STATUS_AVAILABLE = "available";
    public static final String STATUS_BOOKED = "booked";
    public static final String STATUS_CANCELLED = "cancelled";
    public static final String STATUS_NO_SHOW = "no_show";

    private String slotId;
    private String counselorId;
    private String studentId;
    private long startTimeMillis;
    private long endTimeMillis;
    private String status;
    private String counselorName;
    private boolean reminder24hSentStudent;
    private boolean reminder24hSentCounselor;

    private boolean studentFeedbackSubmitted;
    private boolean counselorFeedbackSubmitted;

    // Required empty constructor for Firestore
    /**
     * Required empty constructor for Firestore deserialization.
     */
    public AppointmentSlot() {
    }

    public AppointmentSlot(String slotId, String counselorId, String studentId,
                           long startTimeMillis, long endTimeMillis,
                           String status, boolean reminder24hSentStudent,
                           boolean reminder24hSentCounselor) {
        this.slotId = slotId;
        this.counselorId = counselorId;
        this.studentId = studentId;
        this.startTimeMillis = startTimeMillis;
        this.endTimeMillis = endTimeMillis;
        this.status = status;
        this.reminder24hSentStudent = reminder24hSentStudent;
        this.reminder24hSentCounselor = reminder24hSentCounselor;
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
     * Checks whether student feedback submitted is true.
     *
     * @return true when student feedback submitted is true.
     */
    public boolean isStudentFeedbackSubmitted() {
        return studentFeedbackSubmitted;
    }

    /**
     * Updates the student feedback submitted.
     *
     * @param studentFeedbackSubmitted the student feedback submitted value.
     */
    public void setStudentFeedbackSubmitted(boolean studentFeedbackSubmitted) {
        this.studentFeedbackSubmitted = studentFeedbackSubmitted;
    }

    /**
     * Checks whether counselor feedback submitted is true.
     *
     * @return true when counselor feedback submitted is true.
     */
    public boolean isCounselorFeedbackSubmitted() {
        return counselorFeedbackSubmitted;
    }

    /**
     * Updates the counselor feedback submitted.
     *
     * @param counselorFeedbackSubmitted the counselor feedback submitted value.
     */
    public void setCounselorFeedbackSubmitted(boolean counselorFeedbackSubmitted) {
        this.counselorFeedbackSubmitted = counselorFeedbackSubmitted;
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
     * Returns the counselor ID.
     *
     * @return the current counselor ID value.
     */
    public String getCounselorId() {
        return counselorId;
    }

    /**
     * Updates the counselor ID.
     *
     * @param counselorId the counselor ID value.
     */
    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }

    /**
     * Returns the student ID.
     *
     * @return the current student ID value.
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * Updates the student ID.
     *
     * @param studentId the student ID value.
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    /**
     * Returns the start time millis.
     *
     * @return the current start time millis value.
     */
    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    /**
     * Updates the start time millis.
     *
     * @param startTimeMillis the start time millis value.
     */
    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    /**
     * Returns the end time millis.
     *
     * @return the current end time millis value.
     */
    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    /**
     * Updates the end time millis.
     *
     * @param endTimeMillis the end time millis value.
     */
    public void setEndTimeMillis(long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }

    /**
     * Returns the status.
     *
     * @return the current status value.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns the counselor name shown on student appointment cards.
     *
     * @return the current counselor name value.
     */
    public String getCounselorName() {
        return counselorName;
    }

    /**
     * Updates the counselor name shown on student appointment cards.
     *
     * @param counselorName the counselor display name value.
     */
    public void setCounselorName(String counselorName) {
        this.counselorName = counselorName;
    }

    /**
     * Updates the status.
     *
     * @param status the status value.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Checks whether reminder24h sent student is true.
     *
     * @return true when reminder24h sent student is true.
     */
    public boolean isReminder24hSentStudent() {
        return reminder24hSentStudent;
    }

    /**
     * Updates the reminder24h sent student.
     *
     * @param reminder24hSentStudent the reminder24h sent student value.
     */
    public void setReminder24hSentStudent(boolean reminder24hSentStudent) {
        this.reminder24hSentStudent = reminder24hSentStudent;
    }

    /**
     * Checks whether reminder24h sent counselor is true.
     *
     * @return true when reminder24h sent counselor is true.
     */
    public boolean isReminder24hSentCounselor() {
        return reminder24hSentCounselor;
    }

    /**
     * Updates the reminder24h sent counselor.
     *
     * @param reminder24hSentCounselor the reminder24h sent counselor value.
     */
    public void setReminder24hSentCounselor(boolean reminder24hSentCounselor) {
        this.reminder24hSentCounselor = reminder24hSentCounselor;
    }

    /**
     * Checks whether available is true.
     *
     * @return true when available is true.
     */
    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(status);
    }

    /**
     * Checks whether booked is true.
     *
     * @return true when booked is true.
     */
    public boolean isBooked() {
        return STATUS_BOOKED.equals(status);
    }

    /**
     * Checks whether cancelled is true.
     *
     * @return true when cancelled is true.
     */
    public boolean isCancelled() {
        return STATUS_CANCELLED.equals(status);
    }

    /**
     * Checks whether no show is true.
     *
     * @return true when no show is true.
     */
    public boolean isNoShow() {
        return STATUS_NO_SHOW.equals(status);
    }

    @Override
    /**
     * Builds a debug-friendly string representation of this AppointmentSlot.
     *
     * @return a string containing the main field values.
     */
    public String toString() {
        return "AppointmentSlot{" +
                "slotId='" + slotId + '\'' +
                ", counselorId='" + counselorId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", counselorName='" + counselorName + '\'' +
                ", startTimeMillis=" + startTimeMillis +
                ", endTimeMillis=" + endTimeMillis +
                ", status='" + status + '\'' +
                ", reminder24hSentStudent=" + reminder24hSentStudent +
                ", reminder24hSentCounselor=" + reminder24hSentCounselor +
                '}';
    }
}
