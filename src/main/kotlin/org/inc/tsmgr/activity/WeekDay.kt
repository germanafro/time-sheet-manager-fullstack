package org.inc.tsmgr.activity

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.time.DayOfWeek

enum class WeekDay(
    val javaWeekDay: DayOfWeek,
    @JsonValue
    val de: String
) {
    MONDAY(DayOfWeek.MONDAY, "Mo"),
    TUESDAY(DayOfWeek.TUESDAY, "Di"),
    WEDNESDAY(DayOfWeek.WEDNESDAY, "Mi"),
    THURSDAY(DayOfWeek.THURSDAY, "Do"),
    FRIDAY(DayOfWeek.FRIDAY, "Fr"),
    SATURDAY(DayOfWeek.SATURDAY, "Sa"),
    SUNDAY(DayOfWeek.SUNDAY, "So");

    @JsonCreator
    fun de(de: String): WeekDay? = deMap[de]



    companion object {
        fun valueOf(javaWeekDay: DayOfWeek): WeekDay? = javaMap[javaWeekDay]
        private val deMap: Map<String, WeekDay> = values().associateBy { it.de }
        private val javaMap: Map<DayOfWeek, WeekDay> = values().associateBy { it.javaWeekDay }
    }

}