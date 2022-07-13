package com.omega_r.bind.model.binders

import android.view.View
import androidx.annotation.IdRes
import com.omega_r.bind.R
import com.omega_r.bind.model.BindModel
import com.omega_r.click.ClickManager

open class LongClickBinder<M>(
    override val id: Int,
    private val block: (M) -> Boolean
) : Binder<View, M, Any>() {

    override fun bind(itemView: View, item: M) {
        itemView.setOnLongClickListener {
            block(item)
        }
    }
}

fun <M> BindModel.Builder<M>.bindLongClick(@IdRes id: Int, block: (M) -> Boolean) = bindBinder(LongClickBinder(id, block))
