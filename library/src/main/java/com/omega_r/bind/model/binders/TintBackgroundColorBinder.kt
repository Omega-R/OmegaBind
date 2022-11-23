package com.omega_r.bind.model.binders

import android.view.View
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegatypes.Color
import kotlin.reflect.KProperty

class TintBackgroundBinder<M>(
    override val id: Int,
    private val properties: Array<out KProperty<*>>,
) : Binder<View, M, Color>() {

    override fun bind(itemView: View, item: M) {
        ViewCompat.setBackgroundTintList(
            itemView,
            (item.findValue(item, properties) as? Color)?.getColorStateList(itemView.context)
        )
    }
}

fun <M> BindModel.Builder<M>.bindBackgroundTintColor(@IdRes id: Int, vararg properties: KProperty<*>) =
    bindBinder(TintBackgroundBinder(id, properties))
