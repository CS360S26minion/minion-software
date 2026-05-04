package com.example.seproj;

import com.example.seproj.model.IntakeForm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IntakeFormTest {

    @Test
    public void intakeForm_constructor_setsFieldsCorrectly() {
        IntakeForm form = new IntakeForm(
                "f1",
                "slot1",
                "student1",
                "counselor1",
                "Stressed",
                "Discuss exams",
                "Anxiety",
                1000L
        );

        assertEquals("f1", form.getFormId());
        assertEquals("slot1", form.getSlotId());
        assertEquals("student1", form.getStudentId());
        assertEquals("counselor1", form.getCounselorId());
        assertEquals("Stressed", form.getMood());
        assertEquals("Discuss exams", form.getGoals());
        assertEquals("Anxiety", form.getConcerns());
        assertEquals(1000L, form.getSubmittedAt());
    }
}
