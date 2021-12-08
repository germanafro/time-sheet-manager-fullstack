package org.inc.tsmgr.web.components

import com.vaadin.flow.component.Focusable
import com.vaadin.flow.component.HasText
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.grid.dataview.GridListDataView
import com.vaadin.flow.component.grid.editor.Editor
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationResult
import org.inc.tsmgr.account.Account
import org.inc.tsmgr.account.AccountController
import org.inc.tsmgr.activity.Activity
import org.inc.tsmgr.activity.ActivityController
import org.inc.tsmgr.activity.ActivityId
import org.inc.tsmgr.util.TimeUtils.Companion.formatDate
import java.time.LocalDate


class ActivityTable(
    private val accountController: AccountController,
    private val activityController: ActivityController,
) : VerticalLayout() {

    private val datePicker: DatePicker = DatePicker().apply {
        addValueChangeListener { reloadActivities() }
    }

    private val accountPicker: ComboBox<String> = ComboBox<String>()
    private val newAccount = Button("create").apply {
        addClickListener {
            ActivityId(datePicker.dateString(), accountPicker.value)
                .let { Activity(it, it.date, it.accountId, 0.0, "") }
                .let(activityController::saveActivity)
            reloadActivities()
        }
    }


    private val activityControlPanel = HorizontalLayout().apply {
        this.add(datePicker, accountPicker, newAccount)
    }


    private val accounts: MutableMap<String, Account> = HashMap<String, Account>().apply {
        putAll(accountController.accounts().associateBy(Account::id))
    }

    private var items: Map<String, ActivityResource> = emptyMap()
    private val binder: Binder<ActivityResource> = Binder()

    final val grid: Grid<ActivityResource> = Grid<ActivityResource>()
    private val accountValidationMessage = ValidationMessage()

    private val hoursValidationMessage = ValidationMessage()
    private val commentValidationMessage = ValidationMessage()

    fun updateBackend(new: ActivityResource, old: ActivityResource) {
        activityController.deleteActivity(Activity.from(old, datePicker.dateString()))
        activityController.saveActivity(Activity.from(new, datePicker.dateString()))
        reloadActivities()
    }

    private fun reloadActivities() {
        items = activityController.getActivities(datePicker.dateString()).map(ActivityResource::from)
            .associateBy { it.accountId }
        grid.setItems(items.values)
        getAvailableAccounts().let { availableAccounts ->
            accountPicker.setItems(availableAccounts)
            accountPicker.value = availableAccounts.first()
        }
    }

    private fun getUsedAccounts(): Set<String> = items.keys

    private fun getAvailableAccounts(): List<String> = accounts.keys
        .filter { !getUsedAccounts().contains(it) }
        .sorted()

    init {
        datePicker.value = LocalDate.now()
        grid.apply {
            editor.binder = binder
            addItemDoubleClickListener { e ->
                editor.editItem(e.item)
                val editorComponent = e.column.editorComponent
                if (editorComponent is Focusable<*>) {
                    (editorComponent as Focusable<*>).focus()
                }
            }

            addComponentColumn { resource ->
                ComboBox<String>().apply {
                    this.setItems(getAvailableAccounts())
                    this.value = resource.accountId
                    this.addValueChangeListener { event ->
                        updateBackend(resource.copy(accountId = event.value), resource)
                    }
                }
            }.setHeader("Account")

            initColumn(addColumn(ActivityResource::hours).setHeader("Hours")) { field ->
                field.addValueChangeListener { }
                binder.forField(field).asRequired().withValidator { value, _ ->
                    value.toDoubleOrNull()?.let { ValidationResult.ok() } ?: ValidationResult.error("not a number")
                }.bind({ it.hours }, { it, new -> updateBackend(it.copy(hours = new), it) })
            }

            initColumn(addColumn(ActivityResource::comment).setHeader("Comment")) { field ->
                binder.forField(field).bind({ it.comment }, { it, new -> updateBackend(it.copy(comment = new), it) })
            }
            addComponentColumn { item ->
                Button().apply {
                    icon = VaadinIcon.TRASH.create()
                    addClickListener {
                        activityController.deleteActivity(
                            Activity.from(
                                item, datePicker.dateString()
                            )
                        )
                        reloadActivities()
                    }
                }
            }
        }
        add(
            activityControlPanel, grid, accountValidationMessage, hoursValidationMessage, commentValidationMessage
        )
    }

    fun setItems(items: Iterable<ActivityResource>): GridListDataView<ActivityResource> = grid.setItems(items.toList())

    private fun Grid<ActivityResource>.initColumn(
        column: Grid.Column<ActivityResource>, bind: (TextField) -> Binder.Binding<*, *>
    ) = TextField().let { field ->
        field.setWidthFull()
        addCloseHandler(field, editor)
        bind.invoke(field)
        column.editorComponent = field
    }

    private fun addCloseHandler(textField: com.vaadin.flow.component.Component, editor: Editor<ActivityResource>) {
        textField.getElement().addEventListener("keydown") { e -> editor.cancel() }.setFilter("event.code === 'Escape'")
    }

    internal class ValidationMessage : HorizontalLayout(), HasText {
        private val span = Span()
        override fun getText(): String {
            return span.text
        }

        override fun setText(text: String?) {
            span.text = text
            this.isVisible = text != null && !text.isEmpty()
        }

        init {
            isVisible = false
            alignItems = FlexComponent.Alignment.CENTER
            style["color"] = "var(--lumo-error-text-color)"
            themeList.clear()
            themeList.add("spacing-s")
            val icon: Icon = VaadinIcon.EXCLAMATION_CIRCLE_O.create()
            icon.setSize("16px")
            add(icon, span)
        }
    }

    private fun DatePicker.dateString(): String = this.value.let(::formatDate)
}