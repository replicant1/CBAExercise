package com.bailey.rod.cbaexercise

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.bailey.rod.cbaexercise.view.AccountOverviewActivity
import com.bailey.rod.cbaexercise.view.TxListAdapter
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Assumes that sample_account_data.json is supplying the data to the UI
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class AccountOverviewActivityInstrumentedTest {

    @get: Rule
    var activityRule: ActivityScenarioRule<AccountOverviewActivity> =
        ActivityScenarioRule(AccountOverviewActivity::class.java)

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
    fun secondRowContainsFirstDateHeader() {
        onView(withId(R.id.rv_tx_list)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText("20 JUL 2017"))
                )
            )
        )
    }

    @Test
    fun thirdAndFourthRowsContainTxs() {
        onView(withId(R.id.rv_tx_list)).check(
            (matches(
                Utils.atPosition(
                    2,
                    hasDescendant(withText(startsWith(("Kaching"))))
                )
            ))
        )
        onView(withId(R.id.rv_tx_list)).check(
            (matches(
                Utils.atPosition(
                    3,
                    hasDescendant(withText(startsWith("Wdl ATM CBA")))
                )
            ))
        )
    }

    @Test
    fun fifthRowContainsSecondDateHeader() {
        onView(withId(R.id.rv_tx_list)).check(
            matches(
                Utils.atPosition(
                    4,
                    hasDescendant(withText("19 JUL 2017"))
                )
            )
        )
    }

    /**
     * Second last row in the list should be the given date header. Requires scrolling down.
     */
    @Test
    fun secondLastRowContainsLastDateHeader() {
        onView(withId(R.id.rv_tx_list))
            .check(matches(isDisplayed())).perform(
                RecyclerViewActions.scrollTo<TxListAdapter.TxViewHolder>(
                    hasDescendant(withText("17 MAY 2017"))
                )
            )
    }

    /**
     * Check last tx in list. Requires scrolling down. Only look at tx amount - matching against tx
     * description not working, probably due to HTML usage.
     */
    @Test
    fun lastRowContainsPendingTx() {
        onView(withId(R.id.rv_tx_list))
            .check(matches(isDisplayed())).perform(
                RecyclerViewActions.scrollTo<TxListAdapter.TxViewHolder>(
                    hasDescendant(withText(containsString("$-8.00")))
                )
            )
    }

}