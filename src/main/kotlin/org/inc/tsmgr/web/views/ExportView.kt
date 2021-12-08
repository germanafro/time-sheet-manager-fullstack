package org.inc.tsmgr.web.views

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.progressbar.ProgressBar
import com.vaadin.flow.router.Route
import org.inc.tsmgr.account.AccountController
import org.inc.tsmgr.activity.ActivityController
import org.inc.tsmgr.web.components.Navigation
import org.inc.tsmgr.web.views.ExportView.Companion.ROUTE_EXPORT

@Route(ROUTE_EXPORT)
class ExportView(
    private val accountController: AccountController,
    private val activityController: ActivityController,
) : VerticalLayout() {

    private val navigation: Navigation = Navigation()

    private val print: Button = Button().apply {
        this.icon = VaadinIcon.PRINT.create()
        addClickListener {
            printLabel.text = activityController.export()
        }
    }
    private val printLabel = Label()

    private val progressBar = ProgressBar().apply {
        this.min = .0
        this.max = 1.0
        this.value = 0.5
    }

    init {
        add(
            navigation, Image("icons/excel_logo.png", "export as SA3-excel file"), print, printLabel
        )
    }

    companion object {
        const val ROUTE_EXPORT = "/export"
    }
}