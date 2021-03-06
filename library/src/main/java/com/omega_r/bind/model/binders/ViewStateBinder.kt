package com.omega_r.bind.model.binders

import android.view.View
import com.omega_r.bind.model.BindModel


open class ViewStateBinder<E>(
    override val id: Int,
    private val viewStateFunction: (View, Boolean) -> Unit,
    private val selector: (E) -> Boolean

) : Binder<View, E>() {

    override fun bind(itemView: View, item: E) {
        viewStateFunction(itemView, selector(item))
    }

}


fun <M> BindModel.Builder<M>.bindViewState(
    id: Int,
    viewStateFunction: (View, Boolean) -> Unit,
    selector: (M) -> Boolean
) = bindBinder(ViewStateBinder(id, viewStateFunction, selector))