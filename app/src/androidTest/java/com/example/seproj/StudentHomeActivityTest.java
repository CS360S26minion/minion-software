package com.example.seproj;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.seproj.ui.common.NotificationsActivity;
import com.example.seproj.ui.student.CounselorListActivity;
import com.example.seproj.ui.student.StudentAppointmentsActivity;
import com.example.seproj.ui.student.StudentHomeActivity;

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
public class StudentHomeActivityTest {

    @Before
    public void setUp() {
        Intents.init();

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), StudentHomeActivity.class);
        intent.putExtra("studentId", "s1");
        intent.putExtra("studentName", "Eman");
        ActivityScenario.launch(intent);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void browseCounselorsLaunchesCounselorList() {
        onView(withId(R.id.btnBrowseCounselors)).perform(click());
        intended(hasComponent(CounselorListActivity.class.getName()));
    }

    @Test
    public void myAppointmentsLaunchesStudentAppointments() {
        onView(withId(R.id.btnMyAppointments)).perform(click());
        intended(hasComponent(StudentAppointmentsActivity.class.getName()));
    }

    @Test
    public void notificationsLaunchesNotificationsScreen() {
        onView(withId(R.id.btnStudentNotifications)).perform(click());
        intended(hasComponent(NotificationsActivity.class.getName()));
    }
}