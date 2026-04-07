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

    public String getAvailabilityId() {
        return availabilityId;
    }

    public void setAvailabilityId(String availabilityId) {
        this.availabilityId = availabilityId;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
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