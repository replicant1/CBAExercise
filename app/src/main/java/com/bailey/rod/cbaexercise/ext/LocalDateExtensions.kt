package com.bailey.rod.cbaexercise.ext

import android.content.Context
import com.bailey.rod.cbaexercise.R
import java.time.LocalDate


/**
 * @param Must be in the past
 * @return String representation of the age of something born on the given date.
 *
 * For ages in [0, 1 month) show "X days ago". Special cases if x == 0 then "today". if x == 1 then "yesterday".
 * For ages [1 month, 12 months) show "X months ago". special cases if x == 1 then "last month".
 * For ages [1 year, -) show "X years ago". Special case: if x == 1 then "last year".
 */
fun LocalDate.daysAgoLabel(context: Context, then: LocalDate): String {
    val yearDiff = this.year - then.year

    if (yearDiff > 0) {
        return if (yearDiff == 1)
            context.getString(R.string.age_one_year_ago)
        else
            context.getString(R.string.age_many_years_ago, yearDiff)
    } else {
        val monthDiff = this.monthValue - then.monthValue
        if (monthDiff > 0) {
            return if (monthDiff == 1)
                context.getString(R.string.age_one_month_ago)
            else
                context.getString(R.string.age_many_months_ago, monthDiff)
        } else {
            val dayDiff = this.dayOfMonth - then.dayOfMonth
           return if (dayDiff == 0) {
                 context.getString(R.string.age_no_days_ago)
            } else {
                 if (dayDiff == 1)
                     context.getString(R.string.age_one_day_ago)
                 else
                     context.getString(R.string.age_many_days_ago, dayDiff)
            }
        }
    }
}