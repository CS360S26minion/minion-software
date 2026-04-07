package com.example.seproj;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.seproj.ui.student.AvailableSlotsActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class AvailableSlotsActivityTest {

    @Test
    public void screenLaunchesSuccessfully() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), AvailableSlotsActivity.class);
        intent.putExtra("studentId", "s1");
        intent.putExtra("studentName", "Eman");
        intent.putExtra("counselorId", "c1");
        intent.putExtra("counselorName", "Dr. Sarah");
        ActivityScenario.launch(intent);

        onView(withId(R.id.tvSlotsTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.rvSlots)).check(matches(isDisplayed()));
    }
}