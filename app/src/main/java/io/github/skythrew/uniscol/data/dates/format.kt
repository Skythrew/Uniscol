package io.github.skythrew.uniscol.data.dates

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

val UniscolRawDateFormat = DateTimeComponents.Format {
    year()
    char('-')
    monthNumber()
    char('-')
    dayOfMonth()
}

val UniscolLocalDateFormat = LocalDate.Format {
    dayOfWeek(
        DayOfWeekNames(
            listOf("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche",)
        )
    )
    char(' ')
    dayOfMonth()
    char(' ')
    monthName(
        MonthNames(
            listOf("Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre")
        )
    )
}