package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import androidx.annotation.IdRes
import androidx.core.util.forEach
import com.omega_r.bind.model.BindModel

open class VisibleCustomMultiBinder<M>(id: Int, vararg ids: Int, private val queryBlock: (item: M) -> Boolean) :
    MultiBinder<View, M>(id, *ids) {

    override fun bind(views: SparseArray<View>, item: M) {
        val visibility = if (queryBlock(item)) View.VISIBLE else View.GONE
        views.forEach { _, value ->
            value.visibility = visibility
        }
    }

}

fun <M> BindModel.Builder<M>.bindVisible(
    @IdRes id: Int,
    vararg ids: Int,
    queryBlock: (M) -> Boolean
) = bindBinder(VisibleCustomMultiBinder(id, ids = *ids, queryBlock = queryBlock))