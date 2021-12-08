package org.inc.tsmgr.activity

import java.time.Month

data class YearlyActivities(
    val year: Int,
    val monthlyActivities: Map<Month, MonthlyActivities>
)