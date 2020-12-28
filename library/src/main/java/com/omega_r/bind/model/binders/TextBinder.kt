package com.omega_r.bind.model.binders

import android.widget.TextView
import androidx.annotation.IdRes
import com.omega_r.bind.model.BindModel
import com.omega_r.libs.omegatypes.Text
import com.omega_r.libs.omegatypes.setText
import kotlin.reflect.KProperty

open class TextBinder<M>(override val id: Int, private vararg val properties: KProperty<*>) : Binder<TextView, M>() {

    override fun bind(itemView: TextView, item: M) = BinderTextWatcher.runTextChangedTransaction(itemView) {
        val text: Text? = item.findValue(item, properties)

        if (text != null) {
            itemView.setText(text)
        } else {
            itemView.text = null
        }
    }
}

fun <M> BindModel.Builder<M>.bind(@IdRes id: Int, property: KProperty<Text?>) = bindText(id, property)

fun <M> BindModel.Builder<M>.bindText(@IdRes id: Int, vararg properties: KProperty<*>) = bindBinder(TextBinder(id, *properties))