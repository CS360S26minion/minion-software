package com.example.seproj;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.seproj.ui.counselor.CounselorAppointmentsActivity;
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
public class CounselorAppointmentsActivityTest {

    private FirebaseFirestore db;
    private final Set<String> createdSlotIds = new HashSet<>();

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        for (String id : createdSlotIds) {
            Tasks.await(db.collection("appointment_slots").document(id).delete());
        }
        createdSlotIds.clear();
    }

    @Test
    public void screenLaunchesSuccessfully() throws Exception {
        String slotId = uniqueId("slot");

        Map<String, Object> slot = new HashMap<>();
        slot.put("slotId", slotId);
        slot.put("counselorId", "c1");
        slot.put("studentId", "s1");
        slot.put("startTimeMillis", System.currentTimeMillis() + 3600000L);
        slot.put("endTimeMillis", System.currentTimeMillis() + 5400000L);
        slot.put("status", "booked");
        slot.put("reminder24hSentStudent", false);
        slot.put("reminder24hSentCounselor", false);

        Tasks.await(db.collection("appointment_slots").document(slotId).set(slot));
        createdSlotIds.add(slotId);

        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                CounselorAppointmentsActivity.class
        );
        intent.putExtra("counselorId", "c1");
        intent.putExtra("counselorName", "Dr. Sarah");

        ActivityScenario.launch(intent);

        onView(isRoot()).perform(TestUtils.waitFor(3000));

        onView(withId(R.id.tvCounselorAppointmentsTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.rvCounselorAppointments)).check(matches(isDisplayed()));
    }

    private String uniqueId(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + Math.abs((int) (Math.random() * 100000));
    }
}