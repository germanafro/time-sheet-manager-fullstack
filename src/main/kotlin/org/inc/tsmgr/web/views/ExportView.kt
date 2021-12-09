package org.inc.tsmgr.web.views

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import org.inc.tsmgr.account.AccountController
import org.inc.tsmgr.activity.ActivityController
import org.inc.tsmgr.web.components.ExportResource
import org.inc.tsmgr.web.components.Navigation
import org.inc.tsmgr.web.views.ExportView.Companion.ROUTE_EXPORT

@Route(ROUTE_EXPORT)
class ExportView(
    private val accountController: AccountController,
    private val activityController: ActivityController,
) : VerticalLayout() {

    private val navigation: Navigation = Navigation()

    private val choices: Grid<ExportResource> = Grid<ExportResource>().apply {
        this.isHeightByRows = true
        addComponentColumn(ExportResource::logo)
        addColumn(ExportResource::mime)
        addComponentColumn(ExportResource::buttons)
    }


    private val printExcel: Button = Button().apply {
        this.text = "Activity"
        this.icon = VaadinIcon.PRINT.create()
        addClickListener {
            excelLabel.text = activityController.export()
        }
    }

    private val activityJson: Button = Button().apply {
        this.text = "Activity"
        this.icon = VaadinIcon.PRINT.create()
        addClickListener {
            activityJsonLabel.text = activityController.exportJson()
        }
    }

    private val accountsJson: Button = Button().apply {
        this.text = "Accounts"
        this.icon = VaadinIcon.PRINT.create()
        addClickListener {
            accountsJsonLabel.text = accountController.exportJson()
        }
    }

    private val excel: ExportResource = ExportResource(
        Image("icons/excel_logo.png", "export as SA3-excel file"),
        "application/xlsx",
        HorizontalLayout(printExcel)
    )

    private val json: ExportResource = ExportResource(
        Image("icons/json_logo.png", "export as Json file"),
        "application/json",

        HorizontalLayout(activityJson, accountsJson)
    )

    private val excelLabel = Label()
    private val activityJsonLabel = Label()
    private val accountsJsonLabel = Label()

    init {
        choices.setItems(excel, json)
        add(
            navigation,
            choices,
            excelLabel,
            activityJsonLabel,
            accountsJsonLabel,
        )
    }

    companion object {
        const val ROUTE_EXPORT = "/export"
    }
}