package org.inc.tsmgr.util

import java.text.SimpleDateFormat
import java.util.*

class TimeUtils {
    companion object {

        fun formatDate(date: Date): String =
            SimpleDateFormat("dd.MM.yyyy").format(date)

        fun parseDate(date: String): Date =
            SimpleDateFormat("dd.MM.yyyy").parse(date)


    }
}