package com.omega_r.bind.model.binders

import android.widget.TextView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

open class StringBinder<M>(
    override val id: Int,
    private vararg val properties: KProperty<*>,
    private val formatter: ((Any?) -> String?)? = null
) : Binder<TextView, M>() {

    override fun bind(itemView: TextView, item: M) = BinderTextWatcher.runTextChangedTransaction(itemView) {
        val obj: Any? = item.findValue(item, properties)

        itemView.text = if (formatter == null) obj?.toString() else formatter.invoke(obj)
    }
}


fun <M> BindModel.Builder<M>.bind(@IdRes id: Int, property: KProperty<String?>, formatter: ((Any?) -> String?)? = null) =
    bindString(id, property, formatter = formatter)

fun <M> BindModel.Builder<M>.bindString(
    @IdRes id: Int,
    vararg properties: KProperty<*>,
    formatter: ((Any?) -> String?)? = null
) = bindBinder(StringBinder(id, *properties, formatter = formatter))
