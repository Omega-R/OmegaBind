package com.omega_r.bind.model.binders

import android.widget.TextView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import java.text.DateFormat
import java.util.*
import kotlin.reflect.KProperty

open class StringBinder<M>(
    override val id: Int,
    private vararg val properties: KProperty<*>,
    private val formatter: ((Any?) -> String?)? = null,
    private val defaultValue: String?
) : Binder<TextView, M>() {

    override fun bind(itemView: TextView, item: M) = BinderTextWatcher.runTextChangedTransaction(itemView) {
        val obj: Any? = item.findValue(item, properties)

        itemView.text = (if (formatter == null) obj?.toString()?.takeIf { it.isNotEmpty() } else formatter.invoke(obj))
            ?: defaultValue
    }
}


fun <M> BindModel.Builder<M>.bind(
    @IdRes id: Int,
    property: KProperty<String?>,
    formatter: ((Any?) -> String?)? = null,
    defaultValue: String? = null
) = bindString(id, property, formatter = formatter, defaultValue = defaultValue)

fun <M> BindModel.Builder<M>.bindString(
    @IdRes id: Int,
    vararg properties: KProperty<*>,
    formatter: ((Any?) -> String?)? = null,
    defaultValue: String? = null
) = bindBinder(StringBinder(id, *properties, formatter = formatter, defaultValue = defaultValue))

fun <M> BindModel.Builder<M>.bindDate(
    @IdRes id: Int,
    vararg properties: KProperty<*>,
    formatter: DateFormat = DateFormat.getDateInstance(),
    defaultValue: String? = null
) = bindString(id, properties = properties, formatter = {
        if (it is Date) {
            formatter.format(it)
        } else defaultValue
    }) as Binder<TextView, Date>
