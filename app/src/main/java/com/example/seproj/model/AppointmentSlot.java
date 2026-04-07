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
    private boolean reminder24hSentStudent;
    private boolean reminder24hSentCounselor;

    // Required empty constructor for Firestore
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

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getCounselorId() {
        return counselorId;
    }

    public void setCounselorId(String counselorId) {
        this.counselorId = counselorId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    public void setEndTimeMillis(long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isReminder24hSentStudent() {
        return reminder24hSentStudent;
    }

    public void setReminder24hSentStudent(boolean reminder24hSentStudent) {
        this.reminder24hSentStudent = reminder24hSentStudent;
    }

    public boolean isReminder24hSentCounselor() {
        return reminder24hSentCounselor;
    }

    public void setReminder24hSentCounselor(boolean reminder24hSentCounselor) {
        this.reminder24hSentCounselor = reminder24hSentCounselor;
    }

    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(status);
    }

    public boolean isBooked() {
        return STATUS_BOOKED.equals(status);
    }

    public boolean isCancelled() {
        return STATUS_CANCELLED.equals(status);
    }

    public boolean isNoShow() {
        return STATUS_NO_SHOW.equals(status);
    }

    @Override
    public String toString() {
        return "AppointmentSlot{" +
                "slotId='" + slotId + '\'' +
                ", counselorId='" + counselorId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", startTimeMillis=" + startTimeMillis +
                ", endTimeMillis=" + endTimeMillis +
                ", status='" + status + '\'' +
                ", reminder24hSentStudent=" + reminder24hSentStudent +
                ", reminder24hSentCounselor=" + reminder24hSentCounselor +
                '}';
    }
}