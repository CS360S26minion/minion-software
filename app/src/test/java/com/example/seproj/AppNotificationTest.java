package com.example.seproj;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.seproj.model.AppNotification;

import org.junit.Test;

public class AppNotificationTest {

    @Test
    public void constructor_setsFieldsCorrectly() {
        AppNotification notification = new AppNotification(
                "n1",
                "s1",
                AppNotification.ROLE_STUDENT,
                "Booking Confirmed",
                "Your appointment has been booked successfully.",
                "slot1",
                1712500000000L,
                AppNotification.TYPE_BOOKING_CONFIRMATION,
                false
        );

        assertEquals("n1", notification.getNotificationId());
        assertEquals("s1", notification.getRecipientId());
        assertEquals(AppNotification.ROLE_STUDENT, notification.getRecipientRole());
        assertEquals("Booking Confirmed", notification.getTitle());
        assertEquals("Your appointment has been booked successfully.", notification.getMessage());
        assertEquals("slot1", notification.getAppointmentId());
        assertEquals(1712500000000L, notification.getCreatedAtMillis());
        assertEquals(AppNotification.TYPE_BOOKING_CONFIRMATION, notification.getType());
        assertFalse(notification.isRead());
    }

    @Test
    public void setters_updateFieldsCorrectly() {
        AppNotification notification = new AppNotification();

        notification.setNotificationId("n2");
        notification.setRecipientId("c1");
        notification.setRecipientRole(AppNotification.ROLE_COUNSELOR);
        notification.setTitle("Reminder");
        notification.setMessage("You have an appointment tomorrow.");
        notification.setAppointmentId("slot2");
        notification.setCreatedAtMillis(1712600000000L);
        notification.setType(AppNotification.TYPE_REMINDER_24H);
        notification.setRead(true);

        assertEquals("n2", notification.getNotificationId());
        assertEquals("c1", notification.getRecipientId());
        assertEquals(AppNotification.ROLE_COUNSELOR, notification.getRecipientRole());
        assertEquals("Reminder", notification.getTitle());
        assertEquals("You have an appointment tomorrow.", notification.getMessage());
        assertEquals("slot2", notification.getAppointmentId());
        assertEquals(1712600000000L, notification.getCreatedAtMillis());
        assertEquals(AppNotification.TYPE_REMINDER_24H, notification.getType());
        assertTrue(notification.isRead());
    }
}