package com.omega_r.bind.model.binders

import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.widget.TextViewCompat
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegatypes.Color
import kotlin.reflect.KProperty

class TintCompoundColorBinder<M>(
    override val id: Int,
    private val properties: Array<out KProperty<*>>,
) : Binder<TextView, M, Color>() {

    override fun bind(itemView: TextView, item: M) {
        TextViewCompat.setCompoundDrawableTintList(
            itemView,
            (item.findValue(item, properties) as? Color)?.getColorStateList(itemView.context)
        )
    }
}

fun <M> BindModel.Builder<M>.bindDrawableTintColor(@IdRes id: Int, property: KProperty<Color>) =
    bindBinder(TintCompoundColorBinder(id, arrayOf(property)))

fun <M> BindModel.Builder<M>.bindDrawableTintColor(@IdRes id: Int, vararg properties: KProperty<*>) =
    bindBinder(TintCompoundColorBinder(id, properties))
