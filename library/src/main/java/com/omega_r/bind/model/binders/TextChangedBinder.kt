package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import com.omega_r.bind.model.BindModel

open class TextChangedBinder<E, R>(override val id: Int, private val block: (E, String) -> Unit) : Binder<TextView, E, R>() {

    private val binders = mutableListOf<Binder<*, E, *>>()

    override fun dispatchOnViewCreated(view: View, viewCache: SparseArray<View>) {
        super.dispatchOnViewCreated(view, viewCache)
        if (binders.isNotEmpty()) {
            BinderTextWatcher.from<E>(view).let {
                it.callbacks.add { item: E, _ ->
                    binders.forEach { callback -> callback.dispatchBind(viewCache, item) }
                }
            }
        }
    }

    override fun onViewCreated(itemView: TextView) {
        BinderTextWatcher.from<E>(itemView).let {
            it.callbacks.add(block)
            itemView.addTextChangedListener(it)
        }
    }

    override fun bind(itemView: TextView, item: E) {
        BinderTextWatcher.from<E>(itemView).item = item
    }

    fun addAutoUpdateBinder(vararg binders: Binder<*, E, *>) {
        this.binders += binders
    }

}

fun <M> BindModel.Builder<M>.bindTextChanged(id: Int, textChangedBlock: ((M, String) -> Unit)): Binder<TextView, M, M> =
    bindBinder(TextChangedBinder(id, textChangedBlock))

fun <M, R> BindModel.Builder<M>.bindTextChanged(id: Int, textChangedBlock: (String) -> Unit): Binder<TextView, M, R> =
    bindBinder(TextChangedBinder(id) { _, string -> textChangedBlock(string) })
