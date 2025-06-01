package io.github.skythrew.uniscol.data.dates

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.until

fun LocalDate.isoWeekNumber(): Int {
    if (firstWeekInYearStart(year + 1) < this) return 1

    val currentYearStart = firstWeekInYearStart(year)
    val start = if (this < currentYearStart) firstWeekInYearStart(year - 1) else currentYearStart

    val currentCalendarWeek = start.until(this, DateTimeUnit.WEEK) + 1

    return currentCalendarWeek
}

fun firstWeekInYearStart(year: Int): LocalDate {
    val jan1st = LocalDate(year, 1, 1)
    val previousMonday = jan1st.minus(jan1st.dayOfWeek.ordinal, DateTimeUnit.DAY)

    return if (jan1st.dayOfWeek <= DayOfWeek.THURSDAY) previousMonday else previousMonday.plus(
        1,
        DateTimeUnit.WEEK
    )
}