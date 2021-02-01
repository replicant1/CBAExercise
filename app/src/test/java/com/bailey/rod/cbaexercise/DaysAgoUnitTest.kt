package com.bailey.rod.cbaexercise

import com.bailey.rod.cbaexercise.ui.daysAgo
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DaysAgoUnitTest {

    @Test
    fun todayIsZeroDaysBeforeToday() {
        val today = LocalDate.now()
        assertEquals(today.daysAgo(today), 0L)
    }

    @Test
    fun yesterdayIsOneDayBeforeToday() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        assertEquals(today.daysAgo(yesterday), 1)
    }

    @Test
    fun dayBeforeYesterdayIsTwoDaysBeforeToday() {
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val dayBeforeYesterday = yesterday.minusDays(1)
        assertEquals(today.daysAgo(dayBeforeYesterday), 2)
    }

    @Test
    fun tomorrowIsMinusOneDaysBeforeToday() {
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        assertEquals(today.daysAgo(tomorrow), -1)
    }
}