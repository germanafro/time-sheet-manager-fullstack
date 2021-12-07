package org.inc.tsmgr.account

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Account(
    val id: String,
    val description: String,
    val needsDetails: String,
)
