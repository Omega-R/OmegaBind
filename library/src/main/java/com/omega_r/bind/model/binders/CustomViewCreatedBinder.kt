package com.omega_r.bind.model.binders

import android.view.View
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel

open class CustomViewCreatedBinder<V : View, M>(
    override val id: Int,
    private val viewCreatedBlock: (view: V) -> Unit
) : Binder<V, M, Any>() {

    override fun onViewCreated(itemView: V) {
        viewCreatedBlock(itemView)
    }

    override fun bind(itemView: V, item: M) {
        // nothing
    }
}

fun <V : View, M> BindModel.Builder<M>.bindCustomViewCreated(
    @IdRes id: Int,
    binder: (view: V) -> Unit
) = bindBinder(CustomViewCreatedBinder(id, binder))
