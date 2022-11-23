package com.omega_r.bind.model.binders

import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.widget.ImageViewCompat
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegatypes.Color
import kotlin.reflect.KProperty

class TintColorBinder<M>(
    override val id: Int,
    private val properties: Array<out KProperty<*>>,
) : Binder<ImageView, M, Color>() {

    override fun bind(itemView: ImageView, item: M) {
        ImageViewCompat.setImageTintList(
            itemView,
            (item.findValue(item, properties) as? Color)?.getColorStateList(itemView.context)
        )
    }
}

fun <M> BindModel.Builder<M>.bindTintColor(@IdRes id: Int, vararg properties: KProperty<*>) =
    bindBinder(TintColorBinder(id, properties))
