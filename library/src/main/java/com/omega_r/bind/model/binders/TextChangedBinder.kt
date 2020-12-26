package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import android.widget.TextView
import com.omega_r.bind.model.BindModel

open class TextChangedBinder<E>(override val id: Int, private val block: (E, String) -> Unit) :
    Binder<TextView, E>() {

    private val autoBinders = mutableListOf<Binder<*, E>>()

    override fun dispatchOnCreateView(view: View, viewCache: SparseArray<View>) {
        super.dispatchOnCreateView(view, viewCache)
        if (autoBinders.isNotEmpty()) {
            BinderTextWatcher.from<E>(view).let {
                it.callbacks.add { item: E, _ ->
                    autoBinders.forEach { callback -> callback.dispatchBind(viewCache, item) }
                }
            }
        }
    }

    override fun onCreateView(itemView: TextView) {
        BinderTextWatcher.from<E>(itemView).let {
            it.callbacks.add(block)
            itemView.addTextChangedListener(it)
        }
    }

    override fun bind(itemView: TextView, item: E) {
        BinderTextWatcher.from<E>(itemView).item = item
    }

    fun addAutoUpdateBinder(vararg binders: Binder<*, E>) {
        autoBinders += binders
    }

}

fun <M> BindModel.Builder<M>.bindTextChanged(id: Int, textChangedBlock: ((M, String) -> Unit)) =
    bindBinder(TextChangedBinder(id, textChangedBlock))