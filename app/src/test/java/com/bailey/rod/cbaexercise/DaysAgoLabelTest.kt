package com.bailey.rod.cbaexercise

import com.bailey.rod.cbaexercise.ui.daysAgoLabel
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DaysAgoLabelTest {

    @Test
    fun todayIsLabelledToday() {
        val today = LocalDate.of(2021, 2, 13) // 13 Feb 2021
        assertEquals("Today", today.daysAgoLabel(today))
    }

    @Test
    fun yesterdayIsLabelledYesterday() {
        val today = LocalDate.of(2021, 2, 13) // 13 Feb 2021
        val yesterday = today.minusDays(1)
        assertEquals("Yesterday", today.daysAgoLabel(yesterday))
    }

    @Test
    fun twoDaysAgoIsLabelled2DaysAgo() {
        val today = LocalDate.of(2021, 2, 13)
        val twoDaysAgo = LocalDate.now().minusDays(2)
        assertEquals("2 days ago", today.daysAgoLabel(twoDaysAgo))
    }

    @Test
    fun oneMonthAgoIsLabelledLastMonth() {
        val today = LocalDate.of(2021, 2, 13)
        val oneMonthAgo = today.minusMonths(1)
        assertEquals("Last month", today.daysAgoLabel(oneMonthAgo))
    }

    @Test
    fun eleventMonthsAgoIsLabelled11MonthsAgo() {
        val today = LocalDate.of(2020, 12, 25) // 25 Dec 2020
        val elevenMonthsAgo = today.minusMonths(11)
        assertEquals("11 months ago", today.daysAgoLabel(elevenMonthsAgo))
    }

}