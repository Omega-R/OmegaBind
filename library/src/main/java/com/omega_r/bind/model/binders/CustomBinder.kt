package com.omega_r.bind.model.binders

import android.view.View
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel


open class CustomBinder<V : View, M>(override val id: Int, val binder: (view: V, item: M) -> Unit) :
    Binder<V, M, Any>() {

    override fun bind(itemView: V, item: M) = binder(itemView, item)

}

fun <V : View, M> BindModel.Builder<M>.bindCustom(
    @IdRes id: Int,
    binder: (view: V, item: M) -> Unit
) = bindBinder(CustomBinder(id, binder))
