package org.inc.tsmgr.web.components

import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import org.inc.tsmgr.web.views.ExportView.Companion.ROUTE_EXPORT
import org.inc.tsmgr.web.views.ImportView.Companion.ROUTE_IMPORT


class Navigation : HorizontalLayout() {
    private val import: Anchor = Anchor(ROUTE_IMPORT, "Importer")
    private val export: Anchor = Anchor(ROUTE_EXPORT, "Exporter")
    private val home: Anchor = Anchor("/", "Home")
    private val swagger: Anchor = Anchor("/swagger.html", "Swagger-UI") // TODO

    init {
        this.alignItems = FlexComponent.Alignment.CENTER
        add(home, import, export, swagger)
    }
}