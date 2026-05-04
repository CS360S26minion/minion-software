package com.example.seproj;

import com.example.seproj.model.FeedbackForm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FeedbackFormTest {

    @Test
    public void feedbackForm_constructor_setsFieldsCorrectly() {
        FeedbackForm feedback = new FeedbackForm(
                "fb1",
                "slot1",
                "counselor1",
                5,
                "Very helpful",
                2000L
        );

        assertEquals("fb1", feedback.getFeedbackId());
        assertEquals("slot1", feedback.getSlotId());
        assertEquals("counselor1", feedback.getCounselorId());
        assertEquals(5, feedback.getRating());
        assertEquals("Very helpful", feedback.getComment());
        assertEquals(2000L, feedback.getSubmittedAt());
    }
}
