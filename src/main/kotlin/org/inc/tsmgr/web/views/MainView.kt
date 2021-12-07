package org.inc.tsmgr.web.views

import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route

@Route
class MainView : VerticalLayout() {

    init {
        add(Anchor("/import", "importer"))
    }
}