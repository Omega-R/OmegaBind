package com.omega_r.bind.model.binders

import android.view.View
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel

open class VisibleCustomBinder<M>(override val id: Int, private val queryBlock: (item: M) -> Boolean) :
    Binder<View, M>() {

    override fun bind(itemView: View, item: M) {
        itemView.visibility = if (queryBlock(item)) View.VISIBLE else View.GONE
    }

}

fun <M> BindModel.Builder<M>.bindVisible(
    @IdRes id: Int,
    queryBlock: (M) -> Boolean
) = bindBinder(VisibleCustomBinder(id, queryBlock))