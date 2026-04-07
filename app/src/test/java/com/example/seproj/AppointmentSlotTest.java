package com.example.seproj;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.example.seproj.model.AppointmentSlot;

public class AppointmentSlotTest {

    @Test
    public void appointmentSlotConstructorAndGetters_workCorrectly() {
        AppointmentSlot slot = new AppointmentSlot(
                "slot1",
                "c1",
                "s1",
                1000L,
                2000L,
                AppointmentSlot.STATUS_BOOKED,
                false,
                true
        );

        assertEquals("slot1", slot.getSlotId());
        assertEquals("c1", slot.getCounselorId());
        assertEquals("s1", slot.getStudentId());
        assertEquals(1000L, slot.getStartTimeMillis());
        assertEquals(2000L, slot.getEndTimeMillis());
        assertEquals(AppointmentSlot.STATUS_BOOKED, slot.getStatus());
        assertEquals(false, slot.isReminder24hSentStudent());
        assertEquals(true, slot.isReminder24hSentCounselor());
    }

    @Test
    public void appointmentSlotSetters_workCorrectly() {
        AppointmentSlot slot = new AppointmentSlot();

        slot.setSlotId("slot2");
        slot.setCounselorId("c2");
        slot.setStudentId("s2");
        slot.setStartTimeMillis(5000L);
        slot.setEndTimeMillis(7000L);
        slot.setStatus(AppointmentSlot.STATUS_AVAILABLE);
        slot.setReminder24hSentStudent(true);
        slot.setReminder24hSentCounselor(false);

        assertEquals("slot2", slot.getSlotId());
        assertEquals("c2", slot.getCounselorId());
        assertEquals("s2", slot.getStudentId());
        assertEquals(5000L, slot.getStartTimeMillis());
        assertEquals(7000L, slot.getEndTimeMillis());
        assertEquals(AppointmentSlot.STATUS_AVAILABLE, slot.getStatus());
        assertEquals(true, slot.isReminder24hSentStudent());
        assertEquals(false, slot.isReminder24hSentCounselor());
    }

    @Test
    public void appointmentSlotConstants_areCorrect() {
        assertEquals("available", AppointmentSlot.STATUS_AVAILABLE);
        assertEquals("booked", AppointmentSlot.STATUS_BOOKED);
        assertEquals("cancelled", AppointmentSlot.STATUS_CANCELLED);
    }

    @Test
    public void defaultConstructor_initializesEmptyObject() {
        AppointmentSlot slot = new AppointmentSlot();

        assertNull(slot.getSlotId());
        assertNull(slot.getCounselorId());
        assertNull(slot.getStudentId());
        assertEquals(0L, slot.getStartTimeMillis());
        assertEquals(0L, slot.getEndTimeMillis());
        assertNull(slot.getStatus());
    }
}