package org.inc.tsmgr.web.components

import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.orderedlayout.HorizontalLayout

data class ExportResource(
    val logo: Image,
    val mime: String,
    val buttons: HorizontalLayout
) {
    init {
        logo.apply {
            setWidth(32F, Unit.PIXELS)
            setHeight(32F, Unit.PIXELS)
        }
    }
}