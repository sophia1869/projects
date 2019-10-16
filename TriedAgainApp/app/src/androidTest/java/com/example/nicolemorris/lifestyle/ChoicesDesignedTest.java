package com.example.nicolemorris.lifestyle;

import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class ChoicesDesignedTest {

    private String uri = "No Pic";

    @Rule
    public ActivityTestRule<HomeActivity> activityScenarioRule
            = new ActivityTestRule<>(HomeActivity.class,false,false);

    @Before
    public void setup(){
        Intent i = new Intent();
        i.putExtra("uri", uri);
        activityScenarioRule.launchActivity(i);
    }

    //Click Profile Button
    @Test
    public void clickProfileButton(){

        //Click ProfileButton
        onView((withId(R.id.ib_profile))).perform(click());

        //Displays ReviewFragment
        onView((withId(R.id.frag_rev))).check(matches(isDisplayed()));

        //Displays BottomButtonsFragment
        onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

        //Displays Header
        onView((withId(R.id.frag_header))).check(matches(isDisplayed()));
    }

    //Click Goals Button
    @Test
    public void clickGoalsButton(){

        //Click GoalButton
        onView((withId(R.id.ib_goals))).perform(click());

        //Displays ChangeGoalFragment
        onView((withId(R.id.frag_change_goal))).check(matches(isDisplayed()));

        //Goals fragment not displayed
        try {
            onView((withId(R.id.frag_goals))).check(matches(not(isDisplayed())));
        } catch (Exception exception ) {
            System.out.print(exception);
        }

        //Displays BottomButtonsFragment
        onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

        //Displays Header
        onView((withId(R.id.frag_header))).check(matches(isDisplayed()));

    }

    //Click BMI Button
    @Test
    public void clickBmiButton(){

        //Click BMI Button
        onView((withId(R.id.ib_bmi))).perform(click());

        //Displays ReviewFragment
        onView((withId(R.id.frag_bmi))).check(matches(isDisplayed()));

        //Displays BottomButtonsFragment
        onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

        //Displays Header
        onView((withId(R.id.frag_header))).check(matches(isDisplayed()));

    }

    //Click Hikes Button (Leaves app)

    //Click Weather Button (Displays WeatherFragment,BottomButtonsFragment)
    @Test
    public void clickWeatherButton(){

        //Click BMI Button
        onView((withId(R.id.ib_weather))).perform(click());

        //Displays ReviewFragment
        onView((withId(R.id.frag_weather))).check(matches(isDisplayed()));

        //Displays BottomButtonsFragment
        onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

        //Displays Header
        onView((withId(R.id.frag_header))).check(matches(isDisplayed()));

    }

    //Click Help Button (Displays HelpFragment)
    @Test
    public void clickHelpButton(){

        //Click BMI Button
        onView((withId(R.id.ib_help))).perform(click());

        //Displays ReviewFragment
        onView((withId(R.id.frag_help))).check(matches(isDisplayed()));

        //Displays BottomButtonsFragment
        onView((withId(R.id.bot_buttons))).check(matches(isDisplayed()));

        if(ApplicationProvider.getApplicationContext().getResources().getBoolean(R.bool.isTablet)){

            //Doesn't display Header if tablet
            try {
                onView((withId(R.id.frag_header))).check(matches(not(isDisplayed())));
            } catch (Exception exception ) {
                System.out.print(exception);
            }

        } else {

            //Displays Header if phone
            onView((withId(R.id.frag_header))).check(matches(isDisplayed()));
        }

    }

}
