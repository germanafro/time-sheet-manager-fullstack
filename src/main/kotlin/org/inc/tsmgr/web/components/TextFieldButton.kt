package org.inc.tsmgr.web.components

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField

class TextFieldButton(
    val buttonLabel: String,
    val defaultValue: String,
    val action: (String) -> String
) : HorizontalLayout() {
    val textField: TextField = TextField().apply { value = defaultValue }
    val button: Button = Button(buttonLabel).apply {
        addClickListener {
            status.text = "executing..."
            try {
                action.invoke(textField.value)
                    .let(status::setText)
            } catch (throwable: Throwable){
                status.text = throwable.message
            }


        }
    }
    val status: Label = Label("")

    init {
        add(textField)
        add(button)
        add(status)
    }
}