package com.example.maxim.shortstories2.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import com.example.maxim.shortstories2.MainActivity;
import com.example.maxim.shortstories2.R;
import com.example.maxim.shortstories2.WallsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class WallsActivityTest {
    @Rule
    public ActivityTestRule<WallsActivity> mActivityRule = new ActivityTestRule<>(
            WallsActivity.class);

    @Test
    public void buttonsTest() {
        onView(withId(R.id.button_walls_twitter))
                .perform(click());
        pressBack();

        onView(withId(R.id.button_login_twitter))
                .perform(click());
        pressBack();

        onView(withId(R.id.button_walls_vk))
                .perform(click());
        pressBack();

        onView(withId(R.id.button_login_vk))
                .perform(click());
        pressBack();
    }
}

