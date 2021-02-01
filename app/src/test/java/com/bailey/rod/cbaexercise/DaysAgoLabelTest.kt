package com.bailey.rod.cbaexercise

import com.bailey.rod.cbaexercise.ui.daysAgoLabel
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DaysAgoLabelTest {

    @Test
    fun todayIsLabelledToday() {
        val today = LocalDate.now()
        assertEquals("Today", today.daysAgoLabel(today))
    }

    @Test
    fun yesterdayIsLabelledOneDayAgo() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        assertEquals("1 day ago", today.daysAgoLabel(yesterday))
    }

    @Test
    fun dayBeforeYesterdayIsLabelledTwoDaysAgo() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val dayBeforeYesterday = yesterday.minusDays(1)
        assertEquals("2 days ago", today.daysAgoLabel(dayBeforeYesterday))
    }

    @Test
    fun tomorrowIsLabelledUnknown() {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        assertEquals("?", today.daysAgoLabel(tomorrow))
    }
}