package com.omega_r.bind.model.binders

import android.widget.ImageView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegatypes.image.Image
import com.omega_r.libs.omegatypes.image.setImage
import kotlin.reflect.KProperty

open class ImageBinder<M>(
    override val id: Int,
    private vararg val properties: KProperty<*>,
    private val placeholderResId: Int = 0
) : Binder<ImageView, M>() {

    override fun bind(itemView: ImageView, item: M) {
        itemView.setImage(item.findValue(item, properties), placeholderResId)
    }

}

fun <M> BindModel.Builder<M>.bind(@IdRes id: Int, property: KProperty<Image?>, placeholderRes: Int = 0) =
    bindImage(id, property, placeholderRes = placeholderRes)

fun <M> BindModel.Builder<M>.bindImage(@IdRes id: Int, vararg properties: KProperty<*>, placeholderRes: Int = 0) =
    bindBinder(ImageBinder(id, *properties, placeholderResId = placeholderRes))
