package com.omega_r.bind.model.binders

import android.widget.TextView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

open class CharSequenceBinder<M>(
    override val id: Int,
    private vararg val properties: KProperty<*>
) : Binder<TextView, M>() {

    override fun bind(itemView: TextView, item: M) = BinderTextWatcher.runTextChangedTransaction(itemView) {
        val charSequence: CharSequence? = item.findValue(item, properties)
        itemView.text = charSequence
    }

}

@Suppress("RemoveRedundantSpreadOperator")
fun <M> BindModel.Builder<M>.bind(@IdRes id: Int, property: KProperty<CharSequence?>) =
    bindCharSequence(id, *arrayOf(property))

fun <M> BindModel.Builder<M>.bindCharSequence(
    @IdRes id: Int,
    vararg properties: KProperty<*>
) = bindBinder(CharSequenceBinder(id, *properties))