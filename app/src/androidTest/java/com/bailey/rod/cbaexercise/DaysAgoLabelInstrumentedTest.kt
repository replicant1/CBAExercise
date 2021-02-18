package com.bailey.rod.cbaexercise

import android.content.Context
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.bailey.rod.cbaexercise.ext.daysAgoLabel
import com.bailey.rod.cbaexercise.view.AccountOverviewActivity
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@LargeTest
class DaysAgoLabelInstrumentedTest {

    lateinit var ctx : Context

    @get: Rule
    var activityRule: ActivityScenarioRule<AccountOverviewActivity> =
        ActivityScenarioRule(AccountOverviewActivity::class.java)

    @Before
    fun populateCtx() {
        ctx = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        org.junit.Assert.assertEquals("com.bailey.rod.cbaexercise", appContext.packageName)
    }

    @Test
    fun todayIsLabelledToday() {
        val today = LocalDate.of(2021, 2, 13) // 13 Feb 2021
        assertEquals(ctx.getString(R.string.age_no_days_ago), today.daysAgoLabel(ctx, today))
    }

    @Test
    fun yesterdayIsLabelledYesterday() {
        val today = LocalDate.of(2021, 2, 13) // 13 Feb 2021
        val yesterday = today.minusDays(1)
        assertEquals(ctx.getString(R.string.age_one_day_ago), today.daysAgoLabel(ctx,yesterday))
    }

    @Test
    fun twoDaysAgoIsLabelled2DaysAgo() {
        val today = LocalDate.of(2021, 2, 13) // 13 Feb 2021
        val twoDaysAgo = today.minusDays(2)
        assertEquals(ctx.getString(R.string.age_many_days_ago, 2), today.daysAgoLabel(ctx,twoDaysAgo))
    }

    @Test
    fun oneMonthAgoIsLabelledLastMonth() {
        val today = LocalDate.of(2021, 2, 13)
        val oneMonthAgo = today.minusMonths(1)
        assertEquals(ctx.getString(R.string.age_one_month_ago), today.daysAgoLabel(ctx,oneMonthAgo))
    }

    @Test
    fun elevenMonthsAgoIsLabelled11MonthsAgo() {
        val today = LocalDate.of(2020, 12, 25) // 25 Dec 2020
        val elevenMonthsAgo = today.minusMonths(11)
        assertEquals(ctx.getString(R.string.age_many_months_ago, 11), today.daysAgoLabel(ctx,elevenMonthsAgo))
    }

    @Test
    fun oneYearAgoIsLabelledLastYear() {
        val today = LocalDate.of(2021, 2, 14) // 14 Feb 2021
        val oneYearAgo = today.minusYears(1)
        assertEquals(ctx.getString(R.string.age_one_year_ago), today.daysAgoLabel(ctx,oneYearAgo))
    }

    @Test
    fun twoYearsAgoIsLabelled2YearsAgo() {
        val today = LocalDate.of(2021, 2, 14) // 14 Feb 2021
        val twoYearsAgo = today.minusYears(2)
        assertEquals(ctx.getString(R.string.age_many_years_ago, 2), today.daysAgoLabel(ctx,twoYearsAgo))
    }

}