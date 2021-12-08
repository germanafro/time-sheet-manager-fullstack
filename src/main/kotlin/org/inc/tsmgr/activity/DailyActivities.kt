package org.inc.tsmgr.activity

data class DailyActivities(
    val date: String,
    val activities: Map<String, Activity>
)