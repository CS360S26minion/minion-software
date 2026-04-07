package com.example.seproj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.seproj.model.Availability;

import org.junit.Test;

public class AvailabilityTest {

    @Test
    public void constructor_setsFieldsCorrectly() {
        Availability availability = new Availability(
                "a1",
                "c1",
                2,
                "09:00",
                "17:00",
                false
        );

        assertEquals("a1", availability.getAvailabilityId());
        assertEquals("c1", availability.getCounselorId());
        assertEquals(2, availability.getDayOfWeek());
        assertEquals("09:00", availability.getStartTime());
        assertEquals("17:00", availability.getEndTime());
        assertFalse(availability.isBlocked());
    }

    @Test
    public void setters_updateFieldsCorrectly() {
        Availability availability = new Availability();

        availability.setAvailabilityId("a2");
        availability.setCounselorId("c2");
        availability.setDayOfWeek(5);
        availability.setStartTime("10:00");
        availability.setEndTime("15:00");
        availability.setBlocked(true);

        assertEquals("a2", availability.getAvailabilityId());
        assertEquals("c2", availability.getCounselorId());
        assertEquals(5, availability.getDayOfWeek());
        assertEquals("10:00", availability.getStartTime());
        assertEquals("15:00", availability.getEndTime());
        assertTrue(availability.isBlocked());
    }
}