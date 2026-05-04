package com.example.seproj.model;

/**
 * Represents a counselor's recurring weekly availability or blocked time.
 * This model is used to generate appointment slots.
 *
 * Outstanding issues:
 * - More advanced recurrence rules are not implemented yet.
 * - Time zone support can be improved later if needed.
 */
public class Availability {
    private String availabilityId;
    private String counselorId;
    private int dayOfWeek; // 1 = Sunday, 2 = Monday ... depending on your convention
    private String startTime; // Example: "09:00"
    private String endTime;   // Example: "17:00"
    private boolean blocked;

    // Required empty constructor for Firestore
    /**
     * Required empty constructor for Firestore deserialization.
     */
    public Availability() {
    }

    public Availability(String availabilityId, String counselorId, int dayOfWeek,
                        String startTime, String endTime, boolean blocked) {
        this.availabilityId = availabilityId;
        this.counselorId = counselorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.blocked = blocked;
    }

    /**
     * Returns the availability ID.
     *
     * @return the current availability ID value.
     */
    public String getAvailabilityId() {
        return availabilityId;
    }

    /**
     * Updates the availability ID.
     *
     * @param availabilityId the availability ID value.
     */
    public void setAvailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
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
     * Returns the day of week.
     *
     * @return the current day of week value.
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * Updates the day of week.
     *
     * @param dayOfWeek the day of week value.
     */
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * Returns the start time.
     *
     * @return the current start time value.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Updates the start time.
     *
     * @param startTime the start time value.
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the end time.
     *
     * @return the current end time value.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Updates the end time.
     *
     * @param endTime the end time value.
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * Checks whether blocked is true.
     *
     * @return true when blocked is true.
     */
    public boolean isBlocked() {
        return blocked;
    }

    /**
     * Updates the blocked.
     *
     * @param blocked the blocked value.
     */
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    /**
     * Builds a debug-friendly string representation of this Availability.
     *
     * @return a string containing the main field values.
     */
    public String toString() {
        return "Availability{" +
                "availabilityId='" + availabilityId + '\'' +
                ", counselorId='" + counselorId + '\'' +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", blocked=" + blocked +
                '}';
    }
}
