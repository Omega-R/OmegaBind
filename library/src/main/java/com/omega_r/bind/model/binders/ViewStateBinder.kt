package com.omega_r.bind.model.binders

import android.view.View
import com.omega_r.bind.model.BindModel


open class ViewStateBinder<E>(
    override val id: Int,
    private val viewStateFunction: (View, Boolean) -> Unit,
    private val selector: (E) -> Boolean

) : Binder<View, E, Boolean>() {

    override fun bind(itemView: View, item: E) {
        viewStateFunction(itemView, selector(item))
    }

}


fun <M> BindModel.Builder<M>.bindViewState(
    id: Int,
    viewStateFunction: (View, Boolean) -> Unit,
    selector: (M) -> Boolean = {it == true}
) = bindBinder(ViewStateBinder(id, viewStateFunction, selector))

fun <M> BindModel.Builder<M>.bindSelected(
    id: Int,
    selector: (M) -> Boolean = {it == true}
) = bindViewState(id, selector = selector, viewStateFunction = { view, value -> view.isSelected = value})

fun <M> BindModel.Builder<M>.bindActivated(
    id: Int,
    selector: (M) -> Boolean = {it == true}
) = bindViewState(id, selector = selector, viewStateFunction = { view, value -> view.isActivated = value})

fun <M> BindModel.Builder<M>.bindEnabled(
    id: Int,
    selector: (M) -> Boolean = {it == true}
) = bindViewState(id, selector = selector, viewStateFunction = { view, value -> view.isEnabled = value})