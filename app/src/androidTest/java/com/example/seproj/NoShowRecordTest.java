package com.example.seproj;

import com.example.seproj.model.NoShowRecord;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoShowRecordTest {

    @Test
    public void noShowRecord_constructor_setsFieldsCorrectly() {
        NoShowRecord record = new NoShowRecord(
                "n1",
                "slot1",
                "student1",
                "counselor1",
                3000L
        );

        assertEquals("n1", record.getRecordId());
        assertEquals("slot1", record.getSlotId());
        assertEquals("student1", record.getStudentId());
        assertEquals("counselor1", record.getCounselorId());
        assertEquals(3000L, record.getMarkedAt());
    }
}
