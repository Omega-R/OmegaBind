package com.omega_r.bind.model.binders

import android.widget.TextView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegatypes.Text
import com.omega_r.libs.omegatypes.setText
import kotlin.reflect.KProperty

open class TextBinder<M>(
    override val id: Int,
    private vararg val properties: KProperty<*>,
    private val defaultValue: Text?
) : Binder<TextView, M>() {

    override fun bind(itemView: TextView, item: M) = BinderTextWatcher.runTextChangedTransaction(itemView) {
        val text: Text? = item.findValue(item, properties)

        itemView.setText(text?.takeIf { !text.isEmpty() } ?: defaultValue)
    }
}

fun <M> BindModel.Builder<M>.bind(@IdRes id: Int, property: KProperty<Text?>, defaultValue: Text? = null) =
    bindText(id, property, defaultValue = defaultValue)

fun <M> BindModel.Builder<M>.bindText(@IdRes id: Int, vararg properties: KProperty<*>, defaultValue: Text? = null) =
    bindBinder(TextBinder(id, *properties, defaultValue = defaultValue))