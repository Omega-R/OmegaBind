package com.omega_r.bind.model.binders

import android.view.View
import androidx.annotation.IdRes
import com.omega_r.bind.R
import com.omega_r.bind.model.BindModel
import com.omega_r.click.ClickManager

open class ClickBinder<M>(
    override val id: Int,
    private val block: (M) -> Unit
) : Binder<View, M>() {

    override fun onCreateView(itemView: View) {
        val tag = itemView.getTag(R.id.omega_click_bind) as? ClickManager
        if (tag == null) {
            itemView.setTag(R.id.omega_click_bind, ClickManager())
        }
    }

    override fun bind(itemView: View, item: M) {
        val clickManager = itemView.getTag(R.id.omega_click_bind) as ClickManager
        itemView.setOnClickListener(clickManager.wrap(id, fun() { block(item) }))
    }
}

fun <M> BindModel.Builder<M>.bindClick(@IdRes id: Int, block: (M) -> Unit) = bindBinder(ClickBinder(id, block))
