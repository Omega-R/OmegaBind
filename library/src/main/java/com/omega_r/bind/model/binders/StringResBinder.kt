package com.omega_r.bind.model.binders

import android.widget.TextView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

open class StringResBinder<M>(
    override val id: Int,
    private vararg val properties: KProperty<*>
) : Binder<TextView, M, Int>() {

    override fun bind(itemView: TextView, item: M) = BinderTextWatcher.runTextChangedTransaction(itemView) {
        val obj: Int? = item.findValue(item, properties)

        if (obj != null) itemView.setText(obj) else itemView.text = null
    }
}

@Suppress("RemoveRedundantSpreadOperator")
fun <M> BindModel.Builder<M>.bindStringRes(@IdRes id: Int, property: KProperty<Int?>) = bindStringRes(id, *arrayOf(property))


fun <M> BindModel.Builder<M>.bindStringRes(@IdRes id: Int, vararg properties: KProperty<*>) =
    bindBinder(StringResBinder(id, *properties))