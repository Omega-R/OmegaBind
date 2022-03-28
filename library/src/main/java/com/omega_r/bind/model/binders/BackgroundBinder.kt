package com.omega_r.bind.model.binders

import android.view.View
import android.widget.ImageView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegatypes.image.Image
import com.omega_r.libs.omegatypes.image.setBackground
import com.omega_r.libs.omegatypes.image.setImage
import kotlin.reflect.KProperty

open class BackgroundBinder<M>(
    override val id: Int,
    private vararg val properties: KProperty<*>,
    private val placeholderResId: Int = 0
) : Binder<View, M>() {

    override fun bind(itemView: View, item: M) {
        itemView.setBackground(item.findValue(item, properties), placeholderResId)
    }

}

fun <M> BindModel.Builder<M>.bindBackground(@IdRes id: Int, vararg properties: KProperty<*>, placeholderRes: Int = 0) =
    bindBinder(BackgroundBinder(id, *properties, placeholderResId = placeholderRes))
