package com.omega_r.bind.model.binders

import android.util.SparseArray
import android.view.View
import com.omega_r.bind.model.BindModel

class LambdaMultiViewBinder<V : View, M>(id: Int, vararg ids: Int, private val block: (SparseArray<V>, M) -> Unit) :
    MultiBinder<V, M>(id, *ids) {

    override fun bind(views: SparseArray<V>, item: M) {
        block(views, item)
    }

}

@Suppress("CHANGING_ARGUMENTS_EXECUTION_ORDER_FOR_NAMED_VARARGS",
    "REDUNDANT_SPREAD_OPERATOR_IN_NAMED_FORM_IN_FUNCTION"
)
fun <V : View, M> BindModel.Builder<M>.bindMultiCustom(id: Int, vararg ids: Int, block: (SparseArray<V>, M) -> Unit) =
    bindBinder(LambdaMultiViewBinder(id, ids = *ids, block = block))