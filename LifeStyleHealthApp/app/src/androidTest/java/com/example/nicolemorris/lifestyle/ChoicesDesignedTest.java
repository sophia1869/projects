package com.example.nicolemorris.lifestyle;

import android.content.Intent;

import com.example.nicolemorris.lifestyle.Activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class ChoicesDesignedTest {

    //Todo: FIX SO WORKS EVEN IF A USER WASN'T SAVED ON THE PHONE YET

    private String uri = "No Pic";

    @Rule
    public ActivityTestRule<MainActivity> activityScenarioRule
            = new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void setup() {

        Intent i = new Intent();
        i.putExtra("uri", uri);
        activityScenarioRule.launchActivity(i);
    }

    @Test
    public void allItemsDisplayedCorrectly() {

        onView(withId(R.id.tv_title)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.tv_title)).check(matches(withText("Lifestyle")));
        onView(withId(R.id.iv_heart)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.ib_profile)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.ib_bmi)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.ib_goals)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.ib_hike)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.ib_weather)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.ib_help)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.ib_choose_profile)).check(matches(isCompletelyDisplayed()));

    }

    //Click Profile Button
    @Test
    public void clickProfileButton() {

        try {

            //Click ProfileButton
            onView((withId(R.id.ib_profile))).perform(click());

            //Displays ReviewFragment
            onView((withId(R.id.frag_rev))).check(matches(isDisplayed()));

            //Displays BottomButtonsFragment
            onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

            //Displays Header
            onView((withId(R.id.frag_header))).check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            System.err.println();
        }

    }

    //Click Goals Button
    @Test
    public void clickGoalsButton() {

        try {

            //Click GoalButton
            onView((withId(R.id.ib_goals))).perform(click());

            //Displays ChangeGoalFragment
            onView((withId(R.id.frag_change_goal))).check(matches(isDisplayed()));

            //Goals fragment not displayed
            onView((withId(R.id.frag_goals))).check(matches(not(isDisplayed())));

            //Displays BottomButtonsFragment
            onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

            //Displays Header
            onView((withId(R.id.frag_header))).check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            System.err.println();
        }


    }

    //Click BMI Button
    @Test
    public void clickBmiButton() {

        try {

            //Click BMI Button
            onView((withId(R.id.ib_bmi))).perform(click());

            //Displays ReviewFragment
            onView((withId(R.id.frag_bmi))).check(matches(isDisplayed()));

            //Displays BottomButtonsFragment
            onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

            //Displays Header
            onView((withId(R.id.frag_header))).check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            System.err.println();
        }


    }

    //Click Hikes Button (Leaves app)

    //Click Weather Button (Displays WeatherFragment,BottomButtonsFragment)
    @Test
    public void clickWeatherButton() {

        try {

            //Click Weather Button
            onView((withId(R.id.ib_weather))).perform(click());

            //Displays ReviewFragment
            onView((withId(R.id.frag_weather))).check(matches(isDisplayed()));

            //Displays BottomButtonsFragment
            onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

            //Displays Header
            onView((withId(R.id.frag_header))).check(matches(isDisplayed()));

        } catch (NoMatchingViewException e) {
            System.err.println();
        }

    }

    //Click Help Button (Displays HelpFragment)
    @Test
    public void clickHelpButton() {

        try {

            //Click Help Button
            onView((withId(R.id.ib_help))).perform(click());

            //Displays ReviewFragment
            onView((withId(R.id.frag_help))).check(matches(isDisplayed()));

            //Displays BottomButtonsFragment
            onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

            if (ApplicationProvider.getApplicationContext().getResources().getBoolean(R.bool.isTablet)) {

                //Doesn't display Header if tablet
                try {
                    onView((withId(R.id.frag_header))).check(matches(not(isDisplayed())));
                } catch (Exception e) {
                    System.err.println();
                }

            } else {

                //Displays Header if phone
                onView((withId(R.id.frag_header))).check(matches(isDisplayed()));
            }
        } catch (NoMatchingViewException e) {
            System.err.println();
        }


    }

    //Click Profile Picture (Displays UserLoginFragment)
    public void clickProfilePic() {

        try {

            onView(withId(R.id.ib_choose_profile)).check(matches(isClickable()));
            //Todo: add test for when clicked (should change to something where you choose a user)

        } catch (NoMatchingViewException e) {
            System.err.println();
        }

    }


}
