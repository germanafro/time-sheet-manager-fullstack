package org.inc.tsmgr.activity

import org.inc.tsmgr.web.components.ActivityResource
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Activity(
    @Id
    val id: ActivityId,
    val date: String = id.date,
    val accountId: String = id.accountId,
    val hours: Double = -1.0,
    val comment: String = ""
) {
    companion object {
        fun from(resource: ActivityResource, date: String) = Activity(
            id = ActivityId(date, resource.accountId),
            date = date,
            accountId = resource.accountId,
            hours = resource.hours.toDouble(),
            comment = resource.comment,
        )
    }
}