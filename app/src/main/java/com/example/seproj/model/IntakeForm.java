package com.example.seproj.model;

/**
 * Represents the pre-session intake answers submitted while booking an appointment.
 * Counselors and AI insight generation use this context to understand student goals before a session.
 *
 * Outstanding issues:
 * - Intake answers are currently free text and do not yet include validation categories.
 */
public class IntakeForm {
    private String formId;
    private String slotId;
    private String studentId;
    private String counselorId;
    private String mood;
    private String goals;
    private String concerns;
    private long submittedAt;

    private String studentName;
    private String counselorName;
    private long slotStartTimeMillis;
    private long slotEndTimeMillis;

    /**
     * Required empty constructor for Firestore deserialization.
     */
    public IntakeForm() {}

    public IntakeForm(String formId, String slotId, String studentId, String counselorId,
                      String mood, String goals, String concerns, long submittedAt) {
        this.formId = formId;
        this.slotId = slotId;
        this.studentId = studentId;
        this.counselorId = counselorId;
        this.mood = mood;
        this.goals = goals;
        this.concerns = concerns;
        this.submittedAt = submittedAt;
    }

    /**
     * Returns the form ID.
     *
     * @return the current form ID value.
     */
    public String getFormId() { return formId; }
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
     * Returns the mood.
     *
     * @return the current mood value.
     */
    public String getMood() { return mood; }
    /**
     * Returns the goals.
     *
     * @return the current goals value.
     */
    public String getGoals() { return goals; }
    /**
     * Returns the concerns.
     *
     * @return the current concerns value.
     */
    public String getConcerns() { return concerns; }
    /**
     * Returns the submitted at.
     *
     * @return the current submitted at value.
     */
    public long getSubmittedAt() { return submittedAt; }

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
     * Updates the form ID.
     *
     * @param formId the form ID value.
     */
    public void setFormId(String formId) { this.formId = formId; }
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
     * Updates the mood.
     *
     * @param mood the mood value.
     */
    public void setMood(String mood) { this.mood = mood; }
    /**
     * Updates the goals.
     *
     * @param goals the goals value.
     */
    public void setGoals(String goals) { this.goals = goals; }
    /**
     * Updates the concerns.
     *
     * @param concerns the concerns value.
     */
    public void setConcerns(String concerns) { this.concerns = concerns; }
    /**
     * Updates the submitted at.
     *
     * @param submittedAt the submitted at value.
     */
    public void setSubmittedAt(long submittedAt) { this.submittedAt = submittedAt; }
}


