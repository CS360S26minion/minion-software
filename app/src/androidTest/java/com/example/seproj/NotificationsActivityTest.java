package com.example.seproj;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.seproj.ui.common.NotificationsActivity;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
//import static androidx.test.espresso.matcher.ViewMatchers.anyOf;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class NotificationsActivityTest {

    private FirebaseFirestore db;
    private final Set<String> createdNotificationIds = new HashSet<>();

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        for (String id : createdNotificationIds) {
            Tasks.await(db.collection("notifications").document(id).delete());
        }
        createdNotificationIds.clear();
    }

    @Test
    public void screenLaunchesSuccessfully() throws Exception {
        String notificationId = uniqueId("notification");

        Map<String, Object> notification = new HashMap<>();
        notification.put("notificationId", notificationId);
        notification.put("recipientId", "s1");
        notification.put("recipientRole", "student");
        notification.put("title", "Booking Confirmed");
        notification.put("message", "Your appointment is confirmed.");
        notification.put("appointmentId", "slot1");
        notification.put("createdAtMillis", System.currentTimeMillis());
        notification.put("type", "booking_confirmation");
        notification.put("read", false);

        Tasks.await(db.collection("notifications").document(notificationId).set(notification));
        createdNotificationIds.add(notificationId);

        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                NotificationsActivity.class
        );
        intent.putExtra("recipientId", "s1");
        intent.putExtra("recipientRole", "student");
        intent.putExtra("displayName", "Eman");

        ActivityScenario.launch(intent);

        onView(isRoot()).perform(TestUtils.waitFor(3000));

        onView(withId(R.id.tvNotificationsTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.rvNotifications)).check(matches(isDisplayed()));
    }

    private String uniqueId(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + Math.abs((int) (Math.random() * 100000));
    }
}