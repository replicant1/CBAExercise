package com.bailey.rod.cbaexercise

import androidx.test.espresso.DataInteraction
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasEntry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Assumes that sample_account_data.json is supplying the data to the UI
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityInstrumentedTest {

    @get: Rule
    var activityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.bailey.rod.cbaexercise", appContext.packageName)
    }

    @Test
    fun accountHeaderIsPopulatedOk() {
        onView(withId(R.id.tv_account_name)).check(matches(withText("Complete Access")))
        onView(withId(R.id.tv_account_number)).check(matches(withText("062005 1709 5888")))
        onView(withId(R.id.tv_available_funds_value)).check(matches(withText("$226.76")))
        onView(withId(R.id.tv_account_balance_value)).check(matches(withText("$246.76")))
    }

    @Test
    fun secondRowIsCorrectDateHeader() {
        onView(withText("20 JUL 2017")).check(matches(isDisplayed()))
    }

}