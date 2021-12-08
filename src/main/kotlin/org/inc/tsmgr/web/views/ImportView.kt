package org.inc.tsmgr.web.views

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileBuffer
import com.vaadin.flow.router.Route
import org.inc.tsmgr.account.AccountController
import org.inc.tsmgr.account.Accounts
import org.inc.tsmgr.activity.Activities
import org.inc.tsmgr.activity.ActivityController
import org.inc.tsmgr.web.components.Navigation
import org.inc.tsmgr.web.views.ImportView.Companion.ROUTE_IMPORT
import kotlin.text.Charsets.UTF_8


@Route(ROUTE_IMPORT)
class ImportView(
    private val accountController: AccountController,
    private val activityController: ActivityController,
    private val objectMapper: ObjectMapper
) : VerticalLayout() {
    private val navigation: Navigation = Navigation()

    private val buffer: MultiFileBuffer = MultiFileBuffer()
    private val fileDrop: Upload = Upload(buffer).apply {
        isAutoUpload = false
        setAcceptedFileTypes("application/json")
        addSucceededListener { event ->
            // Determine which file was uploaded
            val fileName: String = event.getFileName()
            // Get input stream specifically for the finished file
            val json: String = buffer.getInputStream(fileName).readAllBytes().toString(UTF_8)
            try {
                objectMapper.readValue<Accounts>(json)
                    .let(accountController::import)
                    .let(accountsStatus::setText)
            } catch (t: Throwable) {
                objectMapper.readValue<Activities>(json)
                    .let(activityController::importActivities)
                    .let(activitiesStatus::setText)
            }

        }
    }
    private val activitiesStatus: Label = Label()
    private val accountsStatus: Label = Label()

    init {
        add(navigation, fileDrop, activitiesStatus, accountsStatus)
    }

    companion object {
        const val ROUTE_IMPORT = "/import"
    }
}