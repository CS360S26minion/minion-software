package com.example.seproj;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.seproj.ui.student.CounselorListActivity;
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
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CounselorListActivityTest {

    private FirebaseFirestore db;
    private final Set<String> createdCounselorIds = new HashSet<>();

    @Before
    public void setUp() {
        db = FirebaseFirestore.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        for (String id : createdCounselorIds) {
            Tasks.await(db.collection("counselors").document(id).delete());
        }
        createdCounselorIds.clear();
    }

    @Test
    public void screenLaunchesSuccessfully() throws Exception {
        String counselorId = uniqueId("counselor");

        Map<String, Object> counselor = new HashMap<>();
        counselor.put("counselorId", counselorId);
        counselor.put("name", "Dr. Test Counselor");
        counselor.put("email", "test@test.com");
        counselor.put("specialization", "Stress Management");
        counselor.put("bio", "Test bio");
        counselor.put("active", true);

        Tasks.await(db.collection("counselors").document(counselorId).set(counselor));
        createdCounselorIds.add(counselorId);

        Intent intent = new Intent(
                ApplicationProvider.getApplicationContext(),
                CounselorListActivity.class
        );
        intent.putExtra("studentId", "s1");
        intent.putExtra("studentName", "Eman");

        ActivityScenario.launch(intent);

        onView(isRoot()).perform(TestUtils.waitFor(3000));

        onView(withId(R.id.tvCounselorListTitle)).check(matches(isDisplayed()));
        onView(withText("Find your Guide")).check(matches(isDisplayed()));
        onView(withId(R.id.rvCounselors)).check(matches(isDisplayed()));
    }

    private String uniqueId(String prefix) {
        return prefix + "_" + System.currentTimeMillis() + "_" + Math.abs((int) (Math.random() * 100000));
    }
}