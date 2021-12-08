package org.inc.tsmgr.activity

import java.time.Month

data class MonthlyActivities(
    val month: Month,
    val dailyActivities: Map<String, DailyActivities>
)