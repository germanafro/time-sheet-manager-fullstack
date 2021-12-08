package org.inc.tsmgr.util

import org.inc.tsmgr.activity.WeekDay
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class TimeUtils {
    companion object {

        private const val timeZone = "UTC"

        fun now(): Date = Date()
            .let(::formatDate)
            .let(::parseDate)

        fun nowFormatted(): String = Date()
            .let(::formatDate)

        fun formatDate(date: Date): String =
            SimpleDateFormat("dd.MM.yyyy").format(date)

        fun formatDate(date: LocalDate): String = date
            .atStartOfDay()
            .atZone(ZoneId.of(timeZone))
            .toInstant()
            .let(Date::from)
            .let(::formatDate)

        fun parseDate(date: String): Date =
            SimpleDateFormat("dd.MM.yyyy").parse(date)

        fun weekDay(date: String): WeekDay = date
            .let(::parseDate)
            .let(::weekDay)

        fun weekDay(date: Date): WeekDay = Calendar.getInstance()
            .apply { time = date }
            .get(Calendar.DAY_OF_WEEK)
            .let { (it-2).mod(7)+1 }
            .let { DayOfWeek.of(it) }
            ?.let { WeekDay.valueOf(it) }
            ?: throw Exception("something went wrong when determining weekday from date")


    }
}