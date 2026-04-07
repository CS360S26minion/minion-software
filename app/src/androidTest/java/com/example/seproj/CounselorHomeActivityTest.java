package com.example.seproj;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.seproj.ui.common.NotificationsActivity;
import com.example.seproj.ui.counselor.CounselorAppointmentsActivity;
import com.example.seproj.ui.counselor.CounselorHomeActivity;
import com.example.seproj.ui.counselor.SetAvailabilityActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class CounselorHomeActivityTest {

    @Before
    public void setUp() {
        Intents.init();

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), CounselorHomeActivity.class);
        intent.putExtra("counselorId", "c1");
        intent.putExtra("counselorName", "Dr. Sarah");
        ActivityScenario.launch(intent);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void setAvailabilityLaunchesAvailabilityScreen() {
        onView(withId(R.id.btnSetAvailability)).perform(click());
        intended(hasComponent(SetAvailabilityActivity.class.getName()));
    }

    @Test
    public void viewAppointmentsLaunchesCounselorAppointments() {
        onView(withId(R.id.btnViewCounselorAppointments)).perform(click());
        intended(hasComponent(CounselorAppointmentsActivity.class.getName()));
    }

    @Test
    public void notificationsLaunchesNotificationsScreen() {
        onView(withId(R.id.btnCounselorNotifications)).perform(click());
        intended(hasComponent(NotificationsActivity.class.getName()));
    }
}