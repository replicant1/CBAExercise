package com.bailey.rod.cbaexercise.ui

import java.time.LocalDate
import java.time.temporal.ChronoField

fun LocalDate.daysAgo(oldDate: LocalDate): Long {
    val thisEpochDay = this.getLong(ChronoField.EPOCH_DAY)
    val oldEpochDay = oldDate.getLong(ChronoField.EPOCH_DAY)
    return thisEpochDay - oldEpochDay
}

fun LocalDate.daysAgoStr(oldDate: LocalDate): String {
    val daysAgo = this.daysAgo(oldDate)
    if (daysAgo == 0L) {
        return "Today"
    } else if (daysAgo == 1L) {
        return "$daysAgo day ago"
    } else if (daysAgo > 1L) {
        return "$daysAgo days ago"
    }
    else {
        return "?"
    }
}