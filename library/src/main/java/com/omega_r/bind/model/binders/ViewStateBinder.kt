package com.omega_r.bind.model.binders

import android.view.View
import com.omega_r.bind.model.BindModel


open class ViewStateBinder<E>(
    override val id: Int,
    private val viewState: (View, Boolean) -> Unit,
    private val selector: (E) -> Boolean

) : Binder<View, E, Boolean>() {

    override fun bind(itemView: View, item: E) {
        viewState(itemView, selector(item))
    }

}

private object EnabledViewState: (View, Boolean) -> Unit {
    override fun invoke(view: View, value: Boolean) {
        view.isEnabled = value
    }
}

private object SelectedViewState: (View, Boolean) -> Unit {
    override fun invoke(view: View, value: Boolean) {
        view.isSelected = value
    }
}

private object ActivatedViewState: (View, Boolean) -> Unit {
    override fun invoke(view: View, value: Boolean) {
        view.isActivated = value
    }
}

fun <M> BindModel.Builder<M>.bindViewState(
    id: Int,
    viewState: (View, Boolean) -> Unit,
    selector: (M) -> Boolean = {it == true}
) = bindBinder(ViewStateBinder(id, viewState, selector))

fun <M> BindModel.Builder<M>.bindSelected(
    id: Int,
    selector: (M) -> Boolean = {it == true}
) = bindViewState(id, selector = selector, viewState = SelectedViewState)

fun <M> BindModel.Builder<M>.bindActivated(
    id: Int,
    selector: (M) -> Boolean = {it == true}
) = bindViewState(id, selector = selector, viewState = ActivatedViewState)

fun <M> BindModel.Builder<M>.bindEnabled(
    id: Int,
    selector: (M) -> Boolean = {it == true}
) = bindViewState(id, selector = selector, viewState = EnabledViewState)