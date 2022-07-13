package com.omega_r.bind.model.binders

import android.os.Build
import android.widget.ProgressBar
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import kotlin.reflect.KProperty

open class ProgressBarBinder<M>(
    override val id: Int,
    private vararg val properties: KProperty<*>,
    private val min: Int? = null,
    private val max: Int? = null,
) : Binder<ProgressBar, M, Int>() {

    override fun onViewCreated(itemView: ProgressBar) {
        super.onViewCreated(itemView)
        max?.let { itemView.max = max }
        min?.let {
            itemView.max = itemView.max - min
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                itemView.min = 0
            }
        }
    }

    override fun bind(itemView: ProgressBar, item: M) {
        val progress: Int? = item.findValue(item, properties)
        progress?.let {
            itemView.progress = progress
        }
    }
}

fun <M> BindModel.Builder<M>.bind(@IdRes id: Int, property: KProperty<Int?>) = bindProgress(id, property)

fun <M> BindModel.Builder<M>.bindProgress(@IdRes id: Int, vararg properties: KProperty<*>) =
    bindBinder(ProgressBarBinder(id, *properties))