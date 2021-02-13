package com.bailey.rod.cbaexercise.ui

import java.time.LocalDate


/**
 * @param Must be in the past
 * @return String representation of the age of something born on the given date.
 *
 * For ages in [0, 1 month) show "X days ago". Special cases if x == 0 then "today". if x == 1 then "yesterday".
 * For ages [1 month, 12 months) show "X months ago". special cases if x == 1 then "last month".
 * For ages [1 year, -) show "X years ago". Special case: if x == 1 then "last year".
 */
fun LocalDate.daysAgoLabel(then: LocalDate): String {
    val yearDiff = this.year - then.year

    if (yearDiff > 0) {
        return if (yearDiff == 1) "Last year" else "$yearDiff years ago"
    } else {
        val monthDiff = this.monthValue - then.monthValue
        if (monthDiff > 0) {
            return if (monthDiff == 1) "Last month" else "$monthDiff months ago"
        } else {
            val dayDiff = this.dayOfMonth - then.dayOfMonth
            if (dayDiff == 0) {
                return "Today"
            } else {
                return if (dayDiff == 1) "Yesterday" else "$dayDiff days ago"
            }
        }
    }
}