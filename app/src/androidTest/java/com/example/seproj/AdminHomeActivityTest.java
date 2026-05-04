package com.example.seproj;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.seproj.ui.admin.AdminAnalyticsActivity;
import com.example.seproj.ui.admin.AdminCounselorManagementActivity;
import com.example.seproj.ui.admin.AdminHomeActivity;

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
public class AdminHomeActivityTest {

    @Before
    public void setUp() {
        Intents.init();

        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AdminHomeActivity.class);
        ActivityScenario.launch(intent);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void analyticsButtonLaunchesAdminAnalytics() {
        onView(withId(R.id.btnAnalytics)).perform(click());
        intended(hasComponent(AdminAnalyticsActivity.class.getName()));
    }

    @Test
    public void manageCounselorsButtonLaunchesCounselorManagement() {
        onView(withId(R.id.btnManageCounselors)).perform(click());
        intended(hasComponent(AdminCounselorManagementActivity.class.getName()));
    }
}
