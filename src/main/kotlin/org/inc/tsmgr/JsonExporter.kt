package org.inc.tsmgr

import com.fasterxml.jackson.databind.ObjectMapper
import org.inc.tsmgr.account.Accounts
import org.inc.tsmgr.activity.Activities
import org.springframework.stereotype.Component
import java.io.File
import kotlin.text.Charsets.UTF_8

@Component
class JsonExporter(
    private val objectMapper: ObjectMapper
) {
    val export_path = "export"

    fun exportActivities(activities: Activities): String {
        val pathname = "$export_path/activities.json"
        File(export_path).mkdirs()
        return File(pathname).also { file ->
            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(activities)
                .let { file.writeText(it, UTF_8) }
        }.absolutePath
    }

    fun exportAccounts(accounts: Accounts): String {
        val pathname = "$export_path/accounts.json"
        File(export_path).mkdirs()
        return File(pathname).also { file ->
            objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(accounts)
                .let { file.writeText(it, UTF_8) }
        }.absolutePath
    }
}