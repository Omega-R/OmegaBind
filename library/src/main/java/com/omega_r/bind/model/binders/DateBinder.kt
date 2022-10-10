package com.omega_r.bind.model.binders

import android.widget.TextView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import java.text.DateFormat
import java.util.*
import kotlin.reflect.KProperty

open class DateBinder<M>(
    override val id: Int,
    private val properties: Array<out KProperty<*>>,
    private val formatter: DateFormat,
) : Binder<TextView, M, Date>() {

    override fun bind(itemView: TextView, item: M) {
        val date: Date? = item.findValue(item, properties)
        itemView.text = date?.let { formatter.format(it) }
    }
}


fun <M> BindModel.Builder<M>.bind(
    @IdRes id: Int,
    property: KProperty<Date?>,
    formatter: DateFormat = DateFormat.getDateInstance()
) = bindDate(id, property, formatter = formatter)

fun <M> BindModel.Builder<M>.bindDate(
    @IdRes id: Int,
    vararg properties: KProperty<*>,
    formatter: DateFormat = DateFormat.getDateInstance()
) = bindBinder(DateBinder(id, properties, formatter = formatter))

