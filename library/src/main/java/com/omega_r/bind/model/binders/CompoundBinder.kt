package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import android.widget.CompoundButton
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

open class CompoundBinder<E>(
    override val id: Int,
    private vararg val properties: KProperty<*>,
    private val block: ((E, Boolean) -> Unit)? = null
) : Binder<CompoundButton, E>() {

    private val autoBinders = mutableListOf<Binder<*, E>>()

    override fun dispatchBind(viewCache: SparseArray<View>, item: E) {
        super.dispatchBind(viewCache, item)
        viewCache[id]?.let { view ->
            val itemView = view as CompoundButton
            block?.let {
                itemView.setOnCheckedChangeListener { _, checked: Boolean ->
                    it(item, checked)
                    autoBinders.forEach { it.dispatchBind(viewCache, item) }
                }
            }
        }
    }

    override fun bind(itemView: CompoundButton, item: E) {
        val checked: Boolean? = item.findValue(item, properties)
        block?.let { itemView.setOnCheckedChangeListener(null) }

        itemView.isChecked = checked ?: false
    }

    fun addAutoUpdateBinder(vararg binders: Binder<*, E>) {
        autoBinders += binders
    }
}

fun <M> BindModel.Builder<M>.bindChecked(id: Int, callback: ((M, Boolean) -> Unit)?, vararg properties: KProperty<*>) =
    bindBinder(CompoundBinder(id, *properties, block = callback))