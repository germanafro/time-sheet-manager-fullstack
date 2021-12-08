package org.inc.tsmgr.web.views

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import org.inc.tsmgr.account.AccountController
import org.inc.tsmgr.activity.ActivityController
import org.inc.tsmgr.web.components.ActivityTable
import org.inc.tsmgr.web.components.Navigation

@Route
class MainView(
    private val accountController: AccountController,
    private val activityController: ActivityController,
) : VerticalLayout() {

    private val navigation: Navigation = Navigation()
    private val activityTable: ActivityTable = ActivityTable(accountController, activityController)

    init {
        add(navigation)
        add(activityTable)
    }
}