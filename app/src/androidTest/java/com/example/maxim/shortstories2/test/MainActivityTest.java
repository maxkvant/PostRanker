package com.example.maxim.shortstories2.test;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.view.Gravity;

import com.example.maxim.shortstories2.R;
import com.example.maxim.shortstories2.MainActivity;
import com.example.maxim.shortstories2.WallsActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.openDrawer;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void test() throws InterruptedException {
        Thread.currentThread().sleep(7000);

        onView(withId(R.id.refresh))
                .perform(swipeDown());

        final ViewInteraction onDrawer = onView(withId(R.id.activity_main));

        onDrawer.check(matches(isClosed()));
        onDrawer.perform(DrawerActions.open());
        onDrawer.check(matches(isOpen()));

        onView(withId(R.id.button_goto_walls))
                .perform(click());
        pressBack();

        onDrawer.check(matches(isClosed()));

        for (int i = 1; i <= 3; i++) {
            onDrawer.perform(DrawerActions.open());
            onData(anything()).inAdapterView(withId(R.id.left_drawer)).atPosition(i).perform(click());
            Thread.currentThread().sleep(1000);
        }

        onView(withId(R.id.spinner_nav)).perform(click());
    }
}
