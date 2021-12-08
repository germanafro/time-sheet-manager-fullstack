package org.inc.tsmgr.account

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Account(
    @Id
    val id: String,
    val description: String,
    val needsDetails: String,
)
