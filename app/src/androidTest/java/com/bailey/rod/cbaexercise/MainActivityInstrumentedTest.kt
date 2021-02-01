package com.bailey.rod.cbaexercise

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
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
        onView(withId(R.id.tv_account_name)).check(ViewAssertions.matches(withText("Complete Access")))
        onView(withId(R.id.tv_account_number)).check(ViewAssertions.matches(withText("062005 1709 5888")))
        onView(withId(R.id.tv_available_funds_value)).check(ViewAssertions.matches(withText("$226.76")))
        onView(withId(R.id.tv_account_balance_value)).check(ViewAssertions.matches(withText("$246.76")))
    }
}