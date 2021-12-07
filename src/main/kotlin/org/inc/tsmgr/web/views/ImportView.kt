package org.inc.tsmgr.web.views

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import org.inc.tsmgr.account.AccountService
import org.inc.tsmgr.web.components.TextFieldButton

@Route("/import")
class ImportView(accountService: AccountService) : VerticalLayout() {
    private val IMPORT_PATH = "local/accounts.json"

    private val textFieldButton: TextFieldButton =
        TextFieldButton("import accounts", IMPORT_PATH) {
            accountService.import(it)
        }

    init {
        add(textFieldButton)
    }
}