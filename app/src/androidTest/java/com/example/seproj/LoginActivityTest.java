package com.example.seproj;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.seproj.ui.common.LoginActivity;
import com.example.seproj.ui.common.SignupActivity;

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
public class LoginActivityTest {

    @Before
    public void setUp() {
        Intents.init();
        ActivityScenario.launch(LoginActivity.class);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void clickingSignupButtonLaunchesSignupActivity() {
        onView(withId(R.id.btnGoToSignup)).perform(click());
        intended(hasComponent(SignupActivity.class.getName()));
    }
}