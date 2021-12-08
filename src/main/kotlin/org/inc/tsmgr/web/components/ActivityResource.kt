package org.inc.tsmgr.web.components

import org.inc.tsmgr.activity.Activity

data class ActivityResource(
    var accountId: String,
    var hours: String,
    var comment: String
) {

    companion object {
        fun from(activity: Activity) = ActivityResource(
            accountId = activity.accountId,
            hours = activity.hours.toString(),
            comment = activity.comment,
        )
    }

}